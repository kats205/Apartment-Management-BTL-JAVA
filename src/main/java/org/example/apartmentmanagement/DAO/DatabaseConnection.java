package org.example.apartmentmanagement.DAO;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static Properties properties;
    private static Connection connection;

    public static Connection getConnection(){
        try{
            properties = new Properties();
            properties.load(new FileInputStream("src/main/resources/Database.properties"));
            String url = properties.getProperty("url");
            String user = properties.getProperty("username");
            String pass = properties.getProperty("password");
            return DriverManager.getConnection(url, user, pass);
        }catch(SQLException e){
            System.out.println("Lỗi kết nối database");
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
