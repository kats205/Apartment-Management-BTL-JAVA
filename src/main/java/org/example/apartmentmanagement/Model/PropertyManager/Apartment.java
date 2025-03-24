package org.example.apartmentmanagement.Model.PropertyManager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.apartmentmanagement.Model.BillingManagement.Bills;
import org.example.apartmentmanagement.Model.ResidentManager.Resident;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Apartment {
    private String apartmentID;
    private int buildingID;
    private int floors;
    private double area;
    private int bedRoom;
    private String status;
    private double maintanceFee;


//    public double calculateMaintanceFee(){
//
//    }
//
//    public List<Resident> getResident(){
//
//    }
//    public List<Bills> getBills(){
//
//    }
}
