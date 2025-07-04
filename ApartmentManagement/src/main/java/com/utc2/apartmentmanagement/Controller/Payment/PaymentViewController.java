package com.utc2.apartmentmanagement.Controller.Payment;

import com.utc2.apartmentmanagement.Controller.DashboardController;
import com.utc2.apartmentmanagement.DAO.Apartment.ApartmentDAO;
import com.utc2.apartmentmanagement.DAO.Billing.PaymentDAO;
import com.utc2.apartmentmanagement.DAO.Report.ReportDAO;
import com.utc2.apartmentmanagement.DAO.User.UserDAO;
import com.utc2.apartmentmanagement.Model.*;
import com.utc2.apartmentmanagement.Model.Billing.Payment;
import com.utc2.apartmentmanagement.Model.Report.Report;
import com.utc2.apartmentmanagement.Utils.PaginationUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import static com.utc2.apartmentmanagement.Utils.AlertBox.showAlert;

public class PaymentViewController implements Initializable {

    @FXML private AnchorPane paymentView;


    @FXML private ComboBox<String> apartmentComboBox;

    @FXML private DatePicker fromDatePicker;
    @FXML private DatePicker toDatePicker;

    @FXML private Button searchButton;
    @FXML private Button refreshBtn;
    @FXML private Button detailButton;
    @FXML private Button printButton;
    @FXML private Button exportButton;

    @FXML private TableView<Payment> paymentTable;
    @FXML private TableColumn<Payment, Integer> idColumn;
    @FXML private TableColumn<Payment, Integer> invoiceColumn;
    @FXML private TableColumn<Payment, Date> paymentDateColumn;
    @FXML private TableColumn<Payment, Double> amountColumn;
    @FXML private TableColumn<Payment, String> methodColumn;
    @FXML private TableColumn<Payment, String> statusColumn;
    @FXML private TableColumn<Payment, Date> createDateColumn;
    @FXML private TableColumn<Payment, String> transactionIdColumn;
    @FXML private Pagination pagination;
    private static final int ROWS_PER_PAGE = 10;
    @FXML private Label noContentLabel;
    @FXML private Label paymentCountLabel;


    private List<Payment> fullPaymentList; // toàn bộ danh sách

    // Getter cho nút đóng để DashboardController có thể truy cập
    @Getter
    @FXML
    private Button closeButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Khởi tạo các thành phần UI
        initializeComponents();

        // Thiết lập các sự kiện
        setupEventHandlers();

        // Tải dữ liệu
        loadData();

        // load dữ liệu của table payment
        loadDataColumn();

        // load dữ liệu combobox apartmentID
        getValueApartmentCB();
    }


    private void getValueApartmentCB(){
        List<String> apartmentIdList = new ApartmentDAO().getAllIdApartment();
        apartmentComboBox.getItems().addAll(apartmentIdList);
    }
    public void getValueColumn(){
        idColumn.setCellValueFactory(new PropertyValueFactory<>("paymentID"));
        idColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");
        idColumn.setPrefWidth(140);

        invoiceColumn.setCellValueFactory(new PropertyValueFactory<>("billID"));
        invoiceColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");
        invoiceColumn.setPrefWidth(140);

        paymentDateColumn.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        paymentDateColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");
        paymentDateColumn.setPrefWidth(150);

        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");
        amountColumn.setPrefWidth(150);

        methodColumn.setCellValueFactory(new PropertyValueFactory<>("paymentMedthod"));
        methodColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");
        methodColumn.setPrefWidth(170);

        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");
        statusColumn.setPrefWidth(130);

        createDateColumn.setCellValueFactory(new PropertyValueFactory<>("created_at"));
        createDateColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");
        createDateColumn.setPrefWidth(130);

        transactionIdColumn.setCellValueFactory(new PropertyValueFactory<>("transactionID"));
        transactionIdColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");
        transactionIdColumn.setPrefWidth(250);

    }


    public void loadDataColumn() {
        getValueColumn();
        fullPaymentList = new PaymentDAO().getAllPayment();
        PaginationUtils.setupPagination(
                fullPaymentList,
                paymentTable,
                pagination,
                paymentCountLabel,
                noContentLabel
        );
    }

    private void initializeComponents() {
        // Khởi tạo DatePicker
        initializeDatePickers();

        // Khởi tạo ComboBox
        initializeComboBoxes();

        // Khởi tạo TableView
        initializeTableView();
    }

    private void initializeDatePickers() {
        // Thiết lập giá trị mặc định: từ đầu tháng đến hiện tại
        LocalDate now = LocalDate.now();
        LocalDate firstDayOfMonth = LocalDate.of(now.getYear(), now.getMonth(), 1);

        fromDatePicker.setValue(firstDayOfMonth);
        toDatePicker.setValue(now);
    }

    private void initializeComboBoxes() {
        // TODO: Khởi tạo dữ liệu cho apartmentComboBox
    }

    private void initializeTableView() {
        // TODO: Cấu hình TableView và các cột
    }

    private void setupEventHandlers() {
        // Xử lý sự kiện tìm kiếm
//        searchButton.setOnAction(event -> searchPayments());

        // Xử lý sự kiện tạo thanh toán mới
//        refreshBtn.setOnAction(event -> createNewPayment());

        // Xử lý sự kiện xem chi tiết
//        detailButton.setOnAction(event -> viewPaymentDetails());

        // Xử lý sự kiện in biên lai

        // Xử lý sự kiện xuất báo cáo
        exportButton.setOnAction(event -> exportReport());

        // Nút đóng mặc định không làm gì - sẽ được xử lý bởi DashboardController
    }

    private void loadData() {
        // TODO: Tải dữ liệu thanh toán từ nguồn dữ liệu

        // Hiển thị số lượng thanh toán
        updatePaymentCount();
    }

    private void searchPayments() {
        // TODO: Thực hiện tìm kiếm thanh toán dựa trên điều kiện

        // Cập nhật bảng và số lượng
        updatePaymentCount();
    }

    private void createNewPayment() {
        // TODO: Mở form tạo thanh toán mới
    }

