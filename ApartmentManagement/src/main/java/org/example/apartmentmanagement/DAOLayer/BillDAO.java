package org.example.apartmentmanagement.DAOLayer;

import org.example.apartmentmanagement.Repository.iBill;
import org.example.apartmentmanagement.models.Bills;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BillDAO implements iBill {

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
        return false;
    }

    @Override
    public Bills getBillByID(int billID) {
        return null;
    }

    @Override
    public boolean updateApartmentID(int billID, String newAparmentID) {
        return false;
    }

    @Override
    public boolean updateBillingDate(int billID, Date newBillDate) {
        return false;
    }

    @Override
    public boolean updateDueDateBill(int billID, Date newDueDate) {
        return false;
    }

    @Override
    public boolean updateStatusBill(int billID, String newStatus) {
        return false;
    }

    @Override
    public boolean deleteBillByID(int billID) throws SQLException {
        return false;
    }
}
