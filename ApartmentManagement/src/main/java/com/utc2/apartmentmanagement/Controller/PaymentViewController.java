package com.utc2.apartmentmanagement.Controller;

import com.utc2.apartmentmanagement.DAO.ApartmentDAO;
import com.utc2.apartmentmanagement.DAO.PaymentDAO;
import com.utc2.apartmentmanagement.Model.Apartment;
import com.utc2.apartmentmanagement.Model.Payment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class PaymentViewController implements Initializable {
    @FXML
    public AnchorPane paymentView;
    @FXML
    private ComboBox<String> apartmentComboBox;

    @FXML
    private DatePicker fromDatePicker;

    @FXML
    private DatePicker toDatePicker;

    @FXML
    private Button searchButton;

    @FXML
    private Button newPaymentButton;

    @FXML
    private TableView<Payment> paymentTable;

    @FXML
    private TableColumn<Payment, Integer> idColumn;

    @FXML
    private TableColumn<Payment, Integer> invoiceColumn;

    @FXML
    private TableColumn<Apartment, String> apartmentColumn;

    @FXML
    private TableColumn<Payment, Date> paymentDateColumn;

    @FXML
    private TableColumn<Payment, Double> amountColumn;

    @FXML
    private TableColumn<Payment, String> methodColumn;

    @FXML
    private TableColumn<Payment, String> statusColumn;

    @FXML
    private TableColumn<Payment, Date> createDateColumn;

    @FXML
    private TableColumn<Payment, String> transactionIdColumn;

    @FXML
    private Label noContentLabel;

    @FXML
    private Label paymentCountLabel;

    @FXML
    private Button detailButton;

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

//        apartmentColumn.setCellValueFactory(new PropertyValueFactory<>("apartmentID"));
//        apartmentColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");
//        apartmentColumn.setPrefWidth(140);

        paymentDateColumn.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        paymentDateColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");
        paymentDateColumn.setPrefWidth(150);

        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");
        amountColumn.setPrefWidth(150);

        methodColumn.setCellValueFactory(new PropertyValueFactory<>("paymentMedthod"));
        methodColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");
        methodColumn.setPrefWidth(130);

        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");
        statusColumn.setPrefWidth(130);

        createDateColumn.setCellValueFactory(new PropertyValueFactory<>("created_at"));
        createDateColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");
        createDateColumn.setPrefWidth(130);

        transactionIdColumn.setCellValueFactory(new PropertyValueFactory<>("transactionID"));
        transactionIdColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");
        transactionIdColumn.setPrefWidth(200);

    }


    public void loadDataColumn(){
        getValueColumn();
        ObservableList<Payment> payments = FXCollections.observableArrayList();
        List<Payment> paymentList = new PaymentDAO().getAllPayment();
        payments.addAll(paymentList);
        paymentTable.setItems(payments);
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
        newPaymentButton.setOnAction(event -> createNewPayment());

        // Xử lý sự kiện xem chi tiết
        detailButton.setOnAction(event -> viewPaymentDetails());

        // Xử lý sự kiện in biên lai
        printButton.setOnAction(event -> printReceipt());

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

    private void viewPaymentDetails() {
        // TODO: Hiển thị chi tiết thanh toán được chọn
    }

    private void printReceipt() {
        // TODO: In biên lai thanh toán được chọn
    }

    private void exportReport() {
        // TODO: Xuất báo cáo thanh toán
    }

    private void updatePaymentCount() {
        // TODO: Cập nhật số lượng thanh toán dựa trên dữ liệu hiện tại
        // Giả sử hiện tại có 0 thanh toán
        paymentCountLabel.setText("0");

        // Hiển thị/ẩn nhãn không có dữ liệu
        boolean hasData = false; // TODO: Kiểm tra có dữ liệu hay không
        noContentLabel.setVisible(!hasData);
    }

    // Getter cho nút đóng để DashboardController có thể truy cập
    public Button getCloseButton() {
        return closeButton;
    }

    public void handleSearchButton(ActionEvent event) throws SQLException {
        String apartmentID = apartmentComboBox.getValue();

        // Kiểm tra nếu apartmentID không null
        if (apartmentID != null && !apartmentID.isEmpty()) {
            // Tìm payment theo apartmentId
            Payment payment = new PaymentDAO().findPaymentByApartmentId(apartmentID);

            // Xóa dữ liệu cũ trong bảng
            paymentTable.getItems().clear();

            // Kiểm tra nếu có payment
            if (payment != null) {
                // Thêm payment vào bảng
                ObservableList<Payment> payments = FXCollections.observableArrayList(payment);
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
}