package org.example.apartmentmanagement.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private double priceApartment;
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
