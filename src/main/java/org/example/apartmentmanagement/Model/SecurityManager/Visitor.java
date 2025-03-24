package org.example.apartmentmanagement.Model.SecurityManager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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


}
