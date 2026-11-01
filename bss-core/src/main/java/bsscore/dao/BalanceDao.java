package bsscore.dao;
import bsscore.model.SubscriberBalanceInfo;
import jakarta.enterprise.context.ApplicationScoped;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
@ApplicationScoped
public class BalanceDao {

    public SubscriberBalanceInfo findBalanceInfo(Connection connection, String msisdn)throws SQLException{
        String balanceQuery="select STATUS,BALANCE_PIASTERS from SUBSCRIBER where MSISDN=? for update";

        try(PreparedStatement balanceStat=connection.prepareStatement(balanceQuery)){
            balanceStat.setString(1,msisdn);
            try(ResultSet balanceSet=balanceStat.executeQuery()){
                if(balanceSet.next()){
                    return new SubscriberBalanceInfo
                            (balanceSet.getString("STATUS"),
                                    balanceSet.getLong("BALANCE_PIASTERS"));
                }
                    return null;
            }
        }
    }

    public int debitIfEnough(Connection connection, String msisdn, long amountPiasters) throws SQLException {
        String debitSql="update SUBSCRIBER set BALANCE_PIASTERS = BALANCE_PIASTERS-? " +
                        "where MSISDN=? and STATUS='A' and BALANCE_PIASTERS>=?";

        try (PreparedStatement updateStatment=connection.prepareStatement(debitSql)){
            updateStatment.setLong(1,amountPiasters);
            updateStatment.setString(2,msisdn);
            updateStatment.setLong(3,amountPiasters);
            return updateStatment.executeUpdate();
        }
    }

    public int credit(Connection connection,String msisdn,long persist)throws SQLException{
        String creditSql="update SUBSCRIBER " +
                         "set BALANCE_PIASTERS=BALANCE_PIASTERS+?" +
                         " where MSISDN=? ";

        try (PreparedStatement updateStat=connection.prepareStatement(creditSql)){
            updateStat.setLong(1,persist);
            updateStat.setString(2,msisdn);
            return updateStat.executeUpdate();
        }
    }
}
