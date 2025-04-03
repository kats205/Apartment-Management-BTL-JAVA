package org.example.apartmentmanagement.DAO;

import lombok.Getter;
import org.example.apartmentmanagement.Model.Staff;
import org.example.apartmentmanagement.Repository.IStaffDAO;


import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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
