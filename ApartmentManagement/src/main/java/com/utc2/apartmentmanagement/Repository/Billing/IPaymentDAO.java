package com.utc2.apartmentmanagement.Repository.Billing;

import com.utc2.apartmentmanagement.Model.Billing.Payment;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IPaymentDAO {
    List<Payment> getAllPayment();
    boolean deletePaymentByID(int paymentID);
    int addPayment(int billId, double amount, String paymentMethod, String transactionId, String status)throws SQLException;
    boolean updatebillID(int paymentID, int newbBillId);
    boolean updateAmount(int paymentID, double newAmount);
    boolean updatePaymentDate(int paymentID, Date newPaymentDate);
    boolean updatePaymentMethod(int paymentID, String newPaymentMethod);
    boolean updateTransactionID(int paymentID, String newTransactionID);
    boolean updateStatusPayment(int paymentID, String newStatus);
    List<Payment> findPaymentByDate(LocalDate fromDate, LocalDate toDate);
    List<Map<String, Object>> listPaymentDetail() throws SQLException;
    double totalPaymentFromStatus(String status) throws SQLException;
}
