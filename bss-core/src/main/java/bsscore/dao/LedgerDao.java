package bsscore.dao;

import bsscore.model.Ledger;
import jakarta.enterprise.context.ApplicationScoped;

import java.sql.*;
import java.time.LocalDateTime;
@ApplicationScoped
public class LedgerDao {

    public boolean debitRefNoExists(Connection connection,String refNo)throws SQLException{
        String checkRefNo="select exists(select 1 from LEDGER where REF_NO=? and TXN_TYPE='D')";

        try(PreparedStatement checkStat=connection.prepareStatement(checkRefNo)){
            checkStat.setString(1,refNo);

            try(ResultSet result=checkStat.executeQuery()){
                result.next();
                return result.getBoolean(1);
            }
        }
    }

    public long insert(Connection connection, Ledger ledger) throws SQLException {
        String insertLedger="insert into LEDGER " +
                            "(MSISDN,TXN_TS,TXN_TYPE,AMOUNT_PIASTERS" +
                            ",BALANCE_AFTER,REF_NO,REASON) " +
                            "values(?,?,?,?,?,?,?)";
        try(PreparedStatement insertStat=connection.prepareStatement(insertLedger,Statement.RETURN_GENERATED_KEYS)){
            insertStat.setString(1,ledger.msisdn());
            insertStat.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            insertStat.setString(3,ledger.txnType());
            insertStat.setLong(4,ledger.amountPiasters());
            insertStat.setLong(5,ledger.balanceAfter());
            insertStat.setString(6,ledger.refNo());
            insertStat.setString(7,ledger.reason());

            int insertedRow=insertStat.executeUpdate();

            if(insertedRow!=1){
                throw new SQLDataException("Ledger did not inserted");
            }
            try (ResultSet keys = insertStat.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getLong(1);
                }
                throw new SQLException("Ledger insert did not return TXN_ID");
            }
        }
    }
}
