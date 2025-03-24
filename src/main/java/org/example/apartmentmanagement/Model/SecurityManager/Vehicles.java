package org.example.apartmentmanagement.Model.SecurityManager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.apartmentmanagement.Model.ResidentManager.Resident;

import java.sql.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vehicles {
    private int vehicleID;
    private int residentID;
    private String licensePlate;
    private String vehicleType;
    private String parkingSlot;
    private Date registrationDate;
    private boolean isActive;



//    public Resident getResident(){
//
//    }
//    public boolean assignParkingSlot(String slot){
//
//    }

}
