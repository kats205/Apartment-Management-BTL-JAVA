package org.example.apartmentmanagement.DAO;

import org.example.apartmentmanagement.Model.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class UserDAO {
    private static List<User> userList = new ArrayList<User>();
    public void getAllUser() throws SQLException {
        userList.clear();
        String sql = "select user_id, username, password, full_name, email,phone_number, role_id, active from [User]";
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
        String sql = " select role_id from [User] where username = ? and password = ?";
        int roleID = 0;
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){

            stmt.setString(1, userName);
            stmt.setString(2, passWord);
            ResultSet rs = stmt.executeQuery();

            User user = new User();
            if(rs.next()){
                roleID = rs.getInt("role_id");
                user.setRoleID(roleID);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return roleID;
    }

    public static void addUser(String userName, String passWord, String fullName, String email, String phoneNumber, int roleID, boolean active, Date created_at, Date updated_at) throws SQLException {
        String sql = "INSERT INTO [User](username, password, full_name, email, phone_number, role_id, active, created_at, updated_at) VALUES ( ? , ? , ? , ? , ? , ? , ? , ? , ?)";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            conn.setAutoCommit(false);
            stmt.setString(1, userName);
            stmt.setString(2, passWord);
            stmt.setNString(3, fullName);
            stmt.setString(4, email);
            stmt.setString(5, phoneNumber);
            stmt.setInt(6, roleID);
            stmt.setInt(7, Boolean.compare(active, false));
            stmt.setDate(8, Date.valueOf(LocalDate.now()));
            stmt.setDate(9, Date.valueOf(LocalDate.now()));
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

//    public boolean logout(){
//
//    }
}
