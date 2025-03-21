package org.example.apartmentmanagement.Model.SecurityManager;

import org.example.apartmentmanagement.Model.ResidentManager.Resident;

import java.sql.Date;

public class Vehicles {
    private int vehicleID;
    private int residentID;
    private String licensePlate;
    private String vehicleType;
    private String parkingSlot;
    private Date registrationDate;
    private boolean isActive;

    public Vehicles() {
        vehicleID = residentID = 0;
        licensePlate = vehicleType = parkingSlot = "";
        registrationDate = null;
        isActive = false;
    }

    public int getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(int vehicleID) {
        this.vehicleID = vehicleID;
    }

    public int getResidentID() {
        return residentID;
    }

    public void setResidentID(int residentID) {
        this.residentID = residentID;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getParkingSlot() {
        return parkingSlot;
    }

    public void setParkingSlot(String parkingSlot) {
        this.parkingSlot = parkingSlot;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Resident getResident(){

    }
    public boolean assignParkingSlot(String slot){

    }

}
