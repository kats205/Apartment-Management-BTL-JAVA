package org.example.apartmentmanagement.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.management.ConstructorParameters;
import java.beans.ConstructorProperties;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private Date createdAt;
    private Date updatedAt;

    public List<Apartment> getAvailbleApartment(List<Apartment> apartmentList){
        List<Apartment> availbleApartments = new ArrayList<>();
        for (int i = 0; i < apartmentList.size(); i++){
            if (apartmentList.get(i).getStatus().equals("available")){
                availbleApartments.add(apartmentList.get(i));
            }
        }
        return availbleApartments;
    }
//    public List<Apartment> getOccupiedApartments(){
//
//    }
//
//    public Apartment getApartmentByID(String id){
//
//    }
}