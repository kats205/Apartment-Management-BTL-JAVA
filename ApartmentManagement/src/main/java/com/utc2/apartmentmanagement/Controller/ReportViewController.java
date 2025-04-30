package com.utc2.apartmentmanagement.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ReportViewController implements Initializable {

    @FXML
    private ComboBox<String> reportTypeComboBox;

    @FXML
    private DatePicker fromDatePicker;

    @FXML
    private DatePicker toDatePicker;

    @FXML
    private Button generateButton;

    @FXML
    private Button exportReportButton;

    @FXML
    private BarChart<String, Number> revenueChart;

    @FXML
    private PieChart statusChart;

    @FXML
    private TableView reportTable;

    @FXML
    private TableColumn reportTypeColumn;

    @FXML
    private TableColumn generationDateColumn;

    @FXML
    private TableColumn generatedByUserIdColumn;

    @FXML
    private TableColumn parametersColumn;

    @FXML
    private TableColumn filePathColumn;

    @FXML
    private TableColumn createdAtColumn;

    @FXML
    private TableColumn updatedAtColumn;

    @FXML
    private Label totalReportsLabel;

    @FXML
    private Label latestReportLabel;

    @FXML
    private Label popularReportTypeLabel;

    @FXML
    private Label topGeneratorLabel;

    @FXML
    private Button printButton;

    @FXML
    private Button exportButton;

    @FXML
    private Button closeButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Thiết lập các giá trị mặc định
        fromDatePicker.setValue(LocalDate.now().minusMonths(1));
        toDatePicker.setValue(LocalDate.now());

        // Khởi tạo loại báo cáo
        initReportTypes();

        // Thiết lập sự kiện cho các nút
        setupButtonActions();

        // Tạo dữ liệu mẫu cho biểu đồ và bảng
        createSampleData();
    }

    // Khởi tạo các loại báo cáo
    private void initReportTypes() {
        ObservableList<String> reportTypes = FXCollections.observableArrayList(
                "Doanh thu theo tháng",
                "Trạng thái thanh toán",
                "Báo cáo tài chính",
                "Tình trạng căn hộ",
                "Báo cáo hoạt động"
        );
        reportTypeComboBox.setItems(reportTypes);
        reportTypeComboBox.getSelectionModel().selectFirst();
    }

    // Thiết lập sự kiện cho các nút
    private void setupButtonActions() {
        exportReportButton.setOnAction(this::handleExportReport);
        printButton.setOnAction(this::handlePrintReport);
        exportButton.setOnAction(this::handleExportExcel);
        closeButton.setOnAction(this::handleClose);

        // Sự kiện khi thay đổi loại báo cáo
        reportTypeComboBox.setOnAction(event -> createSampleData());

        // Sự kiện khi thay đổi ngày
        fromDatePicker.setOnAction(event -> createSampleData());
        toDatePicker.setOnAction(event -> createSampleData());
    }

    // Tạo dữ liệu mẫu cho biểu đồ và bảng
    private void createSampleData() {
        createSampleCharts();
        updateSummaryInfo();
    }

    // Tạo biểu đồ mẫu
    private void createSampleCharts() {
        // Biểu đồ doanh thu theo tháng
        revenueChart.getData().clear();

        // Biểu đồ trạng thái
        statusChart.getData().clear();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Chờ thanh toán", 25),
                new PieChart.Data("Đã thanh toán", 45),
                new PieChart.Data("Quá hạn", 15),
                new PieChart.Data("Đã hủy", 15)
        );
        statusChart.setData(pieChartData);
    }

    // Cập nhật thông tin tóm tắt
    private void updateSummaryInfo() {
        totalReportsLabel.setText("15");
        latestReportLabel.setText("Báo cáo tài chính (01/05/2025)");
        popularReportTypeLabel.setText("Doanh thu theo tháng");
        topGeneratorLabel.setText("Trần Văn Quản");
    }

    // Xử lý sự kiện xuất báo cáo
    private void handleExportReport(ActionEvent event) {
        String reportType = reportTypeComboBox.getValue();
        LocalDate fromDate = fromDatePicker.getValue();
        LocalDate toDate = toDatePicker.getValue();

        showAlert(Alert.AlertType.INFORMATION, "Thông báo",
                "Đang xuất báo cáo: " + reportType + "\n" +
                        "Từ ngày: " + fromDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n" +
                        "Đến ngày: " + toDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    // Xử lý sự kiện in báo cáo
    private void handlePrintReport(ActionEvent event) {
        showAlert(Alert.AlertType.INFORMATION, "Thông báo",
                "Báo cáo đang được gửi đến máy in...");
    }

    // Xử lý sự kiện xuất Excel
    private void handleExportExcel(ActionEvent event) {
        showAlert(Alert.AlertType.INFORMATION, "Thông báo",
                "Báo cáo đã được xuất sang Excel thành công!");
    }

    // Xử lý sự kiện đóng
    private void handleClose(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    // Hiển thị thông báo
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /*
     * Các method liên quan đến Model
     * Các phương thức này sẽ được triển khai khi có các model tương ứng
     */

    /*
    // Tải dữ liệu báo cáo
    private void loadReportData() {
        // Sẽ được triển khai khi có model Report
    }

    // Tạo báo cáo mới
    private void createNewReport(String reportType, LocalDate fromDate, LocalDate toDate) {
        // Sẽ được triển khai khi có model Report
    }

    // Cập nhật báo cáo
    private void updateReport(Report report) {
        // Sẽ được triển khai khi có model Report
    }

    // Xóa báo cáo
    private void deleteReport(Report report) {
        // Sẽ được triển khai khi có model Report
    }

    // Lấy người dùng theo ID
    private User getUserById(int userId) {
        // Sẽ được triển khai khi có model User
        return null;
    }
    */
}