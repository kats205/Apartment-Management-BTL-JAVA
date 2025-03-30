package org.example.apartmentmanagement.DAO;

import org.example.apartmentmanagement.Model.Apartment;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ApartmentDAO {
    private static List<Apartment> apartmentList = new ArrayList<Apartment>();

    public static void getAllApartment(){
        apartmentList.clear();
        String sql = "SELECT * FROM Apartment";
        try{
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                apartmentList.add(new Apartment(rs.getString("apartment_id"), rs.getInt("building_id"), rs.getInt("floor"),
                        rs.getFloat("area"), rs.getInt("bedrooms"), rs.getDouble("price_apartment"), rs.getString("status"),  rs.getDouble("maintenance_fee")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void showAllApartment(){
        getAllApartment();
        if(apartmentList.isEmpty()) System.out.println("Error");
        System.out.println("==========================List Apartment==========================");
        for(Apartment apartment : apartmentList){
            System.out.println("-------------------------------------------");
            System.out.println("Apartment ID: " + apartment.getApartmentID());
            System.out.println("Building ID: " +apartment.getBuildingID());
            System.out.println("Floors: " + apartment.getFloors());
            System.out.println("Area: " + apartment.getArea());
            System.out.println("Bedroom: " + apartment.getBedRoom());
            System.out.println("Price Apartment: " + apartment.getPriceApartment());
            System.out.println("Status: " + apartment.getStatus());
            System.out.println("maintenance fee: " + apartment.getMaintanceFee());
            System.out.println("-------------------------------------------");

        }
    }

    public static void addApartment(String apartmentID, int buildingID, int floor,
                                    double area, int bedrooms, double priceApartment, String status, double maintenance_fee) throws SQLException {
        String sql = "INSERT INTO Apartment(apartment_id, building_id, floor, area, bedrooms, price_apartment, status, maintenance_fee, created_at, updated_at)" +
                "VALUES ( ? , ? , ? , ? , ?, ? , ? , ? , ? , ?)";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, apartmentID);
            stmt.setInt(2, buildingID);
            stmt.setInt(3, floor);
            stmt.setDouble(4, area);
            stmt.setInt(5, bedrooms);
            stmt.setDouble(6, priceApartment);
            stmt.setString(7, status);
            stmt.setDouble(8, maintenance_fee);
            stmt.setDate(9, Date.valueOf(LocalDate.now()));
            stmt.setDate(10, Date.valueOf(LocalDate.now()));

            int excuted = stmt.executeUpdate();
            if(excuted > 0) System.out.println("Added Apartment Successfully!");
            else System.out.println("Error");
        }catch(SQLException e){
            e.printStackTrace();
        }
        apartmentList.add(new Apartment(apartmentID,buildingID,floor,area,bedrooms,priceApartment,status,maintenance_fee));
    }

    // hàm này dùng để cập nhật một resident thông qua id, khi chưa biết trước nên cập nhật thông tin gì
    // -> dùng Object để kiểm tra đối tượng cần update
    public static Apartment findApartmentById(String apartmentID){
        if(apartmentList.isEmpty()) getAllApartment();
        for(Apartment apartment : apartmentList){
            if(apartment.getApartmentID().equalsIgnoreCase(apartmentID)) return apartment;
        }
        return null;
    }
    public static void updateApartment(String apartmentID, String field, Object newValue) throws SQLException {
        // các column có sẵn trong Resident để tránh sql injection
        List<String> allowColumn = Arrays.asList("apartment_id", "building_id", "floor","area", "bedrooms", "price_apartment", "status", "maintenance_fee");
        if(!allowColumn.contains(field.toLowerCase())){
            System.out.println("Field need update isn't invalid!");
            return;
        }
        String sql = "UPDATE Apartment SET " + field + " = ? WHERE apartment_id = ?";
        String updated_atSQL = "UPDATE Apartment SET updated_at = getdate() WHERE apartment_id = ?";
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
            else{
                System.out.println("Field is invalid!");
            }
            stmt.setString(2, apartmentID);
            int excuted = stmt.executeUpdate();
            if(excuted > 0) {
                stmt1.setString(1, apartmentID);
                stmt1.executeUpdate(); // ghi nhận thời gian update thông tin
                System.out.println("Update successfully!");
                connection.commit();
            }
            else System.out.println("Update fail!");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    // xóa resident bằng ID
    public static void deleteApartmentById(String apartmentID) throws SQLException {
        String deleteApartmentSQL = "DELETE FROM Apartment WHERE apartment_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt1 = connection.prepareStatement(deleteApartmentSQL);
             Scanner sc = new Scanner(System.in)) {
            connection.setAutoCommit(false);
            System.out.println("Do you want to delete resident with ID " + apartmentID + "? (Yes/No)");
            String check = sc.nextLine().trim();
            if (!check.equalsIgnoreCase("Yes")) {
                System.out.println("Deletion cancelled.");
                return;
            }
            stmt1.setString(1, apartmentID);
            int executed = stmt1.executeUpdate();
            if (executed > 0) {
                connection.commit(); // Chỉ commit nếu không có lỗi
                System.out.println("Delete A Resident Successfully!");
            } else {
                connection.rollback(); // Rollback nếu không có dòng nào bị xóa
                System.out.println("Resident Delete Failed!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (!DatabaseConnection.getConnection().getAutoCommit()) {
                    DatabaseConnection.getConnection().rollback(); // Rollback nếu có lỗi
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        }
    }
    public static void main(String[] args) throws SQLException {

//        ApartmentDAO.updateApartment("A101", "status", "available");
    }
}
