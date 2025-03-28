package org.example.apartmentmanagement.DAO;

import org.example.apartmentmanagement.Model.Resident;
import org.example.apartmentmanagement.Model.Staff;


import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class StaffDAO {
    private static List<Staff> staffList = new ArrayList<Staff>();

    public static void getAllStaff(){
        staffList.clear();
        String sql = "select * from Staff";
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
    }

    public static void showStaffList(){
        if(staffList.isEmpty()) getAllStaff();
        for(Staff staff : staffList){
            System.out.println("Staff ID: " + staff.getStaffID());
            System.out.println("Department: " + staff.getDepartment());
            System.out.println("Position: " + staff.getPosition());
            System.out.println("Hire Date: " + staff.getHireDate());
            System.out.println("Manager ID: " + staff.getManagerID());
        }
    }

    //thêm 1 staff vào DB
    public static void addStaff( int staffID, String department, String position, Date hireDate,
                                 int ManagerID){
        String sql = "insert into Staff( staff_id, department, position, hire_date, manager_id," +
                "values ( ? , ? , ? , ? , ?";
        try{
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, staffID);
            stmt.setNString(2, department);
            stmt.setNString(3, position);
            stmt.setDate(4, hireDate);
            stmt.setInt(5, ManagerID);
            int excuted = stmt.executeUpdate();
            if(excuted > 0) System.out.println("Add a resident successfully!");
            staffList.add(new Staff(staffID,department,position,hireDate,ManagerID));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // xóa staff bằng ID
    public static void deleteResidentById(int staffID) throws SQLException {
        String deleteStaffSQL = "DELETE FROM Staff WHERE staff_id  = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt1 = connection.prepareStatement(deleteStaffSQL);
             Scanner sc = new Scanner(System.in)) {
            connection.setAutoCommit(false);
            System.out.println("Do you want to delete staff with ID " + staffID + "? (Yes/No)");
            String check = sc.nextLine().trim();
            if (!check.equalsIgnoreCase("Yes")) {
                System.out.println("Deletion cancelled.");
                return;
            }
            stmt1.setInt(1, staffID);
            int excuted = stmt1.executeUpdate();
            if (excuted > 0) {
                connection.commit(); // Chỉ commit nếu không có lỗi
                System.out.println("Delete A Staff Successfully!");
            } else {
                connection.rollback(); // Rollback nếu không có dòng nào bị xóa
                System.out.println("Staff Delete Failed!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (!DatabaseConnection.getConnection().getAutoCommit()) {
                    DatabaseConnection.getConnection().rollback(); // Rollback nếu có lỗi
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        }

    }
    // tìm staff bằng ID
    public static Staff findStaffByID(int staffID) throws SQLException {
        if(staffList.isEmpty()) getAllStaff();
        for(Staff staff  : staffList){
            if(staff.getStaffID() == staffID) return staff;
        }
        return null;
    }
    // hàm này dùng để cập nhật một staff thông qua id, khi chưa biết trước nên cập nhật thông tin gì
    // -> dùng Object để kiểm tra đối tượng cần update
    public static void updateStaff (int staffID, String field, Object newValue) throws SQLException {
        // các column có sẵn trong Resident để tránh sql injection
        List<String> allowColumn = Arrays.asList("staff_id", "department", "position","hire_date", "manager_id");
        if(!allowColumn.contains(field.toLowerCase())){
            System.out.println("Field need update isn't invalid!");
            return;
        }
        String sql = "Update Staff set " + field + " = ? where staff_id = ?";
        try{
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
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
            else{
                System.out.println("Field is invalid!");
            }
            stmt.setInt(2, staffID);
            int excuted = stmt.executeUpdate();
            if(excuted > 0) {
                System.out.println("Update successfully!");
                connection.commit();
            }
            else System.out.println("Update fail!");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws SQLException {
        showStaffList();
//        updateStaff(3, "manager_id", 2);
        deleteResidentById(3);
    }
}
