package com.utc2.apartmentmanagement.Repository;

import com.utc2.apartmentmanagement.Model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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
    int getIdByUserName(String userName) throws SQLException;
    List<Map<String, Object>> searchOnChange(String searchText) throws SQLException;
    boolean updateAvatar(int user_id, String filePath) throws SQLException;
    String getAvatarPathByUserId(String userName) throws SQLException;
}
