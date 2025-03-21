package org.example.apartmentmanagement.Model.ServiceManager;

import java.util.List;

public class Service {
    private int serviceID;
    private String serviceName;
    private String description;
    private double price;
    private String unit;
    private boolean isAvailable;

    public Service() {
        serviceID = 0;
        serviceName = description = unit = "";
        price = 0;
        isAvailable = false;
    }

    public Service(int serviceID, String serviceName, String description, double price, String unit, boolean isAvailable) {
        this.serviceID = serviceID;
        this.serviceName = serviceName;
        this.description = description;
        this.price = price;
        this.unit = unit;
        this.isAvailable = isAvailable;
    }

    public int getServiceID() {
        return serviceID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public boolean register(String apartmentID){

    }
    public boolean cancel(String apartmentID){

    }
    public List<String> getRegisterApartment(){

    }
}
