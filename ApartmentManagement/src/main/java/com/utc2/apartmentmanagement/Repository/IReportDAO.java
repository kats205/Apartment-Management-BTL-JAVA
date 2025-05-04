package com.utc2.apartmentmanagement.Repository;

import com.utc2.apartmentmanagement.Model.Report;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IReportDAO {
    List<Report> getAllReports();
    Report getReportById(int id);
    boolean saveReport(Report report);
    boolean updateReport(Report report);
    boolean deleteReport(int id);
    List<Report> getReportsByDateRange(LocalDate fromDate, LocalDate toDate);
    List<Report> getReportsByType(String reportType);
    List<Report> getReportsByUser(int userId);
    Report mapResultSetToReport(ResultSet resultSet) throws SQLException;
    ObservableList<PieChart.Data> PieChart(LocalDate fromDate, LocalDate toDate) throws SQLException;
    List<Map<String, Object>> getAllReportsWithNamesWhoMakeReport();
}
