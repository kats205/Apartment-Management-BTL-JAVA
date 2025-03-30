package org.example.apartmentmanagement.DAO;

import org.example.apartmentmanagement.Model.Apartment;
import org.example.apartmentmanagement.Model.Building;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class BuildingDAO {
    private static List<Building> buildingList = new ArrayList<>();
    public static void getAllBuilding(){
        buildingList.clear();
        String sql = "select * from buildingList";
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
    }

    public static void showAllBuilding(){
        if(buildingList.isEmpty()) getAllBuilding();
        System.out.println("==============================Building List==============================");
        for(Building building : buildingList){
            System.out.println("------------------------------------------------");
            System.out.println("Building ID: " + building.getBuildingID());
            System.out.println("Building Name: " + building.getBuildingName());
            System.out.println("Address: " + building.getAddress());
            System.out.println("Total Floors: " + building.getTotalFloors());
            System.out.println("Total Apartment: " + building.getTotalApartment());
            System.out.println("Completion Date: " + building.getCompletionDate());
            System.out.println("Created At: " + building.getCreated_at());
            System.out.println("Updated At: " + building.getUpdated_at());
            System.out.println("------------------------------------------------");
        }
    }

    public static void addBuilding(int buildingID, String buildingName, String address,
                                   int totalFloors, int totalApartment, Date completionDate) throws SQLException {
        String sql = "insert into Apartment(building_id, building_name, address, total_floors, total_apartments, completion_date,created_at, updated_at)" +
                "values ( ? , ? , ? , ? , ?, ? , ? , ? )";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, buildingID);
            stmt.setNString(2, buildingName);
            stmt.setNString(3, address);
            stmt.setInt(4, totalFloors);
            stmt.setInt(5, totalApartment);
            stmt.setDate(6, completionDate);
            stmt.setDate(7, Date.valueOf(LocalDate.now()));
            stmt.setDate(8,  Date.valueOf(LocalDate.now()));

            int excuted = stmt.executeUpdate();
            if(excuted > 0) System.out.println("Added Building Successfully!");
            else System.out.println("Error");
        }catch(SQLException e){
            e.printStackTrace();
        }
        buildingList.add(new Building(buildingID,buildingName,address,totalFloors,totalApartment,completionDate,Date.valueOf(LocalDate.now()),Date.valueOf(LocalDate.now())));
    }

    // hàm này dùng để cập nhật một resident thông qua id, khi chưa biết trước nên cập nhật thông tin gì
    // -> dùng Object để kiểm tra đối tượng cần update
    public static Building findBuildingById(int buildingID){
        if(buildingList.isEmpty()) getAllBuilding();
        for(Building building : buildingList){
            if(building.getBuildingID()==buildingID) return building;
        }
        return null;
    }
    public static void updateBuilding(int building_id, String field, Object newValue) throws SQLException {
        List<String> allowColumn = Arrays.asList("building_id", "building_name","address", "totalFloors", "total_apartments", "completion_date");
        if(!allowColumn.contains(field.toLowerCase())){
            System.out.println("Field need update isn't invalid!");
            return;
        }
        String sql = "Update Building set " + field + " = ? where building_id = ?";
        String updated_atSQL = "UPDATE Building SET updated_at = getdate() WHERE building_id = ?";
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
            else if(newValue instanceof Date){
                stmt.setDate(1, (Date) newValue);
            }
            else{
                System.out.println("Field is invalid!");
            }
            stmt.setInt(2, building_id);
            int excuted = stmt.executeUpdate();
            if(excuted > 0) {
                stmt1.setInt(1, building_id);
                stmt1.executeUpdate();
                System.out.println("Update successfully!");
                connection.commit();
            }
            else System.out.println("Update fail!");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    // xóa resident bằng ID
    public static void deleteBuildingById(int buildingID) throws SQLException {
        String SQL = "DELETE FROM Building WHERE building_id = ?";
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement stmt1 = connection.prepareStatement(SQL);
        Scanner sc = new Scanner(System.in);
        try {
            connection.setAutoCommit(false);
            System.out.println("Do you want to delete building with ID " + buildingID + "? (Yes/No)");
            String check = sc.nextLine().trim();
            if (!check.equalsIgnoreCase("Yes")) {
                System.out.println("Deletion cancelled.");
                return;
            }
            stmt1.setInt(1, buildingID);
            int executed = stmt1.executeUpdate();
            if (executed > 0) {
                connection.commit(); // Chỉ commit nếu không có lỗi
                System.out.println("Delete A Building Successfully!");
            } else {
                connection.rollback(); // Rollback nếu không có dòng nào bị xóa
                System.out.println("Building Delete Failed!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        }
    }
}
