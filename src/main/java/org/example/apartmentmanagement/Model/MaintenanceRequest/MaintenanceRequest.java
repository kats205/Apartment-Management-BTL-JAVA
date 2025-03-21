package org.example.apartmentmanagement.Model.MaintenanceRequest;

import org.example.apartmentmanagement.Model.PropertyManager.Apartment;
import org.example.apartmentmanagement.Model.ResidentManager.Resident;

import java.sql.Date;

public class MaintenanceRequest {
    private int requestID;
    private String apartmentID;
    private int residentID;
    private Date requestDate;
    private String description;
    private String status;
    private String priority;
    private int assignedStaffID;
    private Date completionDate;

    public MaintenanceRequest() {
        requestID = residentID = assignedStaffID = 0;
        apartmentID = description = status = priority = "";
        requestDate = completionDate = null;
    }

    public MaintenanceRequest(int requestID, String apartmentID, int residentID, Date requestDate,
                              String description, String status, String priority, int assignedStaffID, Date completionDate) {
        this.requestID = requestID;
        this.apartmentID = apartmentID;
        this.residentID = residentID;
        this.requestDate = requestDate;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.assignedStaffID = assignedStaffID;
        this.completionDate = completionDate;
    }

    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public String getApartmentID() {
        return apartmentID;
    }

    public void setApartmentID(String apartmentID) {
        this.apartmentID = apartmentID;
    }

    public int getResidentID() {
        return residentID;
    }

    public void setResidentID(int residentID) {
        this.residentID = residentID;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public int getAssignedStaffID() {
        return assignedStaffID;
    }

    public void setAssignedStaffID(int assignedStaffID) {
        this.assignedStaffID = assignedStaffID;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }
    public void assignedTotalStaff(int staffID){

    }
    public void updateStatus(String status){

    }
    public Apartment getApartment(){

    }
    public Resident getResident(){

    }
}
