package org.example.apartmentmanagement.DAO;

import org.example.apartmentmanagement.Model.Apartment;
import org.example.apartmentmanagement.Model.Bills;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class BillsDAO {
    private static List<Bills> BillsList = new ArrayList<Bills>();
    public static void getAllBills(){
        String sql = "SELECT * FROM Bill";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                BillsList.add(new Bills(rs.getInt("bill_id"), rs.getString("apartment_id"), rs.getDate("billing_date"),
                        rs.getDate("due_date"), rs.getDouble("total_amount"), rs.getString("status"), rs.getDouble("late_fee"),
                        rs.getDate("created_at"), rs.getDate("updated_at")));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public void showAllBills(){
        if(BillsList.isEmpty()) getAllBills();
        System.out.println("==================Bill List==================");
        for(Bills bill : BillsList){
            System.out.println("----------------------------------");
            System.out.println("Bill ID: " + bill.getBillID());
            System.out.println("Apartment ID: " + bill.getApartmentID());
            System.out.println("Billing Date: " + bill.getBillingDate());
            System.out.println("Due Date: " + bill.getDueDate());
            System.out.println("Total Amount: " + bill.getTotalAmount());
            System.out.println("Status: " + bill.getStatus());
            System.out.println("Late fee: " + bill.getLate_fee());
            System.out.println("Created At: " + bill.getCreated_at());
            System.out.println("Updated_At: " + bill.getUpdated_at());
            System.out.println("----------------------------------");
        }
    }
    public static void addBills(int billID, String apartmentID, Date billingDate, Date dueDate, double totalAmount, String status, double late_fee) throws SQLException {
        String sql = "INSERT INTO Bill(bill_id, apartment_id, billing_date, due_date, total_amount, status, late_fee, created_at, updated_at)" +
                "VALUES ( ? , ? , ? , ? , ?, ? , ? , ? , ?)";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, billID);
            stmt.setString(2, apartmentID);
            stmt.setDate(3, billingDate);
            stmt.setDate(4, dueDate);
            stmt.setDouble(5, totalAmount);
            stmt.setString(6, status);
            stmt.setDouble(7, late_fee);
            stmt.setDate(8, Date.valueOf(LocalDate.now()));
            stmt.setDate(9, Date.valueOf(LocalDate.now()));

            int excuted = stmt.executeUpdate();
            if(excuted > 0) System.out.println("Added Bill Successfully!");
            else System.out.println("Error");
        }catch(SQLException e){
            e.printStackTrace();
        }
        BillsList.add(new Bills(billID,apartmentID,billingDate,dueDate,totalAmount,status,late_fee,Date.valueOf(LocalDate.now()),Date.valueOf(LocalDate.now())));
    }

    // hàm này dùng để cập nhật một resident thông qua id, khi chưa biết trước nên cập nhật thông tin gì
    // -> dùng Object để kiểm tra đối tượng cần update
    public static Bills findApartmentById(int billID){
        if(BillsList.isEmpty()) getAllBills();
        for(Bills bills : BillsList){
            if(bills.getBillID() == billID) return bills;
        }
        return null;
    }
    public static void updateBills(int billID, String field, Object newValue) throws SQLException {
        // các column có sẵn trong Bills để tránh sql injection
        List<String> allowColumn = Arrays.asList("bill_id", "apartment_id", "billing_date","due_date", "total_amount", "status", "late_fee");
        if(!allowColumn.contains(field.toLowerCase())){
            System.out.println("Field need update isn't invalid!");
            return;
        }
        String sql = "UPDATE Bill SET " + field + " = ? WHERE bill_id = ?";
        String updated_atSQL = "UPDATE Bill SET updated_at = getdate() where bill_id = ?";
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
                stmt.setDate(1, (Date)(newValue));
            }
            else{
                System.out.println("Field is invalid!");
            }
            stmt.setInt(2, billID);
            int excuted = stmt.executeUpdate();
            if(excuted > 0) {
                stmt1.setInt(1, billID);
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
    public static void deleteBillsByID(int billID) throws SQLException {
        String SelectApartmentIDSQL = "SELECT apartment_id FROM Bill WHERE bill_id = ?";
        String DeleteApartmentSQL = "DELETE FROM Apartment WHERE apartment_id = ?";
        String deleteBillSQL = "DELETE FROM Bill WHERE bill_id = ?";

        Scanner sc = new Scanner(System.in);
        Connection connection = DatabaseConnection.getConnection();
        try {
            PreparedStatement stmt1 = connection.prepareStatement(SelectApartmentIDSQL);
            PreparedStatement stmt2 = connection.prepareStatement(DeleteApartmentSQL);
            PreparedStatement stmt3 = connection.prepareStatement(deleteBillSQL);
            connection.setAutoCommit(false);

            System.out.println("Do you want to delete bill with ID " + billID + "? (Yes/No)");
            String check = sc.nextLine().trim();
            if (!check.equalsIgnoreCase("Yes")) {
                System.out.println("Deletion cancelled.");
                return;
            }

            stmt1.setInt(1, billID);
            try (ResultSet rs = stmt1.executeQuery()) {
                if (rs.next()) {
                    int apartmentId = rs.getInt("apartment_id");

                    // Xóa Apartment trước
                    stmt2.setInt(1, apartmentId);
                    stmt2.executeUpdate();

                    // Xóa Bill
                    stmt3.setInt(1, billID);
                    int executed = stmt3.executeUpdate();

                    if (executed > 0) {
                        connection.commit();
                        System.out.println("Delete A Bill Successfully!");
                    } else {
                        connection.rollback();
                        System.out.println("Bill Delete Failed!");
                    }
                } else {
                    System.out.println("No apartment found for bill_id: " + billID);
                    connection.rollback();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback(); // Rollback nếu có lỗi
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        }
    }
}
