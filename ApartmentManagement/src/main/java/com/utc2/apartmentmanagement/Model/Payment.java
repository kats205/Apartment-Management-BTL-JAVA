package com.utc2.apartmentmanagement.Model;

import lombok.*;

import java.sql.Date;

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
}
