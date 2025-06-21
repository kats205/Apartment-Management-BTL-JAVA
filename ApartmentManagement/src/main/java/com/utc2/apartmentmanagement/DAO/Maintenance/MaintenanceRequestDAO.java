package com.utc2.apartmentmanagement.DAO.Maintenance;


import com.utc2.apartmentmanagement.DAO.DatabaseConnection;
import com.utc2.apartmentmanagement.Model.Maintenance.MaintenanceRequest;
import com.utc2.apartmentmanagement.Repository.Maintenance.IMaintenanceRequestDAO;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MaintenanceRequestDAO implements IMaintenanceRequestDAO {
    @Override
    public List<MaintenanceRequest>  getAllMaintenanceRequest(){
        List<MaintenanceRequest> maintenanceRequestList = new ArrayList<>();
        String sql = "SELECT * FROM MaintenanceRequest";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);){
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                maintenanceRequestList.add(new MaintenanceRequest(rs.getInt("request_id"), rs.getString("apartment_id"), rs.getInt("resident_id"),
                        rs.getDate("request_date"), rs.getString("description"), rs.getString("status"), rs.getString("priority"),
                        rs.getInt("assigned_staff_id"), rs.getDate("completion_date")));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return maintenanceRequestList;
    }

    @Override
    public MaintenanceRequest getMaintenanceRequestByID(int requestID) {
        String sql = "SELECT * FROM MaintenanceRequest WHERE request_id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)){
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return new MaintenanceRequest(rs.getInt("request_id"), rs.getString("apartment_id"), rs.getInt("resident_id"),
                        rs.getDate("request_date"), rs.getNString("description"), rs.getString("status"), rs.getString("priority"),
                        rs.getInt("assigned_staff_id"), rs.getDate("completion_date"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updateApartmentID(int requestID, int newApartmentID) {
        return false;
    }

    @Override
    public boolean updateResidentID(int requestID, int newResidentID) {
        return false;
    }

    @Override
    public boolean updateRequestDate(int requestID, Date newRequestDate) {
        return updateMaintenanceRequestField(requestID, "request_date", newRequestDate);
    }

    @Override
    public boolean updateDescription(int requestID, String newDescription) {
        return updateMaintenanceRequestField(requestID, "description", newDescription);
    }

    @Override
    public boolean updateStatusRequest(int requestID, String newStatus) {
        return updateMaintenanceRequestField(requestID, "status", newStatus);
    }

    @Override
    public boolean updateAssignedStaffId(int requestID, int newAssignStaffID) {
        return updateMaintenanceRequestField(requestID, "assigned_staff_id", newAssignStaffID);
    }

    @Override
    public boolean updateCompletionDate(int requestID, Date newCompletionDate) {
        return updateMaintenanceRequestField(requestID, "completion_date", newCompletionDate);
    }
    public boolean updateMaintenanceRequestField(int requestID, String field, Object newValue){
        String sql = "UPDATE MaintenanceRequest SET " + field + " = ? , updated_field = ? WHERE requestID = ?";
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)){
            if(newValue instanceof String){
                stmt.setNString(1, (String) newValue);
            }
            else if(newValue instanceof Integer){
                stmt.setInt(1, (Integer)newValue);
            }
            else if(newValue instanceof Date){
                stmt.setDate(1,(Date)newValue);
            }
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
            stmt.setInt(3, requestID);
            return stmt.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public boolean deleteMaintenanceRequestByID(int requestID) {
        String sql = "DELETE FROM MaintenanceRequest WHERE request_id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);){
            return stmt.executeUpdate() > 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean addMaintenanceRequest(MaintenanceRequest maintenanceRequest) {
        String sql = "INSERT INTO MaintenanceRequest(request_id, apartment_id, resident_id, request_date, description," +
                " status, priority, assigned_staff_id, completion_date, created_at, updated_at)" +
                " VALUES ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?)";
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, maintenanceRequest.getRequestID());
            stmt.setString(2, maintenanceRequest.getApartmentID());
            stmt.setInt(3, maintenanceRequest.getResidentID());
            stmt.setDate(4, maintenanceRequest.getRequestDate());
            stmt.setString(5, maintenanceRequest.getDescription());
            stmt.setString(6, maintenanceRequest.getStatus());
            stmt.setString(7, maintenanceRequest.getPriority());
            stmt.setInt(8, maintenanceRequest.getAssignedStaffID());
            stmt.setDate(9, maintenanceRequest.getCompletionDate());
            stmt.setDate(10, Date.valueOf(LocalDate.now()));
            stmt.setDate(11, Date.valueOf(LocalDate.now()));
            return stmt.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int countRequestByStatus(String status) throws SQLException {
        String sql = "SELECT COUNT(*) FROM MaintenanceRequest WHERE status = ?";
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }
        }catch(SQLException e){
            throw new SQLException("Error counting requests by status: " + e.getMessage(), e);
        }
        return 0;
    }
}
