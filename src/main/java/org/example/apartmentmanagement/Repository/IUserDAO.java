package org.example.apartmentmanagement.Repository;

import org.example.apartmentmanagement.Model.User;

import java.util.List;

public interface IUserDAO {
    List<User> getAllUser();
    User getUserByID(int userID);
    boolean addUser(User user);
    boolean deleteUserByID(int userID);
    boolean updateUserName(int userID, String newUserName);
    boolean updatePassWord(int userID, String newPassword);
    boolean updateFullName(int userID, String newFullName);
    boolean updateEmail(int userID, String newEmail);
    boolean updatePhoneNumber(int userID, String newPhoneNumber);
    boolean updateRoleID(int userID, int newRoleID);
    boolean updateActive(int userID, boolean newActive);
}
