package org.example.apartmentmanagement.DAOLayer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class Connector {
    private static Properties properties;
    private static Connection conn;

    public static void connectDB() throws SQLException, ClassNotFoundException, FileNotFoundException, IOException {
        properties = new Properties();
        properties.load(new FileInputStream("src/main/resources/database.properties"));

        String url = properties.getProperty("url");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");
        String driver = properties.getProperty("driver");

        Class.forName(driver);

        conn = DriverManager.getConnection(url, username, password);

        System.out.println("Thanh cong");
    }

    public void show() throws SQLException {
        Statement statement = conn.createStatement();
        String sql = "Select * from Staff";
    }
}
