package org.example.apartmentmanagement.DAOLayer;

import org.example.apartmentmanagement.Repository.iApartment;
import org.example.apartmentmanagement.models.Apartment;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ApartmentDAO implements iApartment {

    public static void main(String[] args) {
        ApartmentDAO a = new ApartmentDAO();

        a.showAllApartment();
    }
// Method dùng để chuyển dữ liệu cua database sang List
    @Override
    public List<Apartment> getAllApartments() {
        List<Apartment> apartments = new ArrayList<>();
        String sql = "SELECT * FROM Apartment";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery();) {
            while (resultSet.next()){
                apartments.add(new Apartment(
                        resultSet.getString("apartment_id"),
                        resultSet.getInt("building_id"),
                        resultSet.getInt("floor"),
                        resultSet.getDouble("area"),
                        resultSet.getInt("bedrooms"),
                        resultSet.getDouble("price_apartment"),
                        resultSet.getString("status"),
                        resultSet.getDouble("maintenance_fee")
                        ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return apartments;

    }

    @Override
    public void showAllApartment() {
        List<Apartment> apartments = getAllApartments();
        if(!apartments.isEmpty()){
            for (Apartment apartment : apartments){
                System.out.println("Apartment ID: " + apartment.getApartmentID());
                System.out.println("Building ID: " + apartment.getBuildingID());
                System.out.println("Floors: " + apartment.getFloors());
                System.out.println("Area: " + apartment.getArea());
                System.out.println("Bedroom: " + apartment.getBedRoom());
                System.out.println("Price Apartment: " + apartment.getPriceApartment());
                System.out.println("Status: " + apartment.getStatus());
                System.out.println("Mainenance Fee: " + apartment.getMaintenanceFee());
                System.out.println("--------------------");
            }
        }
    }

    @Override
    public boolean addApartment(Apartment apartment) {
        String sql = "INSERT INTO Apartment(apartment_id, building_id, floor, area, bedrooms, price_apartment, status, maintenance_fee, created_at, updated_at) "+
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);){
            statement.setString(1,apartment.getApartmentID());
            statement.setInt(2, apartment.getBuildingID());
            statement.setInt(3, apartment.getFloors());
            statement.setDouble(4, apartment.getArea());
            statement.setInt(5, apartment.getBedRoom());
            statement.setDouble(6, apartment.getPriceApartment());
            statement.setString(7, apartment.getStatus());
            statement.setDouble(8, apartment.getMaintenanceFee());
            statement.setDate(9, Date.valueOf(LocalDate.now()));
            statement.setDate(10, Date.valueOf(LocalDate.now()));

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteApartmentById(String apartmentID) {
        return false;
    }
}
