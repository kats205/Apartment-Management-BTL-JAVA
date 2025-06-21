package com.utc2.apartmentmanagement.Model.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Service {
    private int serviceID;
    private String serviceName;
    private String description;
    private double price;
    private String unit;
    private boolean isAvailable;



//    public boolean register(String apartmentID){
//
//    }
//    public boolean cancel(String apartmentID){
//
//    }
//    public List<String> getRegisterApartment(){
//
//    }
}
