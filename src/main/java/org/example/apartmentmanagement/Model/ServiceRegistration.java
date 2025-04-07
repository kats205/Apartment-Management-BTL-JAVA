package org.example.apartmentmanagement.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRegistration {
    private int serviceRegistrationID;
    private int serviceID;
    private int apartmentID;
    private Date startDate;
    private Date endDate;
    private String status;
}
