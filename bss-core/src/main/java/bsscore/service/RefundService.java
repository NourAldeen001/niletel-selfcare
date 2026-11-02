package bsscore.service;

import bsscore.common.BusinessException;
import bsscore.common.ErrorCode;
import bsscore.common.ResponseHelper;
import bsscore.common.Validation;
import bsscore.dao.BalanceDao;
import bsscore.dao.Jdbc;
import bsscore.dao.LedgerDao;
import bsscore.model.Ledger;
import bsscore.model.RefundResult;
import bsscore.model.SubscriberBalanceInfo;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.sql.Connection;
import java.sql.SQLException;

@Stateless
public class RefundService {
    @Inject
    private Jdbc jdbc;
    @Inject
    private BalanceDao balanceDao;
    @Inject
    private LedgerDao ledgerDao;

    public RefundResult refund(String msisdn,String refNo){

        Validation.requireNotBlank(refNo);
        Validation.requireMatch(refNo,Validation.REF_NO);

        try(Connection connection=jdbc.getConnection()){

            Long debitAmountPiasters=ledgerDao.findDebitAmount(connection,refNo,msisdn);
            if (debitAmountPiasters == null) {
                throw new BusinessException(ErrorCode.TXN404);
            }

//            if(!ledgerDao.debitForSubscriber(connection,refNo,msisdn)){
//                throw new BusinessException(ErrorCode.TXN404);
//            }
            if(ledgerDao.refundRefNoExists(connection,refNo,msisdn)){
                throw new BusinessException(ErrorCode.TXN003);
            }

            SubscriberBalanceInfo balanceInfo=balanceDao.findBalanceInfo(connection,msisdn);
            if (balanceInfo == null) {
                throw new BusinessException(ErrorCode.TXN404);
            }
            long currentBalance=balanceInfo.amountPiasters();
            long balanceAfter=currentBalance+debitAmountPiasters;

            Ledger refundLedger=new Ledger(msisdn,"F",debitAmountPiasters,
                                     balanceAfter,refNo,"REFUND");

            int refund=balanceDao.refund(connection,msisdn,debitAmountPiasters);
            if (refund!=1){
                throw new BusinessException(ErrorCode.TXN404);
            }
            long txnId=ledgerDao.insertLedgerRefund(connection,refundLedger);

            return new RefundResult(txnId,debitAmountPiasters,balanceAfter);

        } catch (SQLException e) {

            if (ResponseHelper.isUniqueViolation(e)) {
                throw new BusinessException(ErrorCode.TXN002);
            }
            throw new RuntimeException("Refund transaction failed",e);
        }


    }

}