//    private void viewPaymentDetails() {
//        // TODO: Hiển thị chi tiết thanh toán được chọn
//
//
//    }

    private void printReceipt() {
        // TODO: In biên lai thanh toán được chọn
    }

    private void exportReport() {

        // TODO: Xuất báo cáo thanh toán

        // TODO: Xuất danh sách thanh toán thành PDF
        try {
            String filePath = PDF_Export.exportPaymentList("Payment_List.pdf");

            int user_id = new UserDAO().getIdByUserName(Session.getUserName());
            System.out.println(user_id);
            LocalDate date = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
            String formattedDate = date.format(formatter);
            Report report = new Report("Báo cáo tai chinh", LocalDate.now(), user_id, formattedDate,  filePath, LocalDate.now(), LocalDate.now());
            new ReportDAO().saveReport(report);

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

    private void updatePaymentCount() {
        // TODO: Cập nhật số lượng thanh toán dựa trên dữ liệu hiện tại
        // Giả sử hiện tại có 0 thanh toán
        paymentCountLabel.setText("0");

        // Hiển thị/ẩn nhãn không có dữ liệu
        boolean hasData = false; // TODO: Kiểm tra có dữ liệu hay không
        noContentLabel.setVisible(!hasData);
    }


    @Setter
    private DashboardController parentController;

    @FXML
    public void handleCloseButton(ActionEvent event) {
        // Xoá apartment view
        ((Pane) paymentView.getParent()).getChildren().clear();
        // Thêm lại dashboard nodes từ controller cha
        parentController.getContentArea().getChildren().setAll(parentController.getDashboardNodes());
    }


    public void handleSearchByDateButton(ActionEvent actionEvent) {
        LocalDate fromDate = fromDatePicker.getValue();
        LocalDate toDate = toDatePicker.getValue();
        if (fromDate != null && toDate != null) {
            fullPaymentList = new PaymentDAO().findPaymentByDate(fromDate, toDate);
            if (fullPaymentList.isEmpty()) {
                showAlert("Thông báo!", "Không có dữ liệu trong khoảng thời gian này!");
            }
            PaginationUtils.setupPagination(
                    fullPaymentList,
                    paymentTable,
                    pagination,
                    paymentCountLabel,
                    noContentLabel
            );
        }
    }


    public void handlesearByApartmentIdButton(ActionEvent actionEvent) throws SQLException {
        String apartmentID = apartmentComboBox.getValue();

        // Kiểm tra nếu apartmentID không null
        Payment payment = null;
        if (apartmentID != null && !apartmentID.isEmpty()) {
            // Tìm payment theo apartmentId
            payment = new PaymentDAO().findPaymentByApartmentId(apartmentID);

            // Xóa dữ liệu cũ trong bảng
            paymentTable.getItems().clear();

            // Kiểm tra nếu có payment
            if (payment != null) {
                List<Payment> filteredList = List.of(payment); // hoặc Collections.singletonList(payment)

                // Thêm payment vào bảng
                ObservableList<Payment> payments = FXCollections.observableArrayList(payment);
                PaginationUtils.setupPagination(
                        filteredList,
                        paymentTable,
                        pagination,
                        paymentCountLabel,
                        noContentLabel
                );
                paymentTable.setItems(payments);

                // Cập nhật số lượng payment
                paymentCountLabel.setText("1");
                noContentLabel.setVisible(false);
            } else {
                // Không có payment
                paymentCountLabel.setText("0");
                noContentLabel.setVisible(true);
            }
        }
    }
    @FXML
    public void handleRefreshBtn(){
        loadDataColumn();
    }
    public void handleSearchButton(ActionEvent actionEvent) {
    }
}