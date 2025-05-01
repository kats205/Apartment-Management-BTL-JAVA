package com.utc2.apartmentmanagement.Model;

// phần này dùng để lưu thông tin người dùng khi đăng nhập vào hệ thống

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class Session {
    @Getter
    @Setter
    private static  String userName;
    @Getter
    @Setter
    private static  LocalDate lastLogin;

    public Session() {}

    public Session(String userName, LocalDate lastLogin) {
        Session.userName = userName;
        Session.lastLogin = lastLogin;
    }


}
