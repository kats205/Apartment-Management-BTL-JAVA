package com.utc2.apartmentmanagement.Model.Billing;

import lombok.*;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Payment {
    private int paymentID;
    private int billID;
    private double amount;
    private Date paymentDate;
    private String paymentMedthod;
    private String transactionID;
    private String status;
    private Date created_at;
    private Date updated_at;
//
//    public  boolean verifyPayment(){
//
//    }
//    public File generateRecipt(){
//
//    }
//    public Bills getBill(){
//
//    }

    public int completedPayment(List<Payment> paymentList){
        int total = 0;
        for(Payment payment : paymentList){
            if(payment.getStatus().equals("completed")) total++;
        }
        return total;
    }
    public double totalAmountCollected(List<Payment> paymentList){
        double total = 0;
        for(Payment payment : paymentList){
            if(payment.getStatus().equals("completed")){
                total+= payment.getAmount();
            }
        }
        return total;
    }
    public double totalOutstandingLabel(List<Payment> paymentList){
        double total = 0;
        for(Payment payment : paymentList){
            if(!payment.getStatus().equals("completed")){
                total+= payment.getAmount();
            }
        }
        return total;
    }
    public double paymentRateLabel(List<Payment> paymentList){
        double totalCollected = totalAmountCollected(paymentList);
        double totalOutstanding = totalOutstandingLabel(paymentList);
        return (totalCollected / (totalCollected + totalOutstanding)) * 100;
    }

}
