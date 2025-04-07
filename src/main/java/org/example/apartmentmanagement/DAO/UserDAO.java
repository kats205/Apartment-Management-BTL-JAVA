package org.example.apartmentmanagement.DAO;

import lombok.Getter;
import org.example.apartmentmanagement.Model.User;
import org.example.apartmentmanagement.Repository.IUserDAO;
import org.example.apartmentmanagement.Utils.AlertBox;
import org.example.apartmentmanagement.Utils.passwordEncryption;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class UserDAO implements IUserDAO {
    public static List<String> getAllValuesofColumn(String columnName) {
        List<String> resultList = new ArrayList<>();
        String sql = "SELECT " + columnName + " FROM [User]";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection != null ? connection.prepareStatement(sql) : null;
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                resultList.add(rs.getString(columnName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            AlertBox.showAlertForExeptionRegister("Cảnh báo!", "Lỗi kết nối database!");
        }

        return resultList;
    }
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
        return userList;
    }

    @Override
    public User getUserByID(int userID) {
        String sql = "SELECT * from [User] WHERE user_id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);){
            stmt.setInt(1,userID);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return new User(rs.getInt("user_id"), rs.getString( "username"),rs.getString( "password"), rs.getNString( "full_name"),
                        rs.getString( "email"), rs.getString( "phone_number"), rs.getInt( "role_id"), rs.getBoolean( "active"));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean addUser(User user) {
        String sql = "INSERT INTO [User](username, password, full_name, email, phone_number, role_id, active, created_at, updated_at) VALUES ( ? , ? , ? , ? , ? , ? , ? , getdate() , getdate())";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            conn.setAutoCommit(false);
            stmt.setString(1, user.getUserName());
            stmt.setString(2, passwordEncryption.hashPassword(user.getPassWord()));
            stmt.setNString(3, user.getFullName());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPhoneNumber());
            stmt.setInt(6, user.getRoleID());
            stmt.setBoolean(7, user.isActive());
            return stmt.executeUpdate() > 0;

        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteUserByID(int userID) {
        String sql = "DELETE FROM [User] WHERE user_id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);){
            stmt.setInt(1, userID);
            return stmt.executeUpdate() > 0;
        }catch(SQLException e){
            throw new RuntimeException();
        }
    }

    @Override
    public boolean updateUserName(int userID, String newUserName) {
        return updateStaffField(userID, "username", newUserName);
    }

    @Override
    public boolean updatePassWord(int userID, String newPassword) {
        return updateStaffField(userID, "password", newPassword);
    }

    @Override
    public boolean updateFullName(int userID, String newFullName) {
        return updateStaffField(userID, "fullname", newFullName);
    }

    @Override
    public boolean updateEmail(int userID, String newEmail) {
        return updateStaffField(userID, "email", newEmail);
    }

    @Override
    public boolean updatePhoneNumber(int userID, String newPhoneNumber) {
        return updateStaffField(userID, "phone_number", newPhoneNumber);
    }

    @Override
    public boolean updateRoleID(int userID, int newRoleID) {
        return updateStaffField(userID, "role_id", newRoleID);
    }

    @Override
    public boolean updateActive(int userID, boolean newActive) {
        return updateStaffField(userID, "active", newActive);
    }
    public int login (String userName, String passWord){
        String sql = "SELECT * FROM [User] WHERE username = ?";
        int roleID = 0;
        if (userName == null || userName.isEmpty() || passWord == null || passWord.isEmpty()) {
            return 0; // Tránh truy vấn SQL nếu giá trị rỗng
        }
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, userName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                System.out.println("Stored Hash: " + hashedPassword);
                System.out.println("Input Password: " + passWord);

                boolean isMatch = passwordEncryption.checkPassword(passWord, hashedPassword);
                System.out.println("Password Match: " + isMatch);

                if (isMatch) {
                    roleID = rs.getInt("role_id");
                    System.out.println("User role: " + roleID);
                } else {
                    System.out.println("Password does not match!");
                }
            } else {
                System.out.println("No user found with this username.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roleID;
    }

    public static boolean updateStaffField(int userID, String field, Object newValue){
        String sql = "UPDATE [User] SET " + field + " = ? , updated_at = ? WHERE user_id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);){
            if(newValue instanceof String){
                stmt.setString(1, (String)newValue);
            }
            else if(newValue instanceof Integer){
                stmt.setInt(1, (Integer) newValue);
            }
            else if(newValue instanceof Double){
                stmt.setDouble(1, (Double) newValue);
            }
            else if(newValue instanceof Boolean){
                stmt.setInt(1, Boolean.compare((Boolean)newValue, false));
            }
            else{
                System.out.println("Field is invalid!");
            }
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
            stmt.setInt(3, userID);
            return stmt.executeUpdate() > 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }


//    public boolean logout(){
//
//    }
}
