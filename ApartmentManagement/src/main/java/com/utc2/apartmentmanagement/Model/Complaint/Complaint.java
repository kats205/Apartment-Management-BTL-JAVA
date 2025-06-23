package com.utc2.apartmentmanagement.Model.Complaint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Complaint {
    private int requestID;
    private String typeComplaint;
    private String apartmentID;
    private int residentID;
    private String description;
    private String status;
    private String priority;
    private int assignedStaffID;
    private Date requestDate;
}
