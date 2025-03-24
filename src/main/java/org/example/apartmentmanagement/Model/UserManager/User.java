package org.example.apartmentmanagement.Model.UserManager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.apartmentmanagement.DAO.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int userID;
    private String userName;
    private String passWord;
    private String fullName;
    private String email;
    private String phoneNumber;
    private int roleID;
    private boolean active;


    public int login (String userName, String passWord){
        String sql = " select role_id from [User] where username = ? and password = ?";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){

            stmt.setString(1, userName);
            stmt.setString(2, passWord);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                roleID = rs.getInt("role_id");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return roleID;
    }
//
//    public void logout(){
//
//    }
//
//    public void changePassWord(String oldPassWord, String newPassWord){
//
//    }
//    public boolean hasPermission (String permissionName){
//
//    }


}
