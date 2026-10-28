package bsscore.common;

import java.util.regex.Pattern;

public final class Validation {

    public static final Pattern MSISDN = Pattern.compile("^01[0125][0-9]{8}$");
    public static final Pattern PIN = Pattern.compile("^[0-9]{4}$");
    public static final Pattern REF_NO = Pattern.compile("^[A-Z0-9-]{6,20}$");

    private Validation() {}

    public static String requireMatch(String value, Pattern pattern) {
        if(value == null || !pattern.matcher(value).matches()) {
            throw new BusinessException(ErrorCode.VAL001);
        }
        return value;
    }

    public static String requireNotBlank(String value) {
        if (value == null || value.isBlank()) {
            throw new BusinessException(ErrorCode.VAL001);
        }
        return value;
    }

    public static long validateAmount(String amountPiasters){
        requireNotBlank(amountPiasters);

        if (!amountPiasters.matches("[0-9]+")){
            throw new BusinessException(ErrorCode.BAL001);
        }

        try{
            long amount=Long.parseLong(amountPiasters);
            if(amount<1||amount>10_000_000){
                throw new BusinessException(ErrorCode.BAL001);
            }
            return amount;
        }catch (NumberFormatException exception) {
            throw new BusinessException(ErrorCode.BAL001);
        }
    }
}
