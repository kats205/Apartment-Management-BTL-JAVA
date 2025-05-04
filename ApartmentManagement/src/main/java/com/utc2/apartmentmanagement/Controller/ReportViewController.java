package com.utc2.apartmentmanagement.Controller;

import com.utc2.apartmentmanagement.DAO.ReportDAO;
import com.utc2.apartmentmanagement.DAO.UserDAO;
import com.utc2.apartmentmanagement.Model.PDF_Export;
import com.utc2.apartmentmanagement.Model.Report;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ResourceBundle;

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
    private TableColumn<Map, String> periodColumn;

    @FXML
    private TableColumn<Map, Double> totalInvoicesColumn;

    @FXML
    private TableColumn<Map, Integer> paidInvoicesColumn;

    @FXML
    private TableColumn<Map, Integer> unpaidInvoicesColumn;

    @FXML
    private TableColumn<Map, Integer> overdueInvoicesColumn;

    @FXML
    private TableColumn<Map, Double> totalRevenueColumn;

    @FXML
    private TableColumn<Map, Double> totalFeesColumn;

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

    // Getter cho nút đóng để DashboardController có thể truy cập
    @Getter
    @FXML
    private Button closeButton;

    @FXML
    private  PieChart apartmentStatusPieChart;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Khởi tạo các thành phần UI
        initializeComponents();

        // Thiết lập các sự kiện
        setupEventHandlers();

        // Tải dữ liệu mặc định
        loadDefaultData();

//        apartmentStatusPieChart.setLabelLineLength(10);
//        apartmentStatusPieChart.setLegendVisible(true);
//
//        try {
//            LocalDate fromDate = fromDatePicker.getValue();
//            LocalDate toDate = toDatePicker.getValue();
//
//            // Kiểm tra giá trị ngày
//            if (fromDate == null || toDate == null) {
//                // Hiển thị thông báo lỗi
//                System.out.println("Vui lòng chọn ngày bắt đầu và ngày kết thúc");
//                return;
//            }
//
//            // Kiểm tra thứ tự ngày
//            if (fromDate.isAfter(toDate)) {
//                System.out.println("Ngày bắt đầu phải trước ngày kết thúc");
//                return;
//            }
//
//            ObservableList<PieChart.Data> pieChartData = new ReportDAO().PieChart(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 3, 31));
//            apartmentStatusPieChart.setData(pieChartData);
//
//            // Thêm hiệu ứng tương tác sau khi đã nạp dữ liệu
//            apartmentStatusPieChart.getData().forEach(data -> {
//                String originalStyle = data.getNode().getStyle(); // Lưu style ban đầu
//
//                data.getNode().setOnMouseEntered(event -> {
//                    // Làm nổi bật phần được chọn
//                    data.getNode().setStyle("-fx-background-color: derive(" +
//                            data.getNode().getStyle().replace("-fx-background-color: ", "") +
//                            ", -30%);");
//                });
//
//                data.getNode().setOnMouseExited(event -> {
//                    // Trở về màu gốc
//                    data.getNode().setStyle(originalStyle);
//                });
//            });
//        } catch (SQLException e) {
//            e.printStackTrace();
//            // Hiển thị thông báo lỗi cho người dùng
//            System.out.println("Lỗi khi truy vấn cơ sở dữ liệu: " + e.getMessage());
//        }

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

    public void initialSummary(){
        List<Report> reportList = new ReportDAO().getAllReports();
        int totalReport = reportList.size();
        String newestReport = reportList.getLast().getReportType();
        // Map 1: Đếm số lần mỗi loại báo cáo
        Map<String, Integer> reportTypeCountMap = new HashMap<>();

// Map 2: Đếm số lần mỗi user tạo báo cáo
        Map<Integer, Integer> userReportCountMap = new HashMap<>();

        for (Report report : reportList) {
            // Đếm loại báo cáo
            String type = report.getReportType();
            reportTypeCountMap.put(type, reportTypeCountMap.getOrDefault(type, 0) + 1);

            // Đếm theo user
            int userId = report.getGeneratedByUserId();
            userReportCountMap.put(userId, userReportCountMap.getOrDefault(userId, 0) + 1);
        }

        // Tìm loại báo cáo phổ biến nhất
        String popularReport = reportTypeCountMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        // Tìm user tạo nhiều báo cáo nhất
        Integer mostActiveUserId = userReportCountMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
        String nameUserById = new UserDAO().getUserByID(mostActiveUserId).getFullName();
        totalReportsLabel.setText(String.valueOf(totalReport));
        latestReportLabel.setText(newestReport);
        popularReportTypeLabel.setText(popularReport);
        topGeneratorLabel.setText(nameUserById);
        // Output thử
        System.out.println("Tổng số báo cáo: " + totalReport);
        System.out.println("Báo cáo mới nhất: " + newestReport);
        System.out.println("Loại báo cáo phổ biến nhất: " + popularReport);
        System.out.println("Người dùng tạo nhiều báo cáo nhất: User ID = " + mostActiveUserId);
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
        reportTypeComboBox.getItems().addAll("Báo cáo theo tháng", "Báo cáo theo quý", "Báo cáo theo năm");
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

        createdAtColumn.setCellValueFactory( new PropertyValueFactory<>("createdAt"));
        createdAtColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");

        updatedAtColumn.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));
        updatedAtColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");

        ObservableList<Report> reportList = FXCollections.observableArrayList();
        List<Report> reportList1 = new ReportDAO().getAllReports();
        reportList.addAll(reportList1);
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
            // Handle exception, có thể hiển thị thông báo lỗi cho người dùng
        }
        // Tạo dữ liệu mẫu nếu chưa có
