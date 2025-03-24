package org.example.apartmentmanagement.Model.ServiceManager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
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
