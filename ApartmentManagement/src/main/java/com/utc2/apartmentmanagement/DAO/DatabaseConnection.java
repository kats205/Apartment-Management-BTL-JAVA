package com.utc2.apartmentmanagement.DAO;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static Properties properties = new Properties();
    private static Connection connection;

    static {
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("database.properties")) {
            if (input == null) {
                throw new RuntimeException("Không tìm thấy file cấu hình database.properties");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi đọc file cấu hình database.properties", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            String url = properties.getProperty("url");
            String user = properties.getProperty("username");
            String password = properties.getProperty("password");

            if (url == null || user == null || password == null) {
                throw new RuntimeException("Thiếu thông tin database trong file cấu hình.");
            }

            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Kết nối thành công!");
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Đóng kết nối thành công!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
