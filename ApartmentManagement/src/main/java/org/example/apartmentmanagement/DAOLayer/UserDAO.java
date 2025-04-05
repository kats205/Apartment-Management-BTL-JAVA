package org.example.apartmentmanagement.DAOLayer;

import org.example.apartmentmanagement.Repository.iUser;
import org.example.apartmentmanagement.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements iUser {

    @Override
    public List<User> getAllUser() {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT * from [User]";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();){
            while(rs.next()){
                userList.add(new User(rs.getInt("user_id"), rs.getString( "username"),rs.getString( "password"), rs.getNString( "full_name"),
                        rs.getString( "email"), rs.getString( "phone_number"), rs.getInt( "role_id"), rs.getBoolean( "active")));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return userList;    }

    @Override
    public User getUserByID(int userID) {
        return null;
    }

    @Override
    public boolean addUser(User user) {
        return false;
    }

    @Override
    public boolean deleteUserByID(int userID) {
        return false;
    }

    @Override
    public boolean updateUserName(int userID, String newUserName) {
        return false;
    }

    @Override
    public boolean updatePassWord(int userID, String newPassword) {
        return false;
    }

    @Override
    public boolean updateFullName(int userID, String newFullName) {
        return false;
    }

    @Override
    public boolean updateEmail(int userID, String newEmail) {
        return false;
    }

    @Override
    public boolean updatePhoneNumber(int userID, String newPhoneNumber) {
        return false;
    }

    @Override
    public boolean updateRoleID(int userID, int newRoleID) {
        return false;
    }

    @Override
    public boolean updateActive(int userID, boolean newActive) {
        return false;
    }
}
