package com.utc2.apartmentmanagement.Repository;

import com.utc2.apartmentmanagement.Model.Payment;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public interface IPaymentDAO {
    List<Payment> getAllPayment();
    boolean deletePaymentByID(int paymentID);
    boolean addPayment(Payment payment);
    boolean updatebillID(int paymentID, int newbBillId);
    boolean updateAmount(int paymentID, double newAmount);
    boolean updatePaymentDate(int paymentID, Date newPaymentDate);
    boolean updatePaymentMethod(int paymentID, String newPaymentMethod);
    boolean updateTransactionID(int paymentID, String newTransactionID);
    boolean updateStatusPayment(int paymentID, String newStatus);
    List<Payment> findPaymentByDate(LocalDate fromDate, LocalDate toDate);
}
