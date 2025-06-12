package com.utc2.apartmentmanagement.DAO;

import com.utc2.apartmentmanagement.Model.MaintenanceRequest;
import com.utc2.apartmentmanagement.Repository.IComplaintDAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ComplaintRequestDAO implements IComplaintDAO {

    @Override
    public List<MaintenanceRequest> getAllMaintenanceRequest() {
        return List.of();
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
}

