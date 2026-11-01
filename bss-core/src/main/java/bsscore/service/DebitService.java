package bsscore.service;

import bsscore.common.BusinessException;
import bsscore.common.ErrorCode;
import bsscore.common.Validation;
import bsscore.dao.BalanceDao;
import bsscore.dao.Jdbc;
import bsscore.dao.LedgerDao;
import bsscore.model.DebitResult;
import bsscore.model.Ledger;
import bsscore.model.SubscriberBalanceInfo;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.sql.Connection;
import java.sql.SQLException;

@Stateless
public class DebitService {

    @Inject
    private Jdbc jdbc;
    @Inject
    private LedgerDao ledgerDao;
    @Inject
    private BalanceDao balanceDao;


    public DebitResult debit(String msisdn,String piasters,String refNo,String reason){

        Validation.requireNotBlank(piasters);
        Validation.requireMatch(refNo,Validation.REF_NO);
        Validation.requireNotBlank(reason);

        if(reason.length()>100){
            throw new BusinessException(ErrorCode.VAL001);
        }

        long amountPiasters=Validation.validateAmount(piasters);

        try(Connection connection= jdbc.getConnection()){

            SubscriberBalanceInfo subscriberInfo=balanceDao.findBalanceInfo(connection,msisdn);
            if(subscriberInfo==null){
                throw new BusinessException(ErrorCode.SUB404);
            }
            if (!"A".equals(subscriberInfo.status())){
                throw new BusinessException(ErrorCode.SUB002);
            }
            if (ledgerDao.debitRefNoExists(connection,refNo)){
                throw new BusinessException(ErrorCode.TXN002);
            }
            if (subscriberInfo.amountPiasters()<amountPiasters){
                throw new BusinessException(ErrorCode.BAL002);
            }

            int debit=balanceDao.debitIfEnough(connection,msisdn,amountPiasters);
            if (debit!=1){
                throw  new BusinessException(ErrorCode.BAL002);
            }
            long balanceAfter=subscriberInfo.amountPiasters()-amountPiasters;
            Ledger ledger=new Ledger(msisdn,"D",amountPiasters,balanceAfter,refNo,reason);
            long txnId=ledgerDao.insert(connection,ledger);
           return new DebitResult(txnId,balanceAfter);
        } catch (SQLException e) {
            throw new RuntimeException("Debit transaction failed", e);
        }

    }
}
