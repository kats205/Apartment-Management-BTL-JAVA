package com.utc2.apartmentmanagement.Model.Billing;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillItems {
    private int itemID;
    private int billID;
    private String itemType;
    private String description;
    private double amount;
    private double quantity;
    private double total;
    private Date created_at;
    private Date updated_at;
//    public double calculateTotal(){
//
//    }
}
