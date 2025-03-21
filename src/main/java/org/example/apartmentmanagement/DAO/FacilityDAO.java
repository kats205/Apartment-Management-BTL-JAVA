package org.example.apartmentmanagement.DAO;

import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

public class FacilityDAO <T>{
    public Scanner sc = new Scanner(System.in);
    public void getAllFacilily() throws SQLException {
        String sql = "select * from FacilityBooking";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){

            while(rs.next()){
                System.out.println("ID: " + rs.getInt("ID"));
                System.out.println("Facility Name: " + rs.getNString("facilityName"));
                System.out.println("Start Time: " + rs.getString("startTime"));
                System.out.println("End Time: " + rs.getString("endTime"));
                System.out.println("Status: " + rs.getBoolean("status_"));

            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public void createFacility (String ID, String facilityName, String startTime, String endTime, boolean status) throws SQLException {
        String sql = "insert into facilityBooking (ID, facilityName, startTime, endTime, status_) values ( ? , ? , ? , ? , ? )";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, ID);
            stmt.setNString(2, facilityName);  // Hỗ trợ tiếng Việt
            stmt.setString(3, startTime);
            stmt.setString(4, endTime);
            stmt.setBoolean(5, status);
            int rowsInserted = stmt.executeUpdate();
            if(rowsInserted > 0){
                System.out.println("Thêm dữ liệu thành công!");
            }
        }catch(SQLException e){
            System.out.println("Đã xảy ra lỗi!");
            e.printStackTrace();
        }
    }

    public void deleteFacilityByID(String ID) throws SQLException{
        String sql = "Delete from FacilityBooking where TRIM(ID) =  ? ";

        try(Connection conn =  DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, ID.trim());
            int rowsDeleted = stmt.executeUpdate();
            if(rowsDeleted > 0){
                System.out.println("Xóa facility có ID = " + ID + " thành công!");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
