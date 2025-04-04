package org.example.apartmentmanagement.DAOLayer;

import org.example.apartmentmanagement.Repository.iBuilding;
import org.example.apartmentmanagement.models.Building;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BuildingDAO implements iBuilding {
    public static void main(String[] args) {
        BuildingDAO b = new BuildingDAO();
        b.showAllBuilding();
    }
    @Override
    public List<Building> getAllBuilding() {
        List<Building> buildings = new ArrayList<>();
        String sql = "SELECT * FROM Building";
        try{
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                buildings.add(new Building(
                        resultSet.getInt("building_id"),
                        resultSet.getString("building_name"),
                        resultSet.getNString("address"),
                        resultSet.getInt("total_floors"),
                        resultSet.getInt("total_apartments"),
                        resultSet.getDate("completion_date"),
                        resultSet.getDate("created_at"),
                        resultSet.getDate("updated_at")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return  buildings;
    }

    @Override
    public void showAllBuilding() {
        List<Building> buildings = getAllBuilding();
        if(!buildings.isEmpty()){
            for (Building building : buildings){
                System.out.println("Building ID: " + building.getBuildingID());
                System.out.println("Building Name: "+ building.getBuildingName());
                System.out.println("Address: " + building.getAddress());
                System.out.println("Total Floors: " + building.getTotalFloors());
                System.out.println("Total Apartment: " + building.getTotalApartment());
                System.out.println("Completion Date: " + building.getCompletionDate());
                System.out.println("Created At: " + building.getCreatedAt());
                System.out.println("Updadted At: " + building.getUpdatedAt());
                System.out.println("----------------------");

            }
        }
    }

    @Override
    public boolean addBuilding(Building building) {
        return false;
    }

    @Override
    public boolean deleteBuildingbyID(int buildingID) {
        return false;
    }
}
