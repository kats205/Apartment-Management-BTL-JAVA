package org.example.apartmentmanagement.DAOLayer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

@Getter
@Setter
@AllArgsConstructor

public class DatabaseConnection {
    private static Properties properties = new Properties();
    private static Connection connection;

    static {
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("database.properties")) {
            if (input == null) {
                throw new RuntimeException("Can't not find file database.properties");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error read file database.properties!!!", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        if(connection == null || connection.isClosed()){
            String url = properties.getProperty("url");
            String user = properties.getProperty("username");
            String password = properties.getProperty("password");

            if (url == null || user == null || password == null) {
                throw new RuntimeException("Error!! Wrong infomation in Properties file. ");
            }
            connection = DriverManager.getConnection(url,user,password);
            System.out.println("Connect Success!!");

        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Close connection successfully!!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try{
            getConnection();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }



}