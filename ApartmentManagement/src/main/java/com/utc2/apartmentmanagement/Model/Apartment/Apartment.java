package com.utc2.apartmentmanagement.Model.Apartment;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
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
