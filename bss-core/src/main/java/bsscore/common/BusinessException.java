package bsscore.common;

import jakarta.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class BusinessException extends RuntimeException {

    private final bsscore.common.ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
