package com.utc2.apartmentmanagement.Model;

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
    private double totalAmount;
    private String status;
    private double late_fee;
    private Date created_at;
    private Date updated_at;



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
