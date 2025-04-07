package org.example.apartmentmanagement.Utils;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class passwordEncryption {
    public static String hashPassword (String password){
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }
    public static boolean checkPassword(String password, String hashPassWord){
        return BCrypt.verifyer().verify(password.toCharArray(), hashPassWord).verified;
    }

    public static void main(String[] args) {
        System.out.println(checkPassword("55555", hashPassword("55555")));
    }
}
