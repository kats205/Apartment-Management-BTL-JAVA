package org.example.apartmentmanagement.Model.SecurityManager;

import java.sql.Date;

public class Visitor {
    private int visitorID;
    private String apartmentID;
    private String fullName;
    private String identityCard;
    private String phoneNumber;
    private Date visitDate;
    private Date entranceTime;
    private Date exitTime;
    private String purpose;
    private boolean approved;

    public Visitor() {
        visitorID = 0;
        apartmentID = fullName = identityCard = phoneNumber = purpose = "";
        visitDate = entranceTime = exitTime = null;
        approved = false;
    }

    public Visitor(int visitorID, String apartmentID, String fullName, String identityCard,
                   String phoneNumber, Date visitDate, Date entranceTime, Date exitTime, String purpose, boolean approved) {
        this.visitorID = visitorID;
        this.apartmentID = apartmentID;
        this.fullName = fullName;
        this.identityCard = identityCard;
        this.phoneNumber = phoneNumber;
        this.visitDate = visitDate;
        this.entranceTime = entranceTime;
        this.exitTime = exitTime;
        this.purpose = purpose;
        this.approved = approved;
    }

    public int getVisitorID() {
        return visitorID;
    }

    public void setVisitorID(int visitorID) {
        this.visitorID = visitorID;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(Date visitDate) {
        this.visitDate = visitDate;
    }

    public Date getEntranceTime() {
        return entranceTime;
    }

    public void setEntranceTime(Date entranceTime) {
        this.entranceTime = entranceTime;
    }

    public Date getExitTime() {
        return exitTime;
    }

    public void setExitTime(Date exitTime) {
        this.exitTime = exitTime;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
