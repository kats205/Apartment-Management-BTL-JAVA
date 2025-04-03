package org.example.apartmentmanagement.DAO;

import org.example.apartmentmanagement.Model.Apartment;
import org.example.apartmentmanagement.Repository.IApartmentDAO;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class ApartmentDAO implements IApartmentDAO {
    @Override
    public List<Apartment> getAllApartments() {
        List<Apartment> apartments = new ArrayList<>();
        String sql = "SELECT * FROM Apartment";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                apartments.add(new Apartment(
                        rs.getString("apartment_id"),
                        rs.getInt("building_id"),
                        rs.getInt("floor"),
                        rs.getFloat("area"),
                        rs.getInt("bedrooms"),
                        rs.getDouble("price_apartment"),
                        rs.getString("status"),
                        rs.getDouble("maintenance_fee")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return apartments;
    }
    public static void showAllApartment(){
        List<Apartment> apartmentList = new ApartmentDAO().getAllApartments();
        if(!apartmentList.isEmpty()){
            for(Apartment apartment : apartmentList){
                System.out.println("Aparment ID: " + apartment.getApartmentID());
                System.out.println("Building ID: " + apartment.getBuildingID());
                System.out.println("Floors: " + apartment.getFloors());
                System.out.println("Area: " + apartment.getArea());
                System.out.println("Bedroom: " + apartment.getBedRoom());
                System.out.println("Price Apartment: " + apartment.getPriceApartment());
                System.out.println("Status: " + apartment.getStatus());
                System.out.println("Mainenance Fee: " + apartment.getMaintanceFee());
            }
        }
    }
    @Override
    public Apartment findApartmentById(String apartmentID) {
        String sql = "SELECT * FROM Apartment WHERE apartment_id = ?";
        // sử dụng try-with-resource để sau khi thực hiện xong truy vấn thì tự động ngắt kết nối với DB
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, apartmentID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Apartment(
                        rs.getString("apartment_id"),
                        rs.getInt("building_id"),
                        rs.getInt("floor"),
                        rs.getFloat("area"),
                        rs.getInt("bedrooms"),
                        rs.getDouble("price_apartment"),
                        rs.getString("status"),
                        rs.getDouble("maintenance_fee")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean addApartment(Apartment apartment) {
        String sql = "INSERT INTO Apartment(apartment_id, building_id, floor, area, bedrooms, price_apartment, status, maintenance_fee, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, apartment.getApartmentID());
            stmt.setInt(2, apartment.getBuildingID());
            stmt.setInt(3, apartment.getFloors());
            stmt.setDouble(4, apartment.getArea());
            stmt.setInt(5, apartment.getBedRoom());
            stmt.setDouble(6, apartment.getPriceApartment());
            stmt.setString(7, apartment.getStatus());
            stmt.setDouble(8, apartment.getMaintanceFee());
            stmt.setDate(9, Date.valueOf(LocalDate.now()));
            stmt.setDate(10, Date.valueOf(LocalDate.now()));

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateApartmentStatus(String apartmentID, String newStatus) {
        return updateApartmentField(apartmentID, "status", newStatus);
    }

    @Override
    public boolean updateApartmentPrice(String apartmentID, double newPrice) {
        return updateApartmentField(apartmentID, "price_apartment", newPrice);
    }

    private boolean updateApartmentField(String apartmentID, String field, Object newValue) {
        String sql = "UPDATE Apartment SET " + field + " = ?, updated_at = ? WHERE apartment_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (newValue instanceof String) {
                stmt.setString(1, (String) newValue);
            } else if (newValue instanceof Double) {
                stmt.setDouble(1, (Double) newValue);
            }
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
            stmt.setString(3, apartmentID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteApartmentById(String apartmentID) {
        String sql = "DELETE FROM Apartment WHERE apartment_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, apartmentID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
