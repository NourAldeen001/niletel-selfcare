package bsscore.model;

public record Ledger(String msisdn,String txnType,long amountPiasters
        ,long balanceAfter,String refNo,String reason) {
}
