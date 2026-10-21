package bsscore.common;

public enum ErrorCode {

    SES001(401, "no valid session"),
    SYS999(500, "unexpected error"),
    SEC001(200, "role not allowed"),
    VAL001(200, "parameter missing or bad format"),
    AUTH001(200, "wrong MSISDN or PIN"),
    AUTH002(200, "line suspended"),
    AUTH003(200, "wrong operator login"),
    VCH001(200, "unknown voucher"),
    VCH002(200, "voucher already used"),
    BAL001(200, "invalid amount"),
    BAL002(200, "insufficient balance"),
    SUB002(200, "line suspended"),
    TXN002(200, "reference number already used"),
    TXN003(200, "already refunded"),
    TXN404(200, "unknown reference number"),
    SUB404(200, "subscriber not found");

    private final int httpStatus;
    private final String message;

    ErrorCode(int httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }

}
