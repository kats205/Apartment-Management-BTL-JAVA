package org.example.apartmentmanagement.DAO;

import lombok.Data;
import lombok.Getter;
import org.example.apartmentmanagement.Model.MaintenanceRequest;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MaintenanceRequestDAO {
    @Getter
    private static List<MaintenanceRequest> maintenanceRequestList = new ArrayList<>();


    public static void getAllMaintenanceRequest(){
        maintenanceRequestList.clear();
        String sql = "SELECT * FROM MaintenanceRequest";
        try{
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                maintenanceRequestList.add(new MaintenanceRequest(rs.getInt("request_id"), rs.getString("apartment_id"), rs.getInt("resident_id"),
                        rs.getDate("request_date"), rs.getNString("description"), rs.getString("status"), rs.getString("priority"),
                        rs.getInt("assigned_staff_id"), rs.getDate("completion_date")));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void showAllMainenanceRequest(){
        if(maintenanceRequestList.isEmpty()) getAllMaintenanceRequest();
        System.out.println("==================Maintenance Request List==================");
        for(MaintenanceRequest request : maintenanceRequestList){
            System.out.println("--------------------------------------------");
            System.out.println("Request ID: " + request.getRequestID());
            System.out.println("Apartment ID: " + request.getApartmentID());
            System.out.println("Resident ID: " + request.getResidentID());
            System.out.println("Request Date: " + request.getRequestDate());
            System.out.println("Description: " + request.getDescription());
            System.out.println("Status: " + request.getStatus());
            System.out.println("Priority: " + request.getPriority());
            System.out.println("Assigned Staff ID: " + request.getAssignedStaffID());
            System.out.println("Completion Date: " + request.getCompletionDate());
            System.out.println("--------------------------------------------");
        }
    }

    public static MaintenanceRequest findMaintenanceRequestByID(int requestID){
        if(requestID <= 0){
            System.out.println("Invalid request ID");
        }
        for(MaintenanceRequest request : maintenanceRequestList){
            if(request.getRequestID() == requestID) return request;
        }
        return null;
    }

    public static void deleteMRByID(int requestID){
        if(requestID <= 0){
            System.out.println("Invalid request ID");
        }
        String sql = "DELETE FROM MaintenanceRequest WHERE request_id = ?";
        try{
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            int executed = stmt.executeUpdate();
            if(executed > 0){
                System.out.println("Delete Maintenance Request have ID = " + requestID + "successfully!");
            }
            else{
                System.out.println("Delete fail!");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void updateMRByID(int requestID, String field, Object newValue){
        List<String> allowField = Arrays.asList("request_id", "apartment_id", "resident_id", "request_date", "description", "status", "priority", "assigned_staff_id",
                "completion_date");
        if(!allowField.contains(field.toLowerCase())){
            System.out.println("Filed need update invalid!");
        }
        if(requestID <= 0){
            System.out.println("request ID invalid!");
        }

        String sql = "UPDATE MaintenanceRequest SET " + field + " = ? WHERE request_id = ?";
        String updated_atSQL = "UPDATE MaintenaneRequest SET updated_at = getdate() WHERE request_id = ?";
        try{
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            PreparedStatement stmt1 = connection.prepareStatement(updated_atSQL);
            if(newValue instanceof Integer){
                stmt.setInt(1, (Integer)newValue);
            }
            else if(newValue instanceof String){
                stmt.setString(1, (String)newValue);
            }
            else if(newValue instanceof Date){
                stmt.setDate(1, (Date)newValue);
            }

            stmt.setInt(2,requestID);
            int executed = stmt.executeUpdate();
            if(executed > 0){
                stmt1.setInt(1, requestID);
                stmt1.executeUpdate();
                System.out.println("Update successfully!");
            }
            else{
                System.out.println("Update fail!");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