//        if (apartmentStatusPieChart.getData().isEmpty()) {
//            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
//                    new PieChart.Data("Đã thanh toán", 65),
//                    new PieChart.Data("Chưa thanh toán", 25),
//                    new PieChart.Data("Quá hạn", 10)
//            );
//            apartmentStatusPieChart.setData(pieChartData);
//        }
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

        // Cập nhật bảng dữ liệu
//        updateTableData();

        // Cập nhật dữ liệu tóm tắt
        updateSummaryData();
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

//    private void updateTableData() throws SQLException {
//        // TODO: Cập nhật dữ liệu cho bảng báo cáo
//        LocalDate fromDate = fromDatePicker.getValue();
//        LocalDate toDate = toDatePicker.getValue();
//        reportTable.setItems(new ReportDAO().getValue(fromDate, toDate));
//        periodColumn.setCellValueFactory(new MapValueFactory<>("kỳ"));
//        totalInvoicesColumn.setCellValueFactory(new MapValueFactory<>("tổng số hóa đơn"));
//        overdueInvoicesColumn.setCellValueFactory(new MapValueFactory<>("quá hạn"));
//        paidInvoicesColumn.setCellValueFactory(new MapValueFactory<>("đã thanh toán"));
//        totalFeesColumn.setCellValueFactory(new MapValueFactory<>("tổng phí phạt"));
//        unpaidInvoicesColumn.setCellValueFactory(new MapValueFactory<>("chưa thanh toán"));
//        totalRevenueColumn.setCellValueFactory(new MapValueFactory<>("tổng doanh thu"));
//    }

    private void updateSummaryData() {
        // TODO: Cập nhật các thông tin tóm tắt
        // Giả sử các giá trị mặc định
//        totalInvoicesLabel.setText("0");
//        totalRevenueLabel.setText("0 VNĐ");
//        paymentRateLabel.setText("0%");
//        overdueRateLabel.setText("0%");
    }

    private void exportFullReport() {
        // TODO: Xuất toàn bộ báo cáo (bao gồm biểu đồ, bảng dữ liệu và tóm tắt)
        try {
            String filePath = PDF_Export.exportReportList(revenueChart,apartmentStatusPieChart,"Report_List.pdf");


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

    private void printReport() {
        // TODO: In báo cáo
    }

    private void exportToExcel() {
        // TODO: Xuất báo cáo dạng Excel
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
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/utc2/apartmentmanagement/fxml/DetailReport.fxml"));
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
}