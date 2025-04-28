package com.utc2.apartmentmanagement.Controller;

import com.utc2.apartmentmanagement.Model.Report;
import com.utc2.apartmentmanagement.Model.User;
import com.utc2.apartmentmanagement.Service.ReportService;
import com.utc2.apartmentmanagement.Service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private TableView<Report> reportTable;

    @FXML
    private TableColumn<Report, String> reportTypeColumn;

    @FXML
    private TableColumn<Report, LocalDate> generationDateColumn;

    @FXML
    private TableColumn<Report, Integer> generatedByUserIdColumn;

    @FXML
    private TableColumn<Report, String> parametersColumn;

    @FXML
    private TableColumn<Report, String> filePathColumn;

    @FXML
    private TableColumn<Report, LocalDate> createdAtColumn;

    @FXML
    private TableColumn<Report, LocalDate> updatedAtColumn;

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

    private ReportService reportService;
    private UserService userService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Khởi tạo các dịch vụ
        reportService = new ReportService();
        userService = new UserService();

        // Thiết lập các giá trị mặc định
        fromDatePicker.setValue(LocalDate.now().minusMonths(1));
        toDatePicker.setValue(LocalDate.now());

        // Khởi tạo loại báo cáo
        initReportTypes();

        // Khởi tạo các cột trong bảng
        initTableColumns();

        // Thiết lập sự kiện cho các nút
        setupButtonActions();

        // Tải dữ liệu báo cáo
        loadReportData();
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

    // Khởi tạo các cột trong bảng
    private void initTableColumns() {
        reportTypeColumn.setCellValueFactory(new PropertyValueFactory<>("reportType"));
        generationDateColumn.setCellValueFactory(new PropertyValueFactory<>("generationDate"));

        // Hiển thị tên người dùng thay vì ID
        generatedByUserIdColumn.setCellValueFactory(new PropertyValueFactory<>("generatedByUserId"));
        generatedByUserIdColumn.setCellFactory(column -> new TableCell<Report, Integer>() {
            @Override
            protected void updateItem(Integer userId, boolean empty) {
                super.updateItem(userId, empty);
                if (empty || userId == null) {
                    setText(null);
                } else {
                    User user = userService.getUserById(userId);
                    setText(user != null ? user.getFullName() : String.valueOf(userId));
                }
            }
        });

        parametersColumn.setCellValueFactory(new PropertyValueFactory<>("parameters"));
        filePathColumn.setCellValueFactory(new PropertyValueFactory<>("filePath"));
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        updatedAtColumn.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));

        // Định dạng hiển thị ngày tháng
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        generationDateColumn.setCellFactory(column -> new TableCell<Report, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(date.format(dateFormatter));
                }
            }
        });

        createdAtColumn.setCellFactory(column -> new TableCell<Report, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(date.format(dateFormatter));
                }
            }
        });

        updatedAtColumn.setCellFactory(column -> new TableCell<Report, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(date.format(dateFormatter));
                }
            }
        });
    }

    // Thiết lập sự kiện cho các nút
    private void setupButtonActions() {
        generateButton.setOnAction(this::handleGenerateReport);
        exportReportButton.setOnAction(this::handleExportReport);
        printButton.setOnAction(this::handlePrintReport);
        exportButton.setOnAction(this::handleExportExcel);
        closeButton.setOnAction(this::handleClose);
    }

    // Tải dữ liệu báo cáo
    private void loadReportData() {
        List<Report> reports = reportService.getAllReports();
        reportTable.setItems(FXCollections.observableArrayList(reports));

        updateSummaryInfo(reports);
        updateCharts(reports);
    }

    // Cập nhật thông tin tóm tắt
    private void updateSummaryInfo(List<Report> reports) {
        if (reports.isEmpty()) {
            totalReportsLabel.setText("0");
            latestReportLabel.setText("--");
            popularReportTypeLabel.setText("--");
            topGeneratorLabel.setText("--");
            return;
        }

        // Tổng số báo cáo
        totalReportsLabel.setText(String.valueOf(reports.size()));

        // Báo cáo mới nhất
        Report latestReport = reports.stream()
                .sorted((r1, r2) -> r2.getCreatedAt().compareTo(r1.getCreatedAt()))
                .findFirst()
                .orElse(null);
        if (latestReport != null) {
            latestReportLabel.setText(latestReport.getReportType() + " (" +
                    latestReport.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ")");
        }

        // Loại báo cáo phổ biến
        Map<String, Integer> reportTypeCounts = new HashMap<>();
        for (Report report : reports) {
            reportTypeCounts.put(report.getReportType(), reportTypeCounts.getOrDefault(report.getReportType(), 0) + 1);
        }

        String popularType = reportTypeCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("--");
        popularReportTypeLabel.setText(popularType);

        // Người tạo nhiều nhất
        Map<Integer, Integer> userCounts = new HashMap<>();
        for (Report report : reports) {
            userCounts.put(report.getGeneratedByUserId(), userCounts.getOrDefault(report.getGeneratedByUserId(), 0) + 1);
        }

        Integer topUserId = userCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        if (topUserId != null) {
            User topUser = userService.getUserById(topUserId);
            topGeneratorLabel.setText(topUser != null ? topUser.getFullName() : String.valueOf(topUserId));
        }
    }

    // Cập nhật biểu đồ
    private void updateCharts(List<Report> reports) {
        // Đây chỉ là mẫu dữ liệu để minh họa biểu đồ
        // Trong thực tế, bạn sẽ cần tính toán dữ liệu thực từ báo cáo

        // Biểu đồ doanh thu theo tháng
        revenueChart.getData().clear();

        // Biểu đồ trạng thái
        statusChart.getData().clear();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Đã tạo", reports.size()),
                new PieChart.Data("Đã xuất", reports.stream().filter(r -> r.getFilePath() != null).count()),
                new PieChart.Data("Đã in", reports.size() / 3)  // Giả sử 1/3 số báo cáo đã được in
        );
        statusChart.setData(pieChartData);
    }

    // Xử lý sự kiện tạo báo cáo
    private void handleGenerateReport(ActionEvent event) {
        String reportType = reportTypeComboBox.getValue();
        LocalDate fromDate = fromDatePicker.getValue();
        LocalDate toDate = toDatePicker.getValue();

        if (reportType == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo",
                    "Vui lòng chọn loại báo cáo!");
            return;
        }

        if (fromDate == null || toDate == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo",
                    "Vui lòng chọn khoảng thời gian cho báo cáo!");
            return;
        }

        if (fromDate.isAfter(toDate)) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo",
                    "Ngày bắt đầu không thể sau ngày kết thúc!");
            return;
        }

        // Tạo tham số cho báo cáo
        String parameters = "Từ ngày: " + fromDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                + ", Đến ngày: " + toDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // Tạo báo cáo mới (giả sử có một người dùng đang đăng nhập với ID là 1)
        Report newReport = new Report();
        newReport.setReportType(reportType);
        newReport.setGenerationDate(LocalDate.now());
        newReport.setGeneratedByUserId(1);  // ID người dùng đang đăng nhập
        newReport.setParameters(parameters);
        newReport.setCreatedAt(LocalDate.now());
        newReport.setUpdatedAt(LocalDate.now());

        // Lưu báo cáo mới
        reportService.saveReport(newReport);

        // Tải lại dữ liệu
        loadReportData();

        showAlert(Alert.AlertType.INFORMATION, "Thông báo",
                "Báo cáo đã được tạo thành công!");
    }

    // Xử lý sự kiện xuất báo cáo
    private void handleExportReport(ActionEvent event) {
        Report selectedReport = reportTable.getSelectionModel().getSelectedItem();
        if (selectedReport == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo",
                    "Vui lòng chọn một báo cáo để xuất!");
            return;
        }

        // Giả sử đường dẫn tệp báo cáo
        String filePath = "reports/" + selectedReport.getReportType().replaceAll("\\s+", "_")
                + "_" + selectedReport.getGenerationDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + ".pdf";

        // Cập nhật đường dẫn tệp cho báo cáo
        selectedReport.setFilePath(filePath);
        selectedReport.setUpdatedAt(LocalDate.now());

        // Lưu cập nhật
        reportService.updateReport(selectedReport);

        // Tải lại dữ liệu
        loadReportData();

        showAlert(Alert.AlertType.INFORMATION, "Thông báo",
                "Báo cáo đã được xuất thành công!\nĐường dẫn: " + filePath);
    }

    // Xử lý sự kiện in báo cáo
    private void handlePrintReport(ActionEvent event) {
        Report selectedReport = reportTable.getSelectionModel().getSelectedItem();
        if (selectedReport == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo",
                    "Vui lòng chọn một báo cáo để in!");
            return;
        }

        showAlert(Alert.AlertType.INFORMATION, "Thông báo",
                "Báo cáo đang được gửi đến máy in...");
    }

    // Xử lý sự kiện xuất Excel
    private void handleExportExcel(ActionEvent event) {
        Report selectedReport = reportTable.getSelectionModel().getSelectedItem();
        if (selectedReport == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo",
                    "Vui lòng chọn một báo cáo để xuất Excel!");
            return;
        }

        // Giả sử đường dẫn tệp Excel
        String filePath = "reports/" + selectedReport.getReportType().replaceAll("\\s+", "_")
                + "_" + selectedReport.getGenerationDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + ".xlsx";

        showAlert(Alert.AlertType.INFORMATION, "Thông báo",
                "Báo cáo đã được xuất sang Excel thành công!\nĐường dẫn: " + filePath);
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
}