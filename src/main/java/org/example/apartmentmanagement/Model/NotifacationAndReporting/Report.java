package org.example.apartmentmanagement.Model.NotifacationAndReporting;

import java.io.File;
import java.sql.Date;

public class Report {
    private int reportID;
    private String reportType;
    private Date generationDate;
    private int genaratedByUserID;
    private String parameter;

    public Report() {
        reportID = genaratedByUserID = 0;
        reportType = parameter = "";
        generationDate = null;
    }

    public Report(int reportID, String reportType, Date generationDate, int genaratedByUserID, String parameter) {
        this.reportID = reportID;
        this.reportType = reportType;
        this.generationDate = generationDate;
        this.genaratedByUserID = genaratedByUserID;
        this.parameter = parameter;
    }

    public int getReportID() {
        return reportID;
    }

    public void setReportID(int reportID) {
        this.reportID = reportID;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public Date getGenerationDate() {
        return generationDate;
    }

    public void setGenerationDate(Date generationDate) {
        this.generationDate = generationDate;
    }

    public int getGenaratedByUserID() {
        return genaratedByUserID;
    }

    public void setGenaratedByUserID(int genaratedByUserID) {
        this.genaratedByUserID = genaratedByUserID;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public File generateReport(){

    }
    public File genarateFinancialReport(Date from, Date to){

    }
    public File generateOccupancyReport(){

    }

    public File generateMaintainceReport(){

    }
}
