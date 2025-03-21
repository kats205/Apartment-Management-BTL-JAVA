package org.example.apartmentmanagement.Model.ResidentManager;

import org.example.apartmentmanagement.Model.PropertyManager.Apartment;

import java.sql.Date;

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

    public Resident() {
        residentID = 0;
        apartmentID = fullName = identityCard = gender = phoneNumber = email = "";
        dateOfBirth = moveInDate = null;
        isPrimaryResident = false;
    }

    public Resident(int residentID, String apartmentID, String fullName, String identityCard, Date dateOfBirth,
                    String gender, String phoneNumber, String email, boolean isPrimaryResident, Date moveInDate) {
        this.residentID = residentID;
        this.apartmentID = apartmentID;
        this.fullName = fullName;
        this.identityCard = identityCard;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.isPrimaryResident = isPrimaryResident;
        this.moveInDate = moveInDate;
    }

    public int getResidentID() {
        return residentID;
    }

    public void setResidentID(int residentID) {
        this.residentID = residentID;
    }

    public String getApartmentID() {
        return apartmentID;
    }

    public void setApartmentID(String apartmentID) {
        this.apartmentID = apartmentID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isPrimaryResident() {
        return isPrimaryResident;
    }

    public void setPrimaryResident(boolean primaryResident) {
        isPrimaryResident = primaryResident;
    }

    public Date getMoveInDate() {
        return moveInDate;
    }

    public void setMoveInDate(Date moveInDate) {
        this.moveInDate = moveInDate;
    }

    public void updateProfile(){

    }
    public Apartment getApartment(){

    }

    public List<Vehicles> getVehiclesList(){

    }
}
