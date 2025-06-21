package com.utc2.apartmentmanagement.DAO.User;

import com.utc2.apartmentmanagement.DAO.DatabaseConnection;
import com.utc2.apartmentmanagement.Model.User.Staff;
import com.utc2.apartmentmanagement.Repository.User.IStaffDAO;


import java.sql.*;
import java.sql.Date;
import java.util.*;

public class StaffDAO implements IStaffDAO {

    public List<Staff> getAllStaff(){
        List<Staff> staffList = new ArrayList<>();
        String sql = "SELECT * FROM Staff";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                staffList.add(new Staff(rs.getInt("staff_id"), rs.getNString("department"), rs.getString("position"),
                        rs.getDate("hire_date"), rs.getInt("manager_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staffList;
    }

    @Override
    public boolean addStaff(Staff staff) {
        String sql = "INSERT INTO Staff( staff_id, department, position, hire_date, manager_id," +
                "VALUES ( ? , ? , ? , ? , ?";
        try{
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, staff.getStaffID());
            stmt.setNString(2, staff.getDepartment());
            stmt.setNString(3, staff.getPosition());
            stmt.setDate(4, staff.getHireDate());
            stmt.setInt(5, staff.getManagerID());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    // xóa staff bằng ID
    @Override
    public  boolean deleteStaffByID(int staffID){
        String deleteStaffSQL = "DELETE FROM Staff WHERE staff_id  = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt1 = connection.prepareStatement(deleteStaffSQL);
             Scanner sc = new Scanner(System.in)) {
            System.out.println("Do you want to delete staff with ID " + staffID + "? (Yes/No)");
            String check = sc.nextLine().trim();
            if (!check.equalsIgnoreCase("Yes")) {
                return false;
            }
            stmt1.setInt(1, staffID);
            return stmt1.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateDepartment(int staffID, String newDepartment) {
        return updateStaffField(staffID, "department", newDepartment);
    }

    @Override
    public boolean updatePosition(int staffID, String newPosition) {
        return updateStaffField(staffID, "position", newPosition);
    }

    @Override
    public boolean updateHireDate(int staffID, Date newHireDate) {
        return updateStaffField(staffID, "hire_date", newHireDate);
    }

    @Override
    public boolean updateManagerID(int staffID, int newManagerID) {
        return updateStaffField(staffID, "manager_id", newManagerID);
    }


    @Override
    public List<Map<String, Object>> getAllStaffInfo() throws SQLException{
        String sql = "SELECT s.staff_id, u.username, r.role_name, u.full_name, " +
                "u.email, u.phone_number, s.department, s.position, u.active FROM staff s\n" +
                "JOIN [user] u ON u.user_id = s.staff_id\n" +
                "JOIN role r ON r.role_id = u.role_id";
        List<Map<String, Object>> list = new ArrayList<>();
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)){
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Map<String, Object> row = new HashMap<>();
                row.put("ID", rs.getInt("staff_id"));
                row.put("Tendangnhap", rs.getString("username"));
                row.put("Vaitro", rs.getString("role_name"));
                row.put("Hoten", rs.getString("full_name"));
                row.put("Email", rs.getString("email"));
                row.put("Sodienthoai", rs.getString("phone_number"));
                row.put("Phongban", rs.getString("department"));
                row.put("Chucvu", rs.getString("position"));
                row.put("Trangthai", rs.getBoolean("active"));
                list.add(row);
            }
            return list;
        }catch(SQLException e){
            throw new SQLException("Error retrieving staff information: " + e.getMessage(), e);
        }
    }

    @Override
    public List<String> listPosition() throws SQLException {
        String sql = "SELECT DISTINCT position FROM Staff";
        List<String> positionList = new ArrayList<>();
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)){
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                positionList.add(rs.getString("position"));
            }
        }catch (SQLException e){
            throw new SQLException("Error retrieving position list: " + e.getMessage(), e);
        }
        return positionList;
    }

    @Override
    public List<Map<String, Object>> filterStaffByRoleName(String position) throws SQLException {
        String sql = "SELECT s.staff_id, u.username, r.role_name, u.full_name, " +
                "u.email, u.phone_number, s.department, s.position, u.active FROM staff s\n" +
                "JOIN [user] u ON u.user_id = s.staff_id\n" +
                "JOIN role r ON r.role_id = u.role_id " +
                " WHERE s.position = ?";
        List<Map<String, Object>> list = new ArrayList<>();
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setNString(1, position);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Map<String, Object> row = new HashMap<>();
                row.put("ID", rs.getInt("staff_id"));
                row.put("Tendangnhap", rs.getString("username"));
                row.put("Vaitro", rs.getString("role_name"));
                row.put("Hoten", rs.getString("full_name"));
                row.put("Email", rs.getString("email"));
                row.put("Sodienthoai", rs.getString("phone_number"));
                row.put("Phongban", rs.getString("department"));
                row.put("Chucvu", rs.getString("position"));
                row.put("Trangthai", rs.getBoolean("active"));
                list.add(row);
            }
            return list;
        }catch(SQLException e){
            throw new SQLException("Error retrieving staff information: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, Object>> listStaffRecentActivities() throws SQLException {
        String sql = "SELECT u.username, r.role_name, s.hire_date, u.active  FROM [User] u\n" +
                "JOIN role r ON u.role_id = r.role_id\n" +
                "JOIN Staff s ON s.staff_id = u.user_id";
        List<Map<String, Object>> listStaff = new ArrayList<>();
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)){
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Map<String, Object> rows = new HashMap<>();
                rows.put("username", rs.getString("username"));
                rows.put("role_name", rs.getString("role_name"));
                rows.put("hire_date", rs.getDate("hire_date"));
                rows.put("active", rs.getBoolean("active"));
                listStaff.add(rows);
            }
        }catch(SQLException e){
            throw new SQLException("Error retrieving staff information: " + e.getMessage(), e);
        }
        return listStaff;
    }

    @Override
    public Staff getStaffByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM Staff WHERE staff_id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return new Staff(rs.getInt("staff_id"), rs.getString("department"), rs.getString("position"),
                        rs.getDate("hire_date"), rs.getInt("manager_id"));
            }
        } catch (SQLException e) {
           throw new SQLException("Error retrieving staff by user ID: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public String getDepartmentStaffByUserName(String userName) throws SQLException {
        String sql = "SELECT department FROM staff s JOIN [User] u ON u.user_id = s.staff_id WHERE u.username = ?";
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userName);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return rs.getString("department");
            }
        }catch(SQLException e){
            throw new SQLException("Error retrieving staff by user name: " + e.getMessage(), e);
        }
        return "";
    }


    public boolean updateStaffField(int staffID, String field, Object newValue){
        String sql = "UPDATE Staff SET " + field + " = ?  WHERE staff_id = ?";
        try( Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);){
            if(newValue instanceof String){
                stmt.setString(1, (String)newValue);
            }
            else if(newValue instanceof Integer){
                stmt.setInt(1, (Integer) newValue);
            }
            else if(newValue instanceof Date){
                stmt.setDate(1, (Date) newValue);
            }
            else{
                System.out.println("Field is invalid!");
            }
            stmt.setInt(2, staffID);
            return stmt.executeUpdate() > 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

}
