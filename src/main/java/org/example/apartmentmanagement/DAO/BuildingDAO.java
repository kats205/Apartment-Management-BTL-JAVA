package org.example.apartmentmanagement.DAO;

import org.example.apartmentmanagement.Model.Apartment;
import org.example.apartmentmanagement.Model.Bills;
import org.example.apartmentmanagement.Model.Building;
import org.example.apartmentmanagement.Repository.IBuildingDAO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class BuildingDAO implements IBuildingDAO {
    @Override
    public List<Building> getAllBuilding(){
        List<Building> buildingList = new ArrayList<>();
        String sql = "SELECT * FROM Building";
        try{
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                buildingList.add(new Building(rs.getInt("building_id"), rs.getString("building_name"), rs.getNString("address"), rs.getInt("total_floors"),
                                rs.getInt("total_apartments"), rs.getDate("completion_date"), rs.getDate("created_at"), rs.getDate("updated_at")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return buildingList;
    }

    @Override
    public Building getBuildingByID(int buildingID) {
        String sql = "SELECT * FROM Building WHERE building_id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1,buildingID);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return new Building(rs.getInt("building_id"), rs.getString("building_name"), rs.getNString("address"), rs.getInt("total_floors"),
                        rs.getInt("total_apartments"), rs.getDate("completion_date"), rs.getDate("created_at"), rs.getDate("updated_at"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    @Override
    public boolean updateBuildingName(int buildingID, String newBuildingName) {
        return updateBuildingField(buildingID, "building_name", newBuildingName);
    }

    @Override
    public boolean updateAddress(int buildingID, String newAddress) {
        return updateBuildingField(buildingID, "address", newAddress);
    }

    @Override
    public boolean updateTotalFloor(int buildingID, int newToTalFloor) {
        return updateBuildingField(buildingID, "total_floor", newToTalFloor);
    }

    @Override
    public boolean updateTotalApartment(int buildingID, int newTotalApartment) {
        return updateBuildingField(buildingID, "total_apartment", newTotalApartment);
    }

    @Override
    public boolean updateCompletionDate(int buildingID, Date newCompletionDate) {
        return updateBuildingField(buildingID, "completion_date", newCompletionDate);
    }

    public boolean updateBuildingField(int buildingID, String field, Object newValue){
        String sql = "UPDATE Building SET " + field + " = ? , updated_at = ? WHERE building_id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)){
            if(newValue instanceof String){
                stmt.setNString(1, (String)newValue);
            }
            else if(newValue instanceof Integer){
                stmt.setInt(1, (Integer)newValue);
            }
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
            stmt.setInt(3, buildingID);
            return stmt.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteBuildingbyID(int buildingID) {
        String SQL = "DELETE FROM Building WHERE building_id = ?";
        try ( Connection connection = DatabaseConnection.getConnection();
              PreparedStatement stmt1 = connection.prepareStatement(SQL);
              Scanner sc = new Scanner(System.in)){
            connection.setAutoCommit(false);
            System.out.println("Do you want to delete building with ID " + buildingID + "? (Yes/No)");
            String check = sc.nextLine().trim();
            if (!check.equalsIgnoreCase("Yes")) {
                System.out.println("Deletion cancelled.");
                return false;
            }
            stmt1.setInt(1, buildingID);
            return stmt1.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return false;
    }
    @Override
    public boolean addBuilding(Building building) {
        String sql = "insert into Apartment(building_id, building_name, address, total_floors, total_apartments, completion_date,created_at, updated_at)" +
                "values ( ? , ? , ? , ? , ?, ? , ? , ? )";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, building.getBuildingID());
            stmt.setNString(2, building.getBuildingName());
            stmt.setNString(3, building.getAddress());
            stmt.setInt(4, building.getTotalFloors());
            stmt.setInt(5, building.getTotalApartment());
            stmt.setDate(6, building.getCompletionDate());
            stmt.setDate(7, Date.valueOf(LocalDate.now()));
            stmt.setDate(8,  Date.valueOf(LocalDate.now()));

            return stmt.executeUpdate() > 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }




}
