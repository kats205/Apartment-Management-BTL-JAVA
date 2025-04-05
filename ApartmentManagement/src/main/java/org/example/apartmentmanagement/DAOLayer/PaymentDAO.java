package org.example.apartmentmanagement.DAOLayer;

import org.example.apartmentmanagement.Repository.iPayment;
import org.example.apartmentmanagement.models.Payment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PaymentDAO implements iPayment {

    @Override
    public List<Payment> getAllPayment() {
        List<Payment> paymentList = new ArrayList<>();
        String sql = "SELECT * FROM payment";
        try{
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                paymentList.add(new Payment(rs.getInt("payment_id"), rs.getInt("bill_id"), rs.getFloat("amount"),
                        rs.getDate("payment_date"), rs.getString("payment_method"), rs.getString("transaction_id"),
                        rs.getString("status"), rs.getDate("created_at"), rs.getDate("updated_at")));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return paymentList;    }

    @Override
    public boolean deletePaymentByID(int paymentID) {
        return false;
    }

    @Override
    public boolean addPayment(Payment payment) {
        return false;
    }

    @Override
    public boolean updatebillID(int paymentID, int newbBillId) {
        return false;
    }

    @Override
    public boolean updateAmount(int paymentID, double newAmount) {
        return false;
    }

    @Override
    public boolean updatePaymentDate(int paymentID, Date newPaymentDate) {
        return false;
    }

    @Override
    public boolean updatePaymentMethod(int paymentID, String newPaymentMethod) {
        return false;
    }

    @Override
    public boolean updateTransactionID(int paymentID, String newTransactionID) {
        return false;
    }

    @Override
    public boolean updateStatusPayment(int paymentID, String newStatus) {
        return false;
    }
}
