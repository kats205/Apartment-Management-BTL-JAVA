package org.example.apartmentmanagement.Model.PropertyManager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.management.ConstructorParameters;
import java.beans.ConstructorProperties;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private LocalDate completionDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public List<Apartment> getAvailbleApartment(){

    }
    public List<Apartment> getOccupiedApartments(){

    }

    public Apartment getApartmentByID(String id){

    }
}
