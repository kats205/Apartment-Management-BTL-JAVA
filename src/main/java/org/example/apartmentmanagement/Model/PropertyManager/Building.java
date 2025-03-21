package org.example.apartmentmanagement.Model.PropertyManager;

import java.sql.Date;

public class Building {
    private int buildingID;
    private String buildingName;
    private String address;
    private int totalFloors;
    private int totalApartment;
    private Date completionDate;

    public Building() {
        buildingID = totalApartment = totalFloors = 0;
        buildingName = address = "";
        completionDate = null;
    }

    public Building(int buildingID, String buildingName, String address, int totalFloors, int totalApartment, Date completionDate) {
        this.buildingID = buildingID;
        this.buildingName = buildingName;
        this.address = address;
        this.totalFloors = totalFloors;
        this.totalApartment = totalApartment;
        this.completionDate = completionDate;
    }

    public int getBuildingID() {
        return buildingID;
    }

    public void setBuildingID(int buildingID) {
        this.buildingID = buildingID;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTotalFloors() {
        return totalFloors;
    }

    public void setTotalFloors(int totalFloors) {
        this.totalFloors = totalFloors;
    }

    public int getTotalApartment() {
        return totalApartment;
    }

    public void setTotalApartment(int totalApartment) {
        this.totalApartment = totalApartment;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public List<Apartment> getAvailbleApartment(){

    }
    public List<Apartment> getOccupiedApartments(){

    }

    public Apartment getApartmentByID(String id){

    }
}
