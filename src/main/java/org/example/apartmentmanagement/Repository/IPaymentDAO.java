package org.example.apartmentmanagement.Repository;

import org.example.apartmentmanagement.Model.Payment;

import java.sql.Date;
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
}
