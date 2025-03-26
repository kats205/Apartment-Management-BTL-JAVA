package org.example.apartmentmanagement.Model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

//    public double calculateTotal(){
//
//    }
}
