package com.utc2.apartmentmanagement.Model;

import lombok.*;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

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

    public String formatDate(Date date) {
        if (date == null) return null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }

    public Date minDate(List<Payment> paymentList){
        return paymentList.isEmpty() ? null :
                paymentList.stream()
                        .map(Payment::getPaymentDate)
                        .filter(Objects::nonNull)
                        .min(Date::compareTo)
                        .orElse(null);
    }

    public Date maxDate(List<Payment> paymentList) {
        return paymentList.isEmpty() ? null :
                paymentList.stream()
                        .map(Payment::getPaymentDate)
                        .filter(Objects::nonNull)
                        .max(Date::compareTo)
                        .orElse(null);
    }

}
