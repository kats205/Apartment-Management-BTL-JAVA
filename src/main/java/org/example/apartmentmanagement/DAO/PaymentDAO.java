package org.example.apartmentmanagement.DAO;

import org.example.apartmentmanagement.Model.Payment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PaymentDAO {
    private static List<Payment> paymentList = new ArrayList<>();

    public static void getAllPayment(){
        paymentList.clear();
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
    }
    public static void showAllPayment(){
        if(paymentList.isEmpty()) getAllPayment();
        System.out.println("=====================Payment List=====================");
        for(Payment payment : paymentList){
            System.out.println("--------------------------------------");
            System.out.println("payment ID: " + payment.getPaymentID());
            System.out.println("Bill ID: " + payment.getBillID());
            System.out.println("Amount: " + payment.getAmount());
            System.out.println("Payment Date: " + payment.getPaymentDate());
            System.out.println("Payment method: " + payment.getPaymentMedthod());
            System.out.println("Transaction ID: " + payment.getTransactionID());
            System.out.println("Status: " + payment.getStatus());
            System.out.println("Created At: " + payment.getCreated_at());
            System.out.println("Updated At: " + payment.getUpdated_at());
            System.out.println("--------------------------------------");
        }
    }
    public static Payment findPaymentById(int paymentID){
        if(paymentID <= 0){
            System.out.println("payment ID invalid!");
            return null;
        }
        for(Payment payment : paymentList){
            if(payment.getPaymentID() == paymentID) return payment;
        }
        return null;
    }
    public static void deletePaymentByID(int paymentID){
        if(paymentID <= 0){
            System.out.println("Payment ID invalid!");
            return;
        }
        String sql = "DELETE FROM Payment WHERE payment_id = ?";
        try{
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, paymentID);
            int executed = stmt.executeUpdate();
            if(executed > 0){
                System.out.println("Delete Successfully!");
            }
            else{
                System.out.println("Delete Fail!");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void updatePaymentById(int paymentID, String field, Object newValue){
        if(paymentID <= 0){
            System.out.println("payment ID invalid!");
            return;
        }
        List<String> allowField = Arrays.asList("payment_id", "bill_id", "amount", "payment_date", "payment_method", "transaction_id", "status");
        if(!allowField.contains(field.toLowerCase())){
            System.out.println("Field invalid!");
        }
        String sql = "UPDATE payment SET " + field + " = ? WHERE payment_id = ?";
        String updated_atSQL = "UPDATE payment SET updated_at = getdate() WHERE payment_id = ?";
        try{
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            PreparedStatement stmt1 = connection.prepareStatement(updated_atSQL);
            if(newValue instanceof String){
                stmt.setNString(1, (String)newValue);
            }
            else if(newValue instanceof Float){
                stmt.setFloat(1,(Float)newValue);
            }

            stmt.setInt(2, paymentID);
            int executed = stmt.executeUpdate();
            if(executed > 0){
                stmt1.setInt(1, paymentID);
                stmt1.executeUpdate();
                System.out.println("Update successfully!");
            }
            else{
                System.out.println("Update fail!");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
