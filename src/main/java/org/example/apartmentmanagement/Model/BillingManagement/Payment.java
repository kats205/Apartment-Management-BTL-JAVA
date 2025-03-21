package org.example.apartmentmanagement.Model.BillingManagement;

import java.io.File;
import java.sql.Date;

public class Payment {
    private int paymentID;
    private int billID;
    private double amount;
    private Date paymentDate;
    private String paymentMedthod;
    private String transactionID;
    private String status;

    public Payment() {
        paymentID = billID = 0;
        amount = 0;
        paymentDate = null;
        paymentMedthod = transactionID = status = "";
    }

    public Payment(int paymentID, int billID, double amount, Date paymentDate, String paymentMedthod, String transactionID, String status) {
        this.paymentID = paymentID;
        this.billID = billID;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMedthod = paymentMedthod;
        this.transactionID = transactionID;
        this.status = status;
    }

    public int getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    public int getBillID() {
        return billID;
    }

    public void setBillID(int billID) {
        this.billID = billID;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMedthod() {
        return paymentMedthod;
    }

    public void setPaymentMedthod(String paymentMedthod) {
        this.paymentMedthod = paymentMedthod;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public  boolean verifyPayment(){

    }
    public File generateRecipt(){

    }
    public Bills getBill(){

    }
}
