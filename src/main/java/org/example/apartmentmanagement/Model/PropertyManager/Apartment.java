package org.example.apartmentmanagement.Model.PropertyManager;

public class Apartment {
    private String apartmentID;
    private int buildingID;
    private int floors;
    private double area;
    private int bedRoom;
    private String status;
    private double maintanceFee;

    public Apartment() {
        apartmentID = status = "";
        buildingID = floors =bedRoom = 0;
        area = maintanceFee = 0;
    }

    public Apartment(String apartmentID, int buildingID, int floors, double area, int bedRoom, String status, double maintanceFee) {
        this.apartmentID = apartmentID;
        this.buildingID = buildingID;
        this.floors = floors;
        this.area = area;
        this.bedRoom = bedRoom;
        this.status = status;
        this.maintanceFee = maintanceFee;
    }

    public String getApartmentID() {
        return apartmentID;
    }

    public void setApartmentID(String apartmentID) {
        this.apartmentID = apartmentID;
    }

    public int getBuildingID() {
        return buildingID;
    }

    public void setBuildingID(int buildingID) {
        this.buildingID = buildingID;
    }

    public int getFloors() {
        return floors;
    }

    public void setFloors(int floors) {
        this.floors = floors;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public int getBedRoom() {
        return bedRoom;
    }

    public void setBedRoom(int bedRoom) {
        this.bedRoom = bedRoom;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getMaintanceFee() {
        return maintanceFee;
    }

    public void setMaintanceFee(double maintanceFee) {
        this.maintanceFee = maintanceFee;
    }
    public double calculateMaintanceFee(){

    }

    public List<Resident> getResident(){

    }
    public List<bills> getBills(){

    }
}
