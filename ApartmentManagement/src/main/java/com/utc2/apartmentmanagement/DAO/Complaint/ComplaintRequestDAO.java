package com.utc2.apartmentmanagement.DAO.Complaint;

import com.utc2.apartmentmanagement.DAO.DatabaseConnection;
import com.utc2.apartmentmanagement.Model.Maintenance.MaintenanceRequest;
import com.utc2.apartmentmanagement.Repository.Complaint.IComplaintDAO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComplaintRequestDAO implements IComplaintDAO {

    @Override
    public List<MaintenanceRequest> getAllMaintenanceRequest() {
        return List.of();
    }

    @Override
    public List<Map<String, Object>>  getComplaintByResidentId(int residentID){
        List<Map<String, Object>> requestList = new ArrayList<>();
        String sql = "SELECT * FROM Complaint WHERE resident_id = ?";

        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1,residentID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String,Object> rows = new HashMap<>();

                rows.put("complaint_id",rs.getInt("complaint_id"));
                rows.put("apartment_id", rs.getString("apartment_id"));
                rows.put("resident_id",rs.getInt("resident_id"));
                rows.put("created_at", rs.getDate("created_at"));
                rows.put("description",rs.getString("description"));
                rows.put("status",rs.getString("status"));
                rows.put("priority", rs.getString("priority"));
                rows.put("assigned_staff_id",rs.getInt("assigned_staff_id"));
                rows.put("type_complaint",rs.getString("type_complaint"));
                requestList.add(rows);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return requestList;
    }

    @Override
    public int getTotalComplaintByResidentID(int resident_id){
        String sql = "SELECT COUNT(*) AS total_requests FROM Complaint WHERE resident_id = ? ";

        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){

            stmt.setInt(1,resident_id);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
    @Override
    public void saveComplaintRequest(String apartmentID, String residentID, String complaintType,LocalDate requestDate, String description, String priority){
        // status mặc định ban đầu là pending
        // assgin staff là NULL,
        String sql = "INSERT INTO Complaint " +
                "(apartment_id, resident_id, type_complaint, created_at, description, priority, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, apartmentID);
            stmt.setString(2, residentID);
            stmt.setString(3,complaintType );
            stmt.setDate(4, Date.valueOf(requestDate));
            stmt.setString(5, description);
            stmt.setString(6, priority);
            stmt.setString(7, "pending");

            stmt.executeUpdate();
            System.out.println(" Dữ liệu đã được lưu vào Complaint thành công.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(" Lỗi khi lưu dữ liệu: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getFilterStatusAndType(int residentID, String status, String type) {
        List<Map<String, Object>> requestList = new ArrayList<>();

        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = createPreparedStatement(connection,residentID,status,type)){

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String,Object> rows = new HashMap<>();

                rows.put("complaint_id",rs.getInt("complaint_id"));
                rows.put("apartment_id", rs.getString("apartment_id"));
                rows.put("resident_id",rs.getInt("resident_id"));
                rows.put("created_at", rs.getDate("created_at"));
                rows.put("description",rs.getString("description"));
                rows.put("status",rs.getString("status"));
                rows.put("priority", rs.getString("priority"));
                rows.put("assigned_staff_id",rs.getInt("assigned_staff_id"));
                rows.put("type_complaint",rs.getString("type_complaint"));
                requestList.add(rows);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return requestList;
    }

    @Override
    public int getFilteredComplaintCount(int residentID, String status, String type) {
        int total = 0;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = createPreparedStatementForCount(connection, residentID, status, type)) {

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                total = rs.getInt("total");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return total;
    }
    private PreparedStatement createPreparedStatement(Connection connection, int residentID,String status, String type) throws SQLException {
        PreparedStatement stmt = null;

        if (status == null){
            status = "All Status";
        }
        if(type == null){
            type = "All Type";
        }

        if(status.equals("All Status") && type.equals("All Type")){
            String sql = "SELECT * FROM Complaint WHERE resident_id = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1,residentID);
        } else if(status.equals("All Status")){
            String sql = "SELECT * FROM Complaint WHERE resident_id = ? AND type_complaint = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1,residentID);
            stmt.setString(2,type);
        } else if (type.equals("All Type")){
            String sql = "SELECT * FROM Complaint WHERE resident_id = ? AND status = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1,residentID);
            stmt.setString(2,status);
        } else {
            String sql = "SELECT * FROM Complaint WHERE resident_id = ? AND status = ? AND type_complaint = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1,residentID);
            stmt.setString(2,status);
            stmt.setString(3,type);
        }
        return stmt;
    }
    private PreparedStatement createPreparedStatementForCount(Connection connection, int residentID, String status, String type) throws SQLException {
        if (status == null) status = "All Status";
        if (type == null) type = "All Type";

        PreparedStatement stmt;

        if (status.equals("All Status") && type.equals("All Type")) {
            String sql = "SELECT COUNT(*) AS total FROM Complaint WHERE resident_id = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, residentID);
        } else if (status.equals("All Status")) {
            String sql = "SELECT COUNT(*) AS total FROM Complaint WHERE resident_id = ? AND type_complaint = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, residentID);
            stmt.setString(2, type);
        } else if (type.equals("All Type")) {
            String sql = "SELECT COUNT(*) AS total FROM Complaint WHERE resident_id = ? AND status = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, residentID);
            stmt.setString(2, status);
        } else {
            String sql = "SELECT COUNT(*) AS total FROM Complaint WHERE resident_id = ? AND status = ? AND type_complaint = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, residentID);
            stmt.setString(2, status);
            stmt.setString(3, type);
        }

        return stmt;
    }


}

