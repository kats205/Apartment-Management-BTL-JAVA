package com.utc2.apartmentmanagement.DAO;

import com.utc2.apartmentmanagement.Model.Payment;
import com.utc2.apartmentmanagement.Repository.IPaymentDAO;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;
public class PaymentDAO implements IPaymentDAO {
    @Override
    public List<Payment> getAllPayment(){
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
        return paymentList;
    }

    public boolean deletePaymentByID(int paymentID){
        String sql = "DELETE FROM Payment WHERE payment_id = ?";
        try{
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, paymentID);
            return stmt.executeUpdate() > 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean addPayment(Payment payment) {
        String sql = "INSERT INTO Payment(payment_id, bill_id, amount, payment_date, payment_method, transaction_id, status, created_at, updated_at" +
                " VALUES( ? , ? , ? , ? , ? , ? , ? , ? , ?";
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, payment.getPaymentID());
            stmt.setInt(2, payment.getBillID());
            stmt.setDouble(3, payment.getAmount());
            stmt.setDate(4, payment.getPaymentDate());
            stmt.setNString(5, payment.getPaymentMedthod());
            stmt.setString(6, payment.getTransactionID());
            stmt.setNString(7, payment.getStatus());
            stmt.setDate(8, Date.valueOf(LocalDate.now()));
            stmt.setDate(9, Date.valueOf(LocalDate.now()));
            return stmt.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updatebillID(int paymentID, int newbBillId) {
        return updatePaymentField(paymentID, "bill_id", newbBillId);
    }

    @Override
    public boolean updateAmount(int paymentID, double newAmount) {
        return updatePaymentField(paymentID, "amount", newAmount);
    }

    @Override
    public boolean updatePaymentDate(int paymentID, Date newPaymentDate) {
        return updatePaymentField(paymentID, "payment_date", newPaymentDate);
    }

    @Override
    public boolean updatePaymentMethod(int paymentID, String newPaymentMethod) {
        return updatePaymentField(paymentID, "payment_method", newPaymentMethod);
    }

    @Override
    public boolean updateTransactionID(int paymentID, String newTransactionID) {
        return updatePaymentField(paymentID, "transaction_id", newTransactionID);
    }

    @Override
    public boolean updateStatusPayment(int paymentID, String newStatus) {
        return updatePaymentField(paymentID, "status", newStatus);
    }

    public boolean updatePaymentField(int paymentID, String field, Object newValue){
        String sql = "UPDATE payment SET " + field + " = ? , updated_at = ? WHERE payment_id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);){
            if(newValue instanceof String){
                stmt.setNString(1, (String)newValue);
            }
            else if(newValue instanceof Float){
                stmt.setFloat(1,(Float)newValue);
            }
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
            stmt.setInt(3, paymentID);
            return stmt.executeUpdate() > 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
