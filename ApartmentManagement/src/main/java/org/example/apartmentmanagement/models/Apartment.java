package org.example.apartmentmanagement.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Apartment {
    private String apartmentID;
    private int buildingID;
    private int floors;
    private double area;
    private int bedRoom;
    private String status;
    private double maintenanceFee;



//    public double calculateMaintenanceFee(){
//
//    }

//    public List<Resident> getResident(){
//
//    }
//    public List<Bills> getBills(){
//
//    }
}