package com.utc2.apartmentmanagement.Model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class User {
    private int userID;
    private String userName;
    private String passWord;
    private String fullName;
    private String email;
    private String phoneNumber;
    private int roleID;
    private boolean active;

    public User(int userID, String userName, String passWord, String fullName, String email, String phoneNumber, int roleID, boolean active) {
        this.userID = userID;
        this.userName = userName;
        this.passWord = passWord;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.roleID = roleID;
        this.active = active;
    }
    public User(String userName, String passWord, String fullName, String email, String phoneNumber, int roleID, boolean active){
        this.userName = userName;
        this.passWord = passWord;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.roleID = roleID;
        this.active = active;
    }

}
