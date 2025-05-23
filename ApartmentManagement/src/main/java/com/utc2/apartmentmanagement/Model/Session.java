package com.utc2.apartmentmanagement.Model;

// phần này dùng để lưu thông tin người dùng khi đăng nhập vào hệ thống

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Session {
    @Getter
    @Setter
    private static  String userName;
    @Getter
    @Setter
    private static String lastLogin;

    @Setter
    @Getter
    private static String avatarPath;
    @Setter
    @Getter
    private static String roleName;
    @Setter
    @Getter
    private static String passWord;
    public Session() {}

    public Session(String userName, String lastLogin) {
        Session.userName = userName;
        Session.lastLogin = lastLogin;
    }

}
