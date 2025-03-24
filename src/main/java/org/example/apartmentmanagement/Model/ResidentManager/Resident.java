package org.example.apartmentmanagement.Model.ResidentManager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.apartmentmanagement.Model.PropertyManager.Apartment;
import org.example.apartmentmanagement.Model.SecurityManager.Vehicles;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Resident {
    private int residentID;
    private String apartmentID;
    private String fullName;
    private String identityCard;
    private Date dateOfBirth;
    private String gender;
    private String phoneNumber;
    private String email;
    private boolean isPrimaryResident;
    private Date moveInDate;

    public Resident(int residentID, String apartmentID, String fullName, String identityCard, Date dateOfBirth, String gender, String phoneNumber, String email){
        this.residentID = residentID;
        this.apartmentID = apartmentID;
        this.fullName = fullName;
        this.identityCard = identityCard;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

//    public void updateProfile(){
//
//    }
//    public Apartment getApartment(){
//
//    }
//
//    public List<Vehicles> getVehiclesList(){
//
//    }
}
