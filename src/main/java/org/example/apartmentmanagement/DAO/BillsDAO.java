package org.example.apartmentmanagement.DAO;

import org.example.apartmentmanagement.Model.Apartment;
import org.example.apartmentmanagement.Model.Bills;
import org.example.apartmentmanagement.Repository.IBillDAO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class BillsDAO implements IBillDAO {
    @Override
    public List<Bills> getAllbills() {
        List<Bills> billsList = new ArrayList<>();
        String sql = "SELECT * FROM Bill";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                billsList.add(new Bills(rs.getInt("bill_id"), rs.getString("apartment_id"), rs.getDate("billing_date"),
                        rs.getDate("due_date"), rs.getDouble("total_amount"), rs.getString("status"), rs.getDouble("late_fee"),
                        rs.getDate("created_at"), rs.getDate("updated_at")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return billsList;
    }

    @Override
    public boolean addBill(Bills bill) {
        String sql = "INSERT INTO Bill(bill_id, apartment_id, billing_date, due_date, total_amount, status, late_fee, created_at, updated_at)" +
                "VALUES ( ? , ? , ? , ? , ?, ? , ? , ? , ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, bill.getBillID());
            stmt.setString(2, bill.getApartmentID());
            stmt.setDate(3, bill.getBillingDate());
            stmt.setDate(4, bill.getDueDate());
            stmt.setDouble(5, bill.getTotalAmount());
            stmt.setString(6, bill.getStatus());
            stmt.setDouble(7, bill.getLate_fee());
            stmt.setDate(8, Date.valueOf(LocalDate.now()));
            stmt.setDate(9, Date.valueOf(LocalDate.now()));
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Bills getBillByID(int billID) {
        String sql = "SELECT * FROM Bill WHERE bill_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, billID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Bills(rs.getInt("billID"),
                        rs.getString("apartment_id"),
                        rs.getDate("billing_date"),
                        rs.getDate("due_date"),
                        rs.getDouble("total_amount"),
                        rs.getString("status"),
                        rs.getDouble("late_fee"),
                        rs.getDate("created_at"),
                        rs.getDate("updated_at"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updateApartmentID(int billID, String newApartmentID) {
        return updateBillField(billID, "apartment_id", newApartmentID);
    }

    @Override
    public boolean updateBillingDate(int billID, Date newBillingDate) {
        return updateBillField(billID, "billing_date", newBillingDate);
    }

    @Override
    public boolean updateDueDateBill(int billID, Date newDueDate) {
        return updateBillField(billID, "due_date", newDueDate);
    }

    @Override
    public boolean updateStatusBill(int billID, String newStatus) {
        return updateBillField(billID, "status", newStatus);
    }

    public boolean updateBillField(int billID, String field, Object newValue) {
        String sql = "UPDATE Bill SET " + field + " = ? , updated_at = ? WHERE bill_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (newValue instanceof String) {
                stmt.setString(1, (String) newValue);
            } else if (newValue instanceof Double) {
                stmt.setDouble(1, (Double) newValue);
            } else if (newValue instanceof Date) {
                stmt.setDate(1, (Date) newValue);
            }
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
            stmt.setInt(3, billID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteBillByID(int billID) throws SQLException {
        String SelectApartmentIDSQL = "SELECT apartment_id FROM Bill WHERE bill_id = ?";
        String DeleteApartmentSQL = "DELETE FROM Apartment WHERE apartment_id = ?";
        String deleteBillSQL = "DELETE FROM Bill WHERE bill_id = ?";

        Scanner sc = new Scanner(System.in);
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt1 = connection.prepareStatement(SelectApartmentIDSQL);
             PreparedStatement stmt2 = connection.prepareStatement(DeleteApartmentSQL);
             PreparedStatement stmt3 = connection.prepareStatement(deleteBillSQL)) {
            System.out.println("Do you want to delete bill with ID " + billID + "? (Yes/No)");
            String check = sc.nextLine().trim();
            if (!check.equalsIgnoreCase("Yes")) {
                System.out.println("Deletion cancelled.");
                return false;
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
                    return stmt3.executeUpdate() > 0;
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}