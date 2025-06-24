package com.utc2.apartmentmanagement.Controller.Report;

import com.utc2.apartmentmanagement.Controller.DashboardController;
import com.utc2.apartmentmanagement.DAO.Report.ReportDAO;
import com.utc2.apartmentmanagement.DAO.User.UserDAO;
import com.utc2.apartmentmanagement.Model.PDF_Export;
import com.utc2.apartmentmanagement.Model.Report.Report;
import com.utc2.apartmentmanagement.Model.User.User;
import com.utc2.apartmentmanagement.Utils.PaginationUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class ReportViewController implements Initializable {

    @FXML public TableColumn<Report, String> reportTypeColumn;
    @FXML public TableColumn<Report, LocalDate> generationDateColumn;
    @FXML public TableColumn<Report, Integer> generatedByUserIdColumn;
    @FXML public TableColumn<Report, Date> parametersColumn;
    @FXML public TableColumn<Report, String> filePathColumn;
    @FXML public TableColumn<Report, LocalDate> createdAtColumn;
    @FXML public TableColumn<Report, LocalDate> updatedAtColumn;
    @FXML public Label topGeneratorLabel;
    @FXML public Label popularReportTypeLabel;
    @FXML public Label latestReportLabel;
    @FXML public Label totalReportsLabel;
    @FXML public AnchorPane reportView;
    @FXML public Pagination pagination;
    @FXML public Tab tabDataTable;
    @FXML private ComboBox<String> reportTypeComboBox;
    @FXML private DatePicker fromDatePicker;
    @FXML private DatePicker toDatePicker;
    @FXML private Button generateButton;
    @FXML private Button exportReportButton;
    @FXML private BarChart<String, Number> revenueChart;
    @FXML private TableView<Report> reportTable;



    // Getter cho nút đóng để DashboardController có thể truy cập
    @Getter
    @FXML private Button closeButton;

    @FXML private PieChart apartmentStatusPieChart;

    private static final List<Report> REPORT_INIT = new ReportDAO().getAllReports();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Khởi tạo các thành phần UI
        initializeComponents();

        // Thiết lập các sự kiện
        setupEventHandlers();

        // Tải dữ liệu mặc định
        loadDefaultData();

        pagination.setVisible(false);
        pagination.setManaged(false);
        initTableView(REPORT_INIT);
        reportTypeFilter();
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

        initialSummary();
    }

    public void initialSummary() {
        try {
            List<Report> reportList = new ReportDAO().getAllReports();

            // Kiểm tra danh sách báo cáo có rỗng không
            if (reportList == null || reportList.isEmpty()) {
                // Thiết lập giá trị mặc định khi không có báo cáo
                totalReportsLabel.setText("0");
                latestReportLabel.setText("Không có báo cáo");
                popularReportTypeLabel.setText("Không có dữ liệu");
                topGeneratorLabel.setText("Không có dữ liệu");

                System.out.println("Không có báo cáo nào trong hệ thống");
                return;
            }

            int totalReport = reportList.size();

            // Map 1: Đếm số lần mỗi loại báo cáo
            Map<String, Integer> reportTypeCountMap = new HashMap<>();

            // Map 2: Đếm số lần mỗi user tạo báo cáo
            Map<Integer, Integer> userReportCountMap = new HashMap<>();

            for (Report report : reportList) {
                // Đếm loại báo cáo (kiểm tra null)
                String type = report.getReportType();
                if (type != null && !type.trim().isEmpty()) {
                    reportTypeCountMap.put(type, reportTypeCountMap.getOrDefault(type, 0) + 1);
                }

                // Đếm theo user (kiểm tra user ID hợp lệ)
                int userId = report.getGeneratedByUserId();
                if (userId > 0) { // Giả sử user ID phải > 0
                    userReportCountMap.put(userId, userReportCountMap.getOrDefault(userId, 0) + 1);
                }
            }

            // Tìm loại báo cáo phổ biến nhất
            String popularReport = "Không có dữ liệu";
            if (!reportTypeCountMap.isEmpty()) {
                popularReport = reportTypeCountMap.entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .map(Map.Entry::getKey)
                        .orElse("Không có dữ liệu");
            }

            // Tìm user tạo nhiều báo cáo nhất
            String topGeneratorName = "Không có dữ liệu";
            if (!userReportCountMap.isEmpty()) {
                Integer mostActiveUserId = userReportCountMap.entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .map(Map.Entry::getKey)
                        .orElse(null);

                // Kiểm tra mostActiveUserId trước khi sử dụng
                if (mostActiveUserId != null) {
                    try {
                        User user = new UserDAO().getUserByID(mostActiveUserId);
                        if (user != null && user.getFullName() != null) {
                            topGeneratorName = user.getFullName();
                        } else {
                            topGeneratorName = "User ID: " + mostActiveUserId;
                        }
                    } catch (Exception e) {
                        System.err.println("Lỗi khi lấy thông tin user ID " + mostActiveUserId + ": " + e.getMessage());
                        topGeneratorName = "User ID: " + mostActiveUserId + " (Lỗi)";
                    }
                }
            }

            // Lấy báo cáo mới nhất
            String newestReport = "Không có báo cáo";
            if (!reportList.isEmpty()) {
                Report lastReport = reportList.get(reportList.size() - 1); // Thay thế getLast()
                if (lastReport != null && lastReport.getReportType() != null &&
                        !lastReport.getReportType().trim().isEmpty()) {
                    newestReport = lastReport.getReportType();
                }
            }

            // Cập nhật UI
            totalReportsLabel.setText(String.valueOf(totalReport));
            latestReportLabel.setText(newestReport);
            popularReportTypeLabel.setText(popularReport);
            topGeneratorLabel.setText(topGeneratorName);

            // Debug output
            System.out.println("=== BÁO CÁO THỐNG KÊ ===");
            System.out.println("Tổng số báo cáo: " + totalReport);
            System.out.println("Báo cáo mới nhất: " + newestReport);
            System.out.println("Loại báo cáo phổ biến nhất: " + popularReport);
            System.out.println("Người tạo nhiều báo cáo nhất: " + topGeneratorName);
            System.out.println("========================");

        } catch (Exception e) {
            System.err.println("Lỗi trong initialSummary(): " + e.getMessage());
            e.printStackTrace();

            // Thiết lập giá trị mặc định khi có lỗi
            try {
                totalReportsLabel.setText("Lỗi");
                latestReportLabel.setText("Lỗi");
                popularReportTypeLabel.setText("Lỗi");
                topGeneratorLabel.setText("Lỗi");
            } catch (Exception uiEx) {
                System.err.println("Lỗi khi cập nhật UI: " + uiEx.getMessage());
            }
        }
    }


    private void setDefaultValues() {
        totalReportsLabel.setText("0");
        latestReportLabel.setText("Không có báo cáo");
        popularReportTypeLabel.setText("Không có dữ liệu");
        topGeneratorLabel.setText("Không có dữ liệu");
    }

    private void processReportStatistics(List<Report> validReports) {
        // Implement logic xử lý thống kê ở đây
        // Tương tự như code trên nhưng với validReports
    }

    private void handleError(String message, Exception e) {
        System.err.println(message + ": " + e.getMessage());
        e.printStackTrace();
        setDefaultValues();
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
        reportTypeComboBox.getItems().addAll("Bao cao can ho", "Bao cao tai chinh", "All Report Type");
    }

    public void         reportTypeFilter(){
        reportTypeComboBox.setOnAction(event -> {
            String reportType = reportTypeComboBox.getValue();
            System.out.println("Report type: " + reportType);
            if (reportType == null || reportType.equals("All Report Type")) {
                initTableView(REPORT_INIT); // Hiển thị toàn bộ nếu không chọn hoặc chọn "Tất cả"
                return;
            }

            List<Report> reportTypeFilter = REPORT_INIT.stream()
                    .filter(r -> reportType.equals(r.getReportType()))
                    .toList();

            for(Report r : reportTypeFilter) {
                System.out.println(r);
            }

            initTableView(reportTypeFilter);
        });
    }

    private void initializeTableView() {
        // TODO: Cấu hình TableView và các cột
        reportTypeColumn.setCellValueFactory(new PropertyValueFactory<>("reportType"));
        reportTypeColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");

        generationDateColumn.setCellValueFactory(new PropertyValueFactory<>("generationDate"));
        generationDateColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");

        generatedByUserIdColumn.setCellValueFactory(new PropertyValueFactory<>("generatedByUserId"));
        generatedByUserIdColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");

        parametersColumn.setCellValueFactory(new PropertyValueFactory<>("parameters"));
        parametersColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");

        filePathColumn.setCellValueFactory(new PropertyValueFactory<>("filePath"));
        filePathColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");

//        createdAtColumn.setCellValueFactory( new PropertyValueFactory<>("createdAt"));
//        createdAtColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");

        updatedAtColumn.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));
        updatedAtColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");

    }

    public void initTableView(List<Report> validReports) {
        initializeTableView();
        ObservableList<Report> reportList = FXCollections.observableArrayList(validReports);
        PaginationUtils.setupPagination(
                REPORT_INIT,
                reportTable,
                pagination
        );
        reportTable.setItems(reportList);
    }

    private void initializeCharts() {
        // TODO: Cấu hình biểu đồ bar chart và pie chart
        // Cấu hình biểu đồ doanh thu (Bar Chart)
        setupRevenueChart();

        // Cấu hình biểu đồ trạng thái (Pie Chart)
        setupStatusPieChart();
    }

    private void setupRevenueChart() {
        // Đặt tiêu đề cho biểu đồ
        // Cấu hình trục X (Tháng)
        CategoryAxis xAxis = (CategoryAxis) revenueChart.getXAxis();
        xAxis.setLabel("Tháng");
        xAxis.setTickLabelRotation(-45); // Xoay nhãn để tránh chồng chéo

        // Cấu hình trục Y (Doanh Thu)
        javafx.scene.chart.NumberAxis yAxis = (javafx.scene.chart.NumberAxis) revenueChart.getYAxis();
        yAxis.setLabel("Doanh Thu (VNĐ)");
        yAxis.setForceZeroInRange(true);

        // Cấu hình khoảng cách giữa các cột
        revenueChart.setCategoryGap(20);
        revenueChart.setBarGap(5);

        // Vô hiệu hóa hoạt ảnh để cải thiện hiệu suất
        revenueChart.setAnimated(false);
    }

    private void setupStatusPieChart() {

        // Cấu hình chung cho PieChart
        apartmentStatusPieChart.setLabelLineLength(10);
        apartmentStatusPieChart.setLegendVisible(true);

        // Thêm hiệu ứng tương tác
        apartmentStatusPieChart.getData().forEach(data -> {
            data.getNode().setOnMouseEntered(event -> {
                // Làm nổi bật phần được chọn
                data.getNode().setStyle("-fx-background-color: derive(" +
                        data.getNode().getStyle().replace("-fx-background-color: ", "") +
                        ", -30%);");
            });

            data.getNode().setOnMouseExited(event -> {
                // Trở về màu gốc
                data.getNode().setStyle("");
            });
        });
        try {
            LocalDate fromDate = fromDatePicker.getValue();
            LocalDate toDate = toDatePicker.getValue();
            ObservableList<PieChart.Data> pieChartData = new ReportDAO().PieChart(fromDate, toDate);

            apartmentStatusPieChart.setData(pieChartData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupEventHandlers() {
        // Xử lý sự kiện tạo báo cáo
        generateButton.setOnAction(event -> {
            try {
                generateReport();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        // Xử lý sự kiện xuất báo cáo
        exportReportButton.setOnAction(event -> exportFullReport());

    }

    private void loadDefaultData() {
        // TODO: Tải dữ liệu báo cáo mặc định

    }

    private void generateReport() throws SQLException {
        // TODO: Tạo báo cáo dựa trên loại báo cáo và khoảng thời gian đã chọn
        apartmentStatusPieChart.setLabelLineLength(10);
        apartmentStatusPieChart.setLegendVisible(true);

        try {
            LocalDate fromDate = fromDatePicker.getValue();
            LocalDate toDate = toDatePicker.getValue();
            System.out.println(fromDate + " " + toDate);
            // Kiểm tra giá trị ngày
            if (fromDate == null || toDate == null) {
                // Hiển thị thông báo lỗi
                System.out.println("Vui lòng chọn ngày bắt đầu và ngày kết thúc");
                return;
            }

            // Kiểm tra thứ tự ngày
            if (fromDate.isAfter(toDate)) {
                System.out.println("Ngày bắt đầu phải trước ngày kết thúc");
                return;
            }

            ObservableList<PieChart.Data> pieChartData = new ReportDAO().PieChart(fromDate, toDate);
            apartmentStatusPieChart.setData(pieChartData);

            // Thêm hiệu ứng tương tác sau khi đã nạp dữ liệu
            apartmentStatusPieChart.getData().forEach(data -> {
                String originalStyle = data.getNode().getStyle(); // Lưu style ban đầu

                data.getNode().setOnMouseEntered(event -> {
                    // Làm nổi bật phần được chọn
                    data.getNode().setStyle("-fx-background-color: derive(" +
                            data.getNode().getStyle().replace("-fx-background-color: ", "") +
                            ", -30%);");
                });

                data.getNode().setOnMouseExited(event -> {
                    // Trở về màu gốc
                    data.getNode().setStyle(originalStyle);
                });
            });
        } catch (SQLException e) {
            e.printStackTrace();
            // Hiển thị thông báo lỗi cho người dùng
            System.out.println("Lỗi khi truy vấn cơ sở dữ liệu: " + e.getMessage());
        }
        // Cập nhật biểu đồ
        updateCharts();

    }

    private void updateCharts() throws SQLException {
        // TODO: Cập nhật dữ liệu cho biểu đồ doanh thu và biểu đồ trạng thái
        LocalDate fromDate = fromDatePicker.getValue();
        LocalDate toDate = toDatePicker.getValue();
        ObservableList<XYChart.Data<String, Number>> data = new ReportDAO().getValue(fromDate, toDate);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Doanh thu theo tháng");
        series.setData(data);

        CategoryAxis xAxis = (CategoryAxis) revenueChart.getXAxis();
        xAxis.setTickLabelRotation(-45); // Xoay label 45 độ nhìn gọn
// KHÔNG cần setTickLabelGap ở đây, vì không giúp cách cột

        revenueChart.getData().clear();
        revenueChart.getData().add(series);

// Chính xác ở đây nè
        revenueChart.setCategoryGap(100); // 👈 cái này giúp các tháng (các cột) cách xa nhau theo trục ngang
        revenueChart.setBarGap(10);        // nếu sau này có nhiều cột trong cùng 1 tháng thì mỗi cột cũng cách nhau

    }




    private void exportFullReport() {
        // TODO: Xuất toàn bộ báo cáo (bao gồm biểu đồ, bảng dữ liệu và tóm tắt)
        try {
            String filePath = PDF_Export.exportNodeToPDF(revenueChart,"Report_List.pdf");


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText("Xuất file PDF thành công!");
            alert.setContentText("Đã lưu tại:\n" + filePath);
            alert.showAndWait();

            System.out.println("PDF exported to: " + filePath);

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText("Xuất file thất bại");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    @Setter
    private DashboardController parentController;

    private ContextMenu currentContextMenu;

    public void handleCloseButton(ActionEvent event) {
        // Xoá apartment view
        ((Pane)reportView.getParent()).getChildren().clear();
        // Thêm lại dashboard nodes từ controller cha
        parentController.getContentArea().getChildren().setAll(parentController.getDashboardNodes());
    }

    public void getSelectedApartment(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            Report selectedReport = reportTable.getSelectionModel().getSelectedItem();
            if (selectedReport == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng chọn căn hộ để chỉnh sửa!");
                alert.showAndWait();
                return;
            }

            // Nếu đã có ContextMenu đang mở -> đóng lại
            if (currentContextMenu != null && currentContextMenu.isShowing()) {
                currentContextMenu.hide();
            }

            // Tạo ContextMenu mới
            ContextMenu contextMenu = new ContextMenu();
            MenuItem editItem = new MenuItem("Xem chi tiết báo cáo");

            editItem.setOnAction(e -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/utc2/apartmentmanagement/fxml/Report/DetailReport.fxml"));
                    Parent root = loader.load();

                    DetailReportController editController = loader.getController();
                    editController.setParentController(this);   // set controller cha
                    editController.setApartment(selectedReport); // set báo cáo cần xem chi tiết

                    Stage stage = new Stage();
                    stage.setTitle("Chi tiết báo cáo");
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            contextMenu.getItems().add(editItem);

            // Lưu context menu mới
            currentContextMenu = contextMenu;

            // Hiện ContextMenu tại vị trí con trỏ
            contextMenu.show(reportTable, mouseEvent.getScreenX(), mouseEvent.getScreenY());

            // Thêm listener để ẩn ContextMenu nếu click chỗ khác
            reportTable.getScene().addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                // Ẩn menu nếu click trái hoặc phải ở nơi khác
                if (currentContextMenu != null && currentContextMenu.isShowing()
                        && (event.getButton() == MouseButton.PRIMARY || event.getButton() == MouseButton.SECONDARY)) {
                    currentContextMenu.hide();
                }
            });
        }
    }
    @FXML
    public void handleSelected(Event event) {
        pagination.setVisible(true);
        pagination.setManaged(true);
    }

    public void handleTabSumary(Event event) {
        if(pagination!=null){
            pagination.setVisible(false);
            pagination.setManaged(false);
        }
    }

    public void handleTabChart(Event event) {
        if(pagination!=null){
            pagination.setVisible(false);
            pagination.setManaged(false);
        }
    }
}