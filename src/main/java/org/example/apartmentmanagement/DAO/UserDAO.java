package org.example.apartmentmanagement.DAO;

import org.example.apartmentmanagement.Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public void getAllUser() throws SQLException {
        String sql = "select * from [User]";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();){
            while(rs.next()){
                System.out.println("ID" + rs.getInt("user_id"));
                System.out.println("User Name: " + rs.getString("username"));
                System.out.println("PassWord: " + rs.getString("password"));
                System.out.println("Full Name: " + rs.getString("full_name"));
                System.out.println("Email: " + rs.getString("email"));
                System.out.println("Phone Number: " + rs.getString("phone_number"));
                System.out.println("Role ID: " + rs.getString("role_id"));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

    }

//    public boolean loginAccess(String user){
//
//    }


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

//    public boolean logout(){
//
//    }
}
