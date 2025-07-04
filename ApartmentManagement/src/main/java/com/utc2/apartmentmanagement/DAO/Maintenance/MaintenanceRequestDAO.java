package com.utc2.apartmentmanagement.DAO.Maintenance;


import com.utc2.apartmentmanagement.DAO.DatabaseConnection;
import com.utc2.apartmentmanagement.Model.Maintenance.MaintenanceRequest;
import com.utc2.apartmentmanagement.Repository.Maintenance.IMaintenanceRequestDAO;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                        rs.getInt("assigned_staff_id"), rs.getDate("completion_date"), rs.getString("issue_type")));
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
                        rs.getDate("request_date"), rs.getString("description"), rs.getString("status"), rs.getString("priority"),
                        rs.getInt("assigned_staff_id"), rs.getDate("completion_date"),rs.getString("issue_type"));
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
        String sql = "UPDATE MaintenanceRequest SET " + field + " = ? , updated_at = ? WHERE request_id = ?";
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

    @Override // lưu sau khi gửi yêu cầu bảo trì
    public int saveMaintenaceRequest(String apartmentID, String residentID, LocalDate requestDate, String description,String priority, String issueType) {
        // status mặc định ban đầu là pending
        // assgin staff là NULL,
        // completion_date NULL,
        String sql = "INSERT INTO MaintenanceRequest " +
                "(apartment_id, resident_id, request_date, description, priority, created_at, updated_at, status,issue_type) " +
                " OUTPUT INSERTED.request_id VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, apartmentID);
            stmt.setString(2, residentID);
            stmt.setDate(3, Date.valueOf(requestDate));
            stmt.setString(4, description);
            stmt.setString(5, priority);
            stmt.setDate(6, Date.valueOf(LocalDate.now()));
            stmt.setDate(7, Date.valueOf(LocalDate.now()));
            stmt.setString(8, "pending");
            stmt.setString(9,issueType);

            ResultSet rs = stmt.executeQuery();
            System.out.println(" Dữ liệu đã được lưu vào MaintenanceRequest thành công.");
            if(rs.next()) return rs.getInt("request_id");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(" Lỗi khi lưu dữ liệu: " + e.getMessage());
        }
        return -1; // Trả về -1 nếu có lỗi xảy ra
    }

    @Override
    public List<Map<String, Object>> getMaintenanceRequestsByResidentId(int residentID){
        List<Map<String, Object>> requestList = new ArrayList<>();
        String sql = "SELECT * FROM MaintenanceRequest WHERE resident_id = ?";

        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1,residentID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String,Object> rows = new HashMap<>();

                rows.put("request_id",rs.getInt("request_id"));
                rows.put("apartment_id", rs.getString("apartment_id"));
                rows.put("resident_id",rs.getInt("resident_id"));
                rows.put("request_date", rs.getDate("request_date"));
                rows.put("description",rs.getString("description"));
                rows.put("status",rs.getString("status"));
                rows.put("priority", rs.getString("priority"));
                rows.put("assigned_staff_id",rs.getInt("assigned_staff_id"));
                rows.put("completion_date",rs.getDate("completion_date"));
                rows.put("issue_type",rs.getString("issue_type"));
                requestList.add(rows);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return requestList;
    }

    @Override
    public int getTotalMaintenaceRequestByResidentID(int resident_id) {
        String sql = "SELECT COUNT(*) AS total_requests FROM MaintenanceRequest WHERE resident_id = ? ";

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
    public List<Map<String, Object>> getFilterStatusAndPriority(int residentID, String status, String priority) {
        List<Map<String, Object>> requestList = new ArrayList<>();

        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = createPreparedStatement(connection,residentID,status,priority)){

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String,Object> rows = new HashMap<>();

                rows.put("request_id",rs.getInt("request_id"));
                rows.put("apartment_id", rs.getString("apartment_id"));
                rows.put("resident_id",rs.getInt("resident_id"));
                rows.put("request_date", rs.getDate("request_date"));
                rows.put("description",rs.getString("description"));
                rows.put("status",rs.getString("status"));
                rows.put("priority", rs.getString("priority"));
                rows.put("assigned_staff_id",rs.getInt("assigned_staff_id"));
                rows.put("completion_date",rs.getDate("completion_date"));
                rows.put("issue_type",rs.getString("issue_type"));
                requestList.add(rows);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return requestList;    }

    @Override
    public List<MaintenanceRequest> getFilterStatusAndPriority(String field, String value) {
        List<MaintenanceRequest> maintenanceRequestList = new ArrayList<>();
        String sql = "SELECT * FROM MaintenanceRequest WHERE " + field + " = ?";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1,value);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                maintenanceRequestList.add(new MaintenanceRequest(rs.getInt("request_id"), rs.getString("apartment_id"), rs.getInt("resident_id"),
                        rs.getDate("request_date"), rs.getString("description"), rs.getString("status"), rs.getString("priority"),
                        rs.getInt("assigned_staff_id"), rs.getDate("completion_date"), rs.getString("issue_type")));
            }
        } catch (SQLException e) {
            System.out.println("Đã xảy ra lỗi trong quá trình lọc status!" + e.getMessage());
            throw new RuntimeException(e);
        }
        return maintenanceRequestList;
    }

    @Override
    public boolean updateImageFileName(int requestID, String imageFileName) {
        String sql = "UPDATE MaintenanceRequest SET image_filename = ? WHERE request_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, imageFileName);
            stmt.setInt(2, requestID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateAssignedStaff(int requestID, int staffID) {
        String sql = "UPDATE MaintenanceRequest SET assigned_staff_id = ? WHERE request_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, staffID);
            stmt.setInt(2, requestID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // TODO: Hỗ trợ set biến cho method getFilterStatusAndPriority
    private PreparedStatement createPreparedStatement(Connection connection, int residentID,String status, String priority) throws SQLException {
        PreparedStatement stmt = null;

        if (status == null){
            status = "All Status";
        }
        if(priority == null){
            priority = "All Priority";
        }

        if(status.equals("All Status") && priority.equals("All Priority")){
            String sql = "SELECT * FROM MaintenanceRequest WHERE resident_id = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1,residentID);
        } else if(status.equals("All Status")){
            String sql = "SELECT * FROM MaintenanceRequest WHERE resident_id = ? AND priority = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1,residentID);
            stmt.setString(2,priority);
        } else if (priority.equals("All Priority")){
            String sql = "SELECT * FROM MaintenanceRequest WHERE resident_id = ? AND status = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1,residentID);
            stmt.setString(2,status);
        } else {
            String sql = "SELECT * FROM MaintenanceRequest WHERE resident_id = ? AND status = ? AND priority = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1,residentID);
            stmt.setString(2,status);
            stmt.setString(3,priority);
        }
        return stmt;
    }

    public int getFilteredMaintenanceCount(int residentID, String status, String priority) {
        int total = 0;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = createPreparedStatementForCount(connection, residentID, status, priority)) {

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                total = rs.getInt("total");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return total;
    }

    private PreparedStatement createPreparedStatementForCount(Connection connection, int residentID, String status, String priortity) throws SQLException {
        if (status == null) status = "All Status";
        if (priortity == null) priortity = "All Priority";

        PreparedStatement stmt;

        if (status.equals("All Status") && priortity.equals("All Priority")) {
            String sql = "SELECT COUNT(*) AS total FROM MaintenanceRequest WHERE resident_id = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, residentID);
        } else if (status.equals("All Status")) {
            String sql = "SELECT COUNT(*) AS total FROM MaintenanceRequest WHERE resident_id = ? AND priority = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, residentID);
            stmt.setString(2, priortity);
        } else if (priortity.equals("All Priority")) {
            String sql = "SELECT COUNT(*) AS total FROM MaintenanceRequest WHERE resident_id = ? AND status = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, residentID);
            stmt.setString(2, status);
        } else {
            String sql = "SELECT COUNT(*) AS total FROM MaintenanceRequest WHERE resident_id = ? AND status = ? AND priority = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, residentID);
            stmt.setString(2, status);
            stmt.setString(3, priortity);
        }

        return stmt;
    }

    @Override
    public List<Map<String, Object>> getMaintenanceRequestsByAssignedStaffId(int staffId) {
            List<Map<String, Object>> requestList = new ArrayList<>();
        String sql = "SELECT mr.issue_type,mr.status,mr.completion_date, mr.request_id, mr.apartment_id, mr.description, mr.priority, u.full_name AS staff_name, ru.full_name AS resident_name,mr.request_date FROM MaintenanceRequest mr JOIN Staff s ON mr.assigned_staff_id = s.staff_id JOIN [User] u ON s.staff_id = u.user_id JOIN Resident r ON mr.resident_id = r.resident_id JOIN [User] ru ON r.user_id = ru.user_id WHERE mr.assigned_staff_id = ? AND mr.status IN ('assigned', 'in_progress') ORDER BY CASE WHEN mr.status = 'assigned' THEN 0 WHEN mr.status = 'in_progress' THEN 1 ELSE 2 END, mr.request_date DESC;";
            try(Connection connection = DatabaseConnection.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)){
                stmt.setInt(1,staffId);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    Map<String, Object> rows = new HashMap<>();

                    rows.put("request_id", rs.getInt("request_id"));
                    rows.put("apartment_id", rs.getString("apartment_id"));
                    rows.put("description", rs.getString("description"));
                    rows.put("status", rs.getString("status"));
                    rows.put("priority", rs.getString("priority"));
                    rows.put("staff_name", rs.getString("staff_name"));
                    rows.put("resident_name", rs.getString("resident_name"));
                    rows.put("issue_type", rs.getString("issue_type"));
                    rows.put("request_date", rs.getDate("request_date"));
                    requestList.add(rows);
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return requestList;
        }

    @Override
    public boolean updateStatus(int requestId, String newStatus) {
        String sql = "UPDATE MaintenanceRequest SET status = ? WHERE request_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setInt(2, requestId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

