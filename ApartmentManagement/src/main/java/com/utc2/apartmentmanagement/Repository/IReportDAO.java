package com.utc2.apartmentmanagement.Repository;

import com.utc2.apartmentmanagement.Model.Report;

import java.sql.Date;
import java.util.List;

public interface IReportDAO {
    List<Report> getAllReports();
    Report getReportById(int id);
    boolean addReport(Report report);
    boolean deleteReport(int id);
    boolean updateReportType(int id, String newType);
    boolean updateGenerationDate(int id, Date newDate);
    boolean updateParameter(int id, String newParameter);

}
