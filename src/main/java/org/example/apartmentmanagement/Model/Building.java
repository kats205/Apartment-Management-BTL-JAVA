package org.example.apartmentmanagement.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Building {
    private int buildingID;
    private String buildingName;
    private String address;
    private int totalFloors;
    private int totalApartment;
    private Date completionDate;
    private Date created_at;
    private Date updated_at;


//    public List<Apartment> getAvailbleApartment(){
//
//    }
//    public List<Apartment> getOccupiedApartments(){
//
//    }
//
//    public Apartment getApartmentByID(String id){
//
//    }
}
