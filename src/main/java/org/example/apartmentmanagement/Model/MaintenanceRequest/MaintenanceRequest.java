package org.example.apartmentmanagement.Model.MaintenanceRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.apartmentmanagement.Model.PropertyManager.Apartment;
import org.example.apartmentmanagement.Model.ResidentManager.Resident;

import java.sql.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceRequest {
    private int requestID;
    private String apartmentID;
    private int residentID;
    private Date requestDate;
    private String description;
    private String status;
    private String priority;
    private int assignedStaffID;
    private Date completionDate;


//    public void assignedTotalStaff(int staffID){
//
//    }
//    public void updateStatus(String status){
//
//    }
//    public Apartment getApartment(){
//
//    }
//    public Resident getResident(){
//
//    }
}
