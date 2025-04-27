package com.utc2.apartmentmanagement.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    private int reportID;
    private String reportType;
    private Date generationDate;
    private int genaratedByUserID;
    private String parameter;

//    public File generateReport(){
//
//    }
//    public File genarateFinancialReport(Date from, Date to){
//
//    }
//    public File generateOccupancyReport(){
//
//    }
//
//    public File generateMaintainceReport(){
//
//    }
}
