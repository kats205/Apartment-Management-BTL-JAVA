package com.utc2.apartmentmanagement.DAO;

import com.utc2.apartmentmanagement.Model.Report;
import com.utc2.apartmentmanagement.Repository.IReportDAO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO implements IReportDAO {
    @Override
    public List<Report> getAllReports() {
        List<Report> reportList = new ArrayList<>();
        String sql = "SELECT * FROM Report";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                reportList.add(new Report(
                        rs.getInt("report_id"),
                        rs.getString("report_type"),
                        rs.getDate("generation_date"),
                        rs.getInt("generated_by_user_id"),
                        rs.getString("parameters")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reportList;
    }

    @Override
    public Report getReportById(int id) {
        String sql = "SELECT * FROM Report WHERE report_id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return new Report(
                        rs.getInt("report_id"),
                        rs.getString("report_type"),
                        rs.getDate("generation_date"),
                        rs.getInt("generated_by_user_id"),
                        rs.getString("parameters")
                );
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean addReport(Report report) {
        String sql = "INSERT INTO Report (report_type, generation_date, generated_by_user_id, parameters, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, report.getReportType());
            stmt.setDate(2, report.getGenerationDate());
            stmt.setInt(3, report.getGenaratedByUserID());
            stmt.setString(4, report.getParameter());
            stmt.setDate(5, Date.valueOf(LocalDate.now()));
            stmt.setDate(6, Date.valueOf(LocalDate.now()));
            return stmt.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteReport(int id) {
        String sql = "DELETE FROM Report WHERE report_id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean updateReportType(int id, String newType) {
        return updateReportField(id, "report_type", newType);
    }

    @Override
    public boolean updateGenerationDate(int id, Date newDate) {
        return updateReportField(id, "generation_date", newDate);
    }

    @Override
    public boolean updateParameter(int id, String newParameter) {
        return updateReportField(id, "parameters", newParameter);
    }
    public boolean updateReportField(int id, String field, Object newValue){
        String sql = "UPDATE Report SET " + field + " = ? , updated_at = ? WHERE report_id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){
            if(newValue instanceof Integer){
                stmt.setInt(1, (Integer) newValue);
            }
            else if(newValue instanceof String){
                stmt.setString(1, (String) newValue);
            }
            else if(newValue instanceof Date){
                stmt.setDate(1, (Date) newValue);
            }
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
            stmt.setInt(3, id);
            return stmt.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
