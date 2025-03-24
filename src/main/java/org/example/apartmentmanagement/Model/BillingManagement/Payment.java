package org.example.apartmentmanagement.Model.BillingManagement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    private int paymentID;
    private int billID;
    private double amount;
    private Date paymentDate;
    private String paymentMedthod;
    private String transactionID;
    private String status;

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
