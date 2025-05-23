package com.utc2.apartmentmanagement.Utils;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class passwordEncryption {
    public static String hashPassword (String password){
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }
    public static boolean checkPassword(String password, String hashPassWord){
        return BCrypt.verifyer().verify(password.toCharArray(), hashPassWord).verified;
    }

}
