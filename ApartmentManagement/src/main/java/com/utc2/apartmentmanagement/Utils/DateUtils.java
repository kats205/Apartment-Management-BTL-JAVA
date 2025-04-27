package com.utc2.apartmentmanagement.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String format(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static String getCurrentTimestamp() {
        return format(new Date(), DEFAULT_FORMAT);
    }
}
