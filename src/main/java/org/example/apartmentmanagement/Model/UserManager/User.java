package org.example.apartmentmanagement.Model.UserManager;

public class User {
    private int userID;
    private String userName;
    private String passWord;
    private String fullName;
    private String email;
    private String phoneNumber;
    private int roleID;
    private boolean active;

    public User() {
        userName = passWord = fullName = email = phoneNumber = "";
        userID = roleID = 0;
        active = false;
    }

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

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    public boolean login (String userName, String passWord){

    }

    public void logout(){

    }

    public void changePassWord(String oldPassWord, String newPassWord){

    }
    public boolean hasPermission (String permissionName){

    }

    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", roleID=" + roleID +
                ", active=" + active +
                '}';
    }
}
