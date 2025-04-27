package com.utc2.apartmentmanagement.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ReportViewController implements Initializable {

    @FXML
    private ComboBox<?> reportTypeComboBox;

    @FXML
    private DatePicker fromDatePicker;

    @FXML
    private DatePicker toDatePicker;

    @FXML
    private Button generateButton;

    @FXML
    private Button exportReportButton;

    @FXML
    private BarChart<?, ?> revenueChart;

    @FXML
    private PieChart statusChart;

    @FXML
    private TableView<?> reportTable;

    @FXML
    private TableColumn<?, ?> periodColumn;

    @FXML
    private TableColumn<?, ?> totalInvoicesColumn;

    @FXML
    private TableColumn<?, ?> paidInvoicesColumn;

    @FXML
    private TableColumn<?, ?> unpaidInvoicesColumn;

    @FXML
    private TableColumn<?, ?> overdueInvoicesColumn;

    @FXML
    private TableColumn<?, ?> totalRevenueColumn;

    @FXML
    private TableColumn<?, ?> totalFeesColumn;

    @FXML
    private Label totalInvoicesLabel;

    @FXML
    private Label totalRevenueLabel;

    @FXML
    private Label paymentRateLabel;

    @FXML
    private Label overdueRateLabel;

    @FXML
    private Button printButton;

    @FXML
    private Button exportButton;

    @FXML
    private Button closeButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Khởi tạo các thành phần UI
        initializeComponents();

        // Thiết lập các sự kiện
        setupEventHandlers();

        // Tải dữ liệu mặc định
        loadDefaultData();
    }

    private void initializeComponents() {
        // Khởi tạo DatePicker
        initializeDatePickers();

        // Khởi tạo ComboBox
        initializeComboBoxes();

        // Khởi tạo TableView
        initializeTableView();

        // Khởi tạo biểu đồ
        initializeCharts();
    }

    private void initializeDatePickers() {
        // Thiết lập giá trị mặc định: từ đầu tháng đến hiện tại
        LocalDate now = LocalDate.now();
        LocalDate firstDayOfMonth = LocalDate.of(now.getYear(), now.getMonth(), 1);

        fromDatePicker.setValue(firstDayOfMonth);
        toDatePicker.setValue(now);
    }

    private void initializeComboBoxes() {
        // TODO: Khởi tạo các loại báo cáo cho reportTypeComboBox
        // Ví dụ: Báo cáo theo tháng, theo quý, theo năm
    }

    private void initializeTableView() {
        // TODO: Cấu hình TableView và các cột
    }

    private void initializeCharts() {
        // TODO: Cấu hình biểu đồ bar chart và pie chart
    }

    private void setupEventHandlers() {
        // Xử lý sự kiện tạo báo cáo
        generateButton.setOnAction(event -> generateReport());

        // Xử lý sự kiện xuất báo cáo
        exportReportButton.setOnAction(event -> exportFullReport());

        // Xử lý sự kiện in báo cáo
        printButton.setOnAction(event -> printReport());

        // Xử lý sự kiện xuất Excel
        exportButton.setOnAction(event -> exportToExcel());

        // Nút đóng mặc định không làm gì - sẽ được xử lý bởi DashboardController
    }

    private void loadDefaultData() {
        // TODO: Tải dữ liệu báo cáo mặc định

        // Hiển thị dữ liệu tóm tắt
        updateSummaryData();
    }

    private void generateReport() {
        // TODO: Tạo báo cáo dựa trên loại báo cáo và khoảng thời gian đã chọn

        // Cập nhật biểu đồ
        updateCharts();

        // Cập nhật bảng dữ liệu
        updateTableData();

        // Cập nhật dữ liệu tóm tắt
        updateSummaryData();
    }

    private void updateCharts() {
        // TODO: Cập nhật dữ liệu cho biểu đồ doanh thu và biểu đồ trạng thái
    }

    private void updateTableData() {
        // TODO: Cập nhật dữ liệu cho bảng báo cáo
    }

    private void updateSummaryData() {
        // TODO: Cập nhật các thông tin tóm tắt
        // Giả sử các giá trị mặc định
        totalInvoicesLabel.setText("0");
        totalRevenueLabel.setText("0 VNĐ");
        paymentRateLabel.setText("0%");
        overdueRateLabel.setText("0%");
    }

    private void exportFullReport() {
        // TODO: Xuất toàn bộ báo cáo (bao gồm biểu đồ, bảng dữ liệu và tóm tắt)
    }

    private void printReport() {
        // TODO: In báo cáo
    }

    private void exportToExcel() {
        // TODO: Xuất báo cáo dạng Excel
    }

    // Getter cho nút đóng để DashboardController có thể truy cập
    public Button getCloseButton() {
        return closeButton;
    }
}