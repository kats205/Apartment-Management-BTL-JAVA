package com.utc2.apartmentmanagement.Utils;

import java.util.regex.Pattern;

public class TryCatchUtil {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    public static boolean validateEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
    public static boolean validatePassword(String password) {
        return  password.length() >= 6 && password.length() <= 12 && password.matches(".*[A-Z].*");
    }
    public static boolean validatePhone(String phone){
        return phone.matches("0\\d{9,10}");   //  10 hoặc 11 chữ số, bắt đầu bằng 0
    }
}
