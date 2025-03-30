package org.example.apartmentmanagement.DAO;

import org.example.apartmentmanagement.Model.User;
import org.example.apartmentmanagement.Utils.passwordEncryption;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class UserDAO {
    private static List<User> userList = new ArrayList<User>();
    public void getAllUser() throws SQLException {
        userList.clear();
        String sql = "SELECT user_id, username, password, full_name, email,phone_number, role_id, active from [User]";
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
//        return userList;
    }

    public static void showAllUser() throws SQLException {
        if(userList.isEmpty()) new UserDAO().getAllUser();
        for(User user : userList){
            System.out.println("User ID: " + user.getUserID());
            System.out.println("User Name: " + user.getUserName());
            System.out.println("pass word: " + user.getPassWord());
            System.out.println("Full Name: " + user.getPassWord());
            System.out.println("Email: " + user.getEmail());
            System.out.println("Phone Number: " + user.getPhoneNumber());
            System.out.println("Role ID: " + user.getRoleID());
            System.out.println("Active: " + user.isActive());
        }
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

    public static void addUser(String userName, String passWord, String fullName, String email, String phoneNumber, int roleID, boolean active) throws SQLException {
        String sql = "INSERT INTO [User](username, password, full_name, email, phone_number, role_id, active, created_at, updated_at) VALUES ( ? , ? , ? , ? , ? , ? , ? , getdate() , getdate())";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            conn.setAutoCommit(false);
            stmt.setString(1, userName);
            stmt.setString(2, passwordEncryption.hashPassword(passWord));
            stmt.setNString(3, fullName);
            stmt.setString(4, email);
            stmt.setString(5, phoneNumber);
            stmt.setInt(6, roleID);
            stmt.setBoolean(7, active);
            int excuted = stmt.executeUpdate();
            if(excuted > 0) {
                conn.commit();
                System.out.println("Added a User Successfully!");
            }
            else{
                conn.rollback();
                System.out.println("Added a User Fail!");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public static void DeleteStaffbyId (int userID) throws SQLException{
        String sql = "DELETE FROM [User] WHERE user_id = ?";
        try{
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userID);
            int executed = stmt.executeUpdate();
            if(executed > 0) System.out.println("Delete Successfully!");
            else System.out.println("Delete Fail!");
        }catch(SQLException e){
            throw new RuntimeException();
        }
    }

    public static void UpdateUserById(int userID, String field, Object newValue){
        List<String> allowColumn = Arrays.asList("user_id", "username", "password","full_name", "email", "phone_number", "role_id", "active");
        if(!allowColumn.contains(field.toLowerCase())){
            System.out.println("Field need update isn't invalid!");
            return;
        }
        String sql = "UPDATE [User] SET " + field + " = ? WHERE user_id = ?";
        String updated_atSQL = "UPDATE [User] SET updated_at = getdate() WHERE user_id = ?";
        try{
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            PreparedStatement stmt1 = connection.prepareStatement(updated_atSQL);
            connection.setAutoCommit(false);
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
            stmt.setInt(2, userID);
            int excuted = stmt.executeUpdate();
            if(excuted > 0) {
                stmt1.setInt(1, userID);
                stmt1.executeUpdate();
                System.out.println("Update successfully!");
                connection.commit();
            }
            else System.out.println("Update fail!");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

//    public boolean logout(){
//
//    }
}
