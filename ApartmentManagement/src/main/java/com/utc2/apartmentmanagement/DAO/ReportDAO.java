package com.utc2.apartmentmanagement.DAO;

import com.utc2.apartmentmanagement.Model.Report;
import com.utc2.apartmentmanagement.Repository.IReportDAO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportDAO implements IReportDAO {
    private Connection connection;

    public ReportDAO() {
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Report> getAllReports() {
        List<Report> reports = new ArrayList<>();
        String query = "SELECT * FROM report ORDER BY created_at DESC";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Report report = mapResultSetToReport(resultSet);
                reports.add(report);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reports;
    }

    @Override
    public Report getReportById(int id) {
        String query = "SELECT * FROM reports WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToReport(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean saveReport(Report report) {
        String query = "INSERT INTO report (report_type, generation_date, generated_by_user_id, parameters, file_path, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, report.getReportType());
            statement.setDate(2, report.getGenerationDate() != null ?
                    Date.valueOf(report.getGenerationDate()) : null);
            statement.setInt(3, report.getGeneratedByUserId());
            statement.setString(4, report.getParameters());
            statement.setString(5, report.getFilePath());
            statement.setDate(6, report.getCreatedAt() != null ?
                    Date.valueOf(report.getCreatedAt()) : null);
            statement.setDate(7, report.getUpdatedAt() != null ?
                    Date.valueOf(report.getUpdatedAt()) : null);

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        report.setId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean updateReport(Report report) {
        String query = "UPDATE reports SET report_type = ?, generation_date = ?, generated_by_user_id = ?, " +
                "parameters = ?, file_path = ?, updated_at = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, report.getReportType());
            statement.setDate(2, report.getGenerationDate() != null ?
                    Date.valueOf(report.getGenerationDate()) : null);
            statement.setInt(3, report.getGeneratedByUserId());
            statement.setString(4, report.getParameters());
            statement.setString(5, report.getFilePath());
            statement.setDate(6, report.getUpdatedAt() != null ?
                    Date.valueOf(report.getUpdatedAt()) : null);
            statement.setInt(7, report.getId());

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean deleteReport(int id) {
        String query = "DELETE FROM reports WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public List<Report> getReportsByDateRange(LocalDate fromDate, LocalDate toDate) {
        List<Report> reports = new ArrayList<>();
        String query = "SELECT * FROM reports WHERE generation_date BETWEEN ? AND ? ORDER BY generation_date DESC";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDate(1, Date.valueOf(fromDate));
            statement.setDate(2, Date.valueOf(toDate));

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Report report = mapResultSetToReport(resultSet);
                    reports.add(report);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reports;
    }

    @Override
    public List<Report> getReportsByType(String reportType) {
        List<Report> reports = new ArrayList<>();
        String query = "SELECT * FROM reports WHERE report_type = ? ORDER BY generation_date DESC";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, reportType);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Report report = mapResultSetToReport(resultSet);
                    reports.add(report);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reports;
    }

    @Override
    public List<Report> getReportsByUser(int userId) {
        List<Report> reports = new ArrayList<>();
        String query = "SELECT * FROM reports WHERE generated_by_user_id = ? ORDER BY generation_date DESC";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Report report = mapResultSetToReport(resultSet);
                    reports.add(report);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reports;
    }

    @Override
    public Report mapResultSetToReport(ResultSet resultSet) throws SQLException {
        Report report = new Report();
        report.setId(resultSet.getInt("report_id"));
        report.setReportType(resultSet.getString("report_type"));

        Date generationDate = resultSet.getDate("generation_date");
        if (generationDate != null) {
            report.setGenerationDate(generationDate.toLocalDate());
        }

        report.setGeneratedByUserId(resultSet.getInt("generated_by_user_id"));
        report.setParameters(resultSet.getString("parameters"));
        report.setFilePath(resultSet.getString("file_path"));

        Date createdAt = resultSet.getDate("created_at");
        if (createdAt != null) {
            report.setCreatedAt(createdAt.toLocalDate());
        }

        Date updatedAt = resultSet.getDate("updated_at");
        if (updatedAt != null) {
            report.setUpdatedAt(updatedAt.toLocalDate());
        }

        return report;
    }

    @Override
    public ObservableList<PieChart.Data> PieChart(LocalDate fromDate, LocalDate toDate) throws SQLException {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        // Kiểm tra tổng số bản ghi trước để tránh lỗi chia cho 0
        String countSql = "SELECT COUNT(*) FROM Payment WHERE payment_date BETWEEN ? AND ?";
        long totalRecords = 0;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement countStmt = connection.prepareStatement(countSql)) {
            countStmt.setDate(1, Date.valueOf(fromDate));
            countStmt.setDate(2, Date.valueOf(toDate));

            ResultSet countRs = countStmt.executeQuery();
            if (countRs.next()) {
                totalRecords = countRs.getLong(1);
            }

            // Nếu không có dữ liệu, trả về danh sách trống
            if (totalRecords == 0) {
                pieChartData.add(new PieChart.Data("Không có dữ liệu", 100));
                return pieChartData;
            }

            // Truy vấn chính
            String sql = "WITH StatusCounts AS (\n" +
                    "    SELECT \n" +
                    "        status, \n" +
                    "        COUNT(*) AS count_status\n" +
                    "    FROM Payment\n" +
                    "    WHERE payment_date BETWEEN ? AND ?\n" +
                    "    GROUP BY status\n" +
                    ")\n" +
                    "SELECT \n" +
                    "    status,\n" +
                    "    (count_status * 100.0 / (SELECT COUNT(*) FROM Payment WHERE payment_date BETWEEN ? AND ?)) AS percentage\n" +
                    "FROM StatusCounts;";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setDate(1, Date.valueOf(fromDate));
                preparedStatement.setDate(2, Date.valueOf(toDate));
                preparedStatement.setDate(3, Date.valueOf(fromDate));
                preparedStatement.setDate(4, Date.valueOf(toDate));
                ResultSet resultSet = preparedStatement.executeQuery();

                // Định dạng DecimalFormat để làm tròn số
                DecimalFormat df = new DecimalFormat("#.##");

                // Lặp qua kết quả trả về và thêm vào danh sách PieChart.Data
                while (resultSet.next()) {
                    String status = resultSet.getString("status");
                    double percentage = resultSet.getDouble("percentage");

                    // Định dạng tên hiển thị có thêm phần trăm
                    String displayName = status + " (" + df.format(percentage) + "%)";

                    // Thêm dữ liệu vào pieChartData
                    pieChartData.add(new PieChart.Data(displayName, percentage));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Lỗi khi lấy dữ liệu cho biểu đồ tròn", e);
        }

        return pieChartData;
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

    public static void main(String[] args) {
//        Platform.startup(() -> {
//            try {
//                // Tạo đối tượng DAO
//                ReportDAO dao = new ReportDAO();
//
//                // Gọi phương thức với khoảng ngày cụ thể
//                LocalDate fromDate = LocalDate.of(2023, 1, 1);
//                LocalDate toDate = LocalDate.of(2024, 3, 31);
//                ObservableList<PieChart.Data> result = new ReportDAO().PieChart(fromDate, toDate);
//
//                // In kết quả ra console
//                System.out.println("Kết quả PieChart:");
//                for (PieChart.Data data : result) {
//                    System.out.println("Label: " + data.getName() + ", Value: " + data.getPieValue());
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//
//            // Kết thúc chương trình JavaFX
//            Platform.exit();
//        });

        List<Report> reports = new ReportDAO().getAllReports();
        if(reports.isEmpty()){
            System.out.println("Không có bản ghi");
        }
        for(Report report : reports){
            System.out.println(report.getReportType());
        }
    }
}
