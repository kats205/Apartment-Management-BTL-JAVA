package org.example.apartmentmanagement.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String url="jdbc:sqlserver://localhost:1433;databaseName=QLCC;encrypt=true;trustServerCertificate=true;";
    private static final String  user="sa"; // Dùng tài khoản SQL Server, không phải Windows
    private static final String pass="YourSecurePassword";// Mật khẩu của 'sa'

    public static Connection getConnection(){
        try{
            return DriverManager.getConnection(url, user, pass);
        }catch(SQLException e){
            System.out.println("Lỗi kết nối database");
            e.printStackTrace();
            return null;
        }
    }
}
