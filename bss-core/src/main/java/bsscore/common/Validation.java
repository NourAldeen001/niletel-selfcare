package bsscore.common;

import java.util.regex.Pattern;

public final class Validation {

    public static final Pattern MSISDN = Pattern.compile("^01[0125][0-9]{8}$");
    public static final Pattern PIN = Pattern.compile("^[0-9]{4}$");

    private Validation() {}

    public static String requireMatch(String value, Pattern pattern) {
        if(value == null || !pattern.matcher(value).matches()) {
            throw new BusinessException(ErrorCode.VAL001);
        }
        return value;
    }
}
