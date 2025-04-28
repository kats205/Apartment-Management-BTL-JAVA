package com.utc2.apartmentmanagement.DAO;

import com.utc2.apartmentmanagement.Model.Report;
import com.utc2.apartmentmanagement.Repository.IReportDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.XYChart;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public ObservableList<Map<String, Object>> getValueReport(LocalDate fromDate, LocalDate toDate) throws SQLException{
        ObservableList<Map<String, Object>> data = FXCollections.observableArrayList();
        String sql = "SELECT \n" +
                "    FORMAT(B.billing_date, 'MM/yyyy') AS [Kỳ],\n" +
                "    COUNT(B.bill_id) AS [Tổng số hóa đơn],\n" +
                "    SUM(CASE WHEN B.status = 'paid' THEN 1 ELSE 0 END) AS [Đã thanh toán],\n" +
                "    SUM(CASE WHEN B.status = 'pending' THEN 1 ELSE 0 END) AS [Chưa thanh toán],\n" +
                "    SUM(CASE WHEN B.status = 'overdue' THEN 1 ELSE 0 END) AS [Quá hạn],\n" +
                "    SUM(B.total_amount) AS [Tổng doanh thu (VND)],\n" +
                "    SUM(B.late_fee) AS [Tổng phí phạt (VND)]\n" +
                "FROM \n" +
                "    Bill B\n" +
                "WHERE \n" +
                "    B.billing_date BETWEEN ? AND ?\n" +
                "GROUP BY \n" +
                "    FORMAT(B.billing_date, 'MM/yyyy')\n" +
                "ORDER BY \n" +
                "    FORMAT(B.billing_date, 'MM/yyyy')";
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setDate(1, Date.valueOf(fromDate));
            stmt.setDate(2, Date.valueOf(toDate));
            ResultSet rs = stmt.executeQuery();
            Map<String, Object> row = new HashMap<>();
            while(rs.next()){
                row.put("kỳ", rs.getNString("Kỳ"));
                row.put("tổng số hóa đơn", rs.getInt("Tổng số hóa đơn"));
                row.put("đã thanh toán", rs.getInt("Đã thanh toán"));
                row.put("chưa thanh toán", rs.getInt("Chưa thanh toán"));
                row.put("quá hạn", rs.getInt("Quá hạn"));
                row.put("tổng doanh thu", rs.getDouble("Tổng doanh thu (VND)"));
                row.put("tổng phí phạt", rs.getDouble("Tổng phí phạt (VND)"));
                data.add(row);
            }

        }catch(SQLException e){
            throw new SQLException("Error while generating report", e);
        }
        return data;
    }
    public ObservableList<XYChart.Data<String, Number>> getValue(LocalDate fromDate, LocalDate toDate) throws SQLException {
        String sql = "SELECT \n" +
                "    FORMAT(p.payment_date, 'yyyy-MM') AS Thang,\n" +
                "    SUM(p.amount) AS TongDoanhThu\n" +
                "FROM \n" +
                "    Payment p\n" +
                "JOIN \n" +
                "    Bill b ON p.bill_id = b.bill_id\n" +
                "WHERE \n" +
                "    p.status = 'completed' \n" +
                "    AND p.payment_date BETWEEN ? AND ? \n" +
                "GROUP BY \n" +
                "    FORMAT(p.payment_date, 'yyyy-MM')\n" +
                "ORDER BY \n" +
                "    Thang ASC";
        ObservableList<XYChart.Data<String, Number>> data = FXCollections.observableArrayList();
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setDate(1, Date.valueOf(fromDate));
            stmt.setDate(2, Date.valueOf(toDate));
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                String thang = rs.getString("Thang");
                double doanhThu = rs.getDouble("TongDoanhThu");

                data.add(new XYChart.Data<>(thang, doanhThu));
            }
        }catch(SQLException e){
            throw new SQLException("Error while generating report", e);
        }
        return data;
    }
}
