package org.example.apartmentmanagement.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bills {
    private int billID;
    private String apartmentID;
    private Date billingDate;
    private Date dueDate;
    private double totalAmount;
    private String status;
    private double late_fee;
    private Date created_at;
    private Date updated_at;

    public Bills(int billId, double amount, String description) {
        billID = billId;
        totalAmount = amount;
        status = description;
    }
    public String getPaymentUrl() {
        return "https://payment.example.com/pay?billId=" + billID + "&amount=" + totalAmount;
    }
    public void printBillDetails() {
        System.out.println("Bill ID: " + billID);
        System.out.println("Amount: " + totalAmount);
        System.out.println("Payment URL: " + getPaymentUrl());
    }
//    public double calculateTotalAmount(){
//
//    }
//    public File generatePDF(){
//
//    }
//    public void addItem(BillItems item){
//
//    }
//    public boolean isPaid(){
//
//    }
}
