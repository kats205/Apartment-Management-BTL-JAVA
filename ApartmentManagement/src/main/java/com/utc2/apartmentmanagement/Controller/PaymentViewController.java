package com.utc2.apartmentmanagement.Controller;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.utc2.apartmentmanagement.DAO.ApartmentDAO;
import com.utc2.apartmentmanagement.DAO.PaymentDAO;
import com.utc2.apartmentmanagement.Model.Apartment;
import com.utc2.apartmentmanagement.Model.Payment;
import com.utc2.apartmentmanagement.Utils.AlertBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
//        detailButton.setOnAction(event -> viewPaymentDetails());

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
            // Đường dẫn mặc định
            String directoryPath = "src/main/resources/com/utc2/apartmentmanagement/PDF_File/";
            String filePath = directoryPath + "Payment_List.pdf";
            // Tạo thư mục nếu chưa có
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            PdfFont vietnameseFont = PdfFontFactory.createFont("src/main/resources/com/utc2/apartmentmanagement/Font/arial.ttf");

            // Tạo file PDF
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);

            Document document = new Document(pdf);
            document.setFont(vietnameseFont);

            Color headerColor = new DeviceRgb(209, 224, 227);
            Color textHeadColor = new DeviceRgb(21, 88, 155);

            float[] columnWidthsHeader = {280F, 280F}; // Điều chỉnh theo khổ giấy
            Table headerTable = new Table(columnWidthsHeader);
            headerTable.setWidth(UnitValue.createPercentValue(100));

            // Cột bên trái: Tên chung cư
            Paragraph left = new Paragraph("CHUNG CƯ ABC")
                    .setBold()
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.LEFT);

            LineSeparator line = new LineSeparator(new SolidLine());
            line.setWidth(50);  // Độ dài cố định
            Paragraph lineWrapper1 = new Paragraph().add(line)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setMarginTop(2)
                    .setMarginBottom(2)
                    .setPaddingLeft(20);

            Cell leftCell = new com.itextpdf.layout.element.Cell().add(left).add(lineWrapper1)
                    .setBorder(Border.NO_BORDER)
                    .setTextAlignment(TextAlignment.LEFT);

// Cột bên phải: Tiêu ngữ + đường kẻ + khẩu hiệu
            Paragraph right = new Paragraph("CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM")
                    .setBold()
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setFontSize(12);

            LineSeparator line1 = new LineSeparator(new SolidLine());
            line1.setWidth(100);  // Độ dài cố định
            Paragraph lineWrapper = new Paragraph().add(line1)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setMarginTop(2)
                    .setMarginBottom(2);

            Paragraph slogan = new Paragraph("Độc lập - Tự do - Hạnh phúc")
                    .setItalic()
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setFontSize(11);

            Paragraph rightContent = new Paragraph()
                    .add(right)
                    .add(lineWrapper)
                    .add(slogan);

            Cell rightCell = new com.itextpdf.layout.element.Cell().add(rightContent)
                    .setBorder(Border.NO_BORDER)  // Loại bỏ viền
                    .setTextAlignment(TextAlignment.CENTER);
    // Thêm vào bảng
            headerTable.addCell(leftCell);
            headerTable.addCell(rightCell);

    // Thêm bảng vào document
            document.add(headerTable);

            document.add(new Paragraph("BÁO CÁO").setBold().setFontColor(textHeadColor).setFontSize(14).setTextAlignment(TextAlignment.CENTER));

            // Thêm tiêu đề
            document.add(new Paragraph("Danh sách các thanh toán").setBold().setFontColor(textHeadColor).setFontSize(16).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph(""));

            // Tạo bảng
            float[] columnWidths = {60F, 60F, 80F, 80F, 90F, 90F, 70F};
            Table table = new Table(columnWidths);


            // Header
            table.addCell(new Cell().add(new Paragraph("Mã thanh toán"))
                    .setBackgroundColor(headerColor)
                    .setBold()
            );
            table.addCell(new Cell().add(new Paragraph("Mã hóa đơn").setBold().setFontColor(textHeadColor)).setBackgroundColor(headerColor));
            table.addCell(new Cell().add(new Paragraph("Ngày thanh toán").setBold().setFontColor(textHeadColor)).setBackgroundColor(headerColor));
            table.addCell(new Cell().add(new Paragraph("Số tiền (VNĐ)").setBold().setFontColor(textHeadColor)).setBackgroundColor(headerColor));
            table.addCell(new Cell().add(new Paragraph("Phương thức").setBold().setFontColor(textHeadColor)).setBackgroundColor(headerColor));
            table.addCell(new Cell().add(new Paragraph("Trạng thái").setBold().setFontColor(textHeadColor)).setBackgroundColor(headerColor));
            table.addCell(new Cell().add(new Paragraph("Ngày tạo").setBold().setFontColor(textHeadColor)).setBackgroundColor(headerColor));


            // Data
            List<Payment> payments = new PaymentDAO().getAllPayment();

            for (Payment payment : payments) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(payment.getPaymentID()))));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(payment.getBillID()))));

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String paymentDateStr = payment.getPaymentDate() != null ? dateFormat.format(payment.getPaymentDate()) : "";
                table.addCell(new Cell().add(new Paragraph(paymentDateStr)));

                table.addCell(new Cell().add(new Paragraph(String.format("%.2f", payment.getAmount()))));

                table.addCell(new Cell().add(new Paragraph(payment.getPaymentMedthod())));

                table.addCell(new Cell().add(new Paragraph(payment.getStatus())));

                String createdDateStr = payment.getCreated_at() != null ? dateFormat.format(payment.getCreated_at()) : "";
                table.addCell(new Cell().add(new Paragraph(createdDateStr)));
            }

            // Thêm bảng vào document
            document.add(table);

            // Đóng tài liệu
            document.close();

            // Thông báo thành công
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Xuất file PDF thành công!\nĐã lưu tại: " + filePath);
            alert.showAndWait();

            System.out.println("Xuất báo cáo thanh toán thành công!!!");
            System.out.println("PDF exported to: " + filePath);
        } catch (Exception e) {
            e.printStackTrace();

            // Nếu lỗi thì thông báo lỗi
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

    //    public void handleSearchButton(ActionEvent event) throws SQLException {
//        String apartmentID = apartmentComboBox.getValue();
//
//        // Kiểm tra nếu apartmentID không null
//        Payment payment = null;
//        if (apartmentID != null && !apartmentID.isEmpty()) {
//            // Tìm payment theo apartmentId
//            payment = new PaymentDAO().findPaymentByApartmentId(apartmentID);
//
//            // Xóa dữ liệu cũ trong bảng
//            paymentTable.getItems().clear();
//
//            // Kiểm tra nếu có payment
//            if (payment != null) {
//                // Thêm payment vào bảng
//                ObservableList<Payment> payments = FXCollections.observableArrayList(payment);
//                paymentTable.setItems(payments);
//
//                // Cập nhật số lượng payment
//                paymentCountLabel.setText("1");
//                noContentLabel.setVisible(false);
//            } else {
//                // Không có payment
//                paymentCountLabel.setText("0");
//                noContentLabel.setVisible(true);
//            }
//        }
//
//        LocalDate fromDate = fromDatePicker.getValue();
//        LocalDate toDate = toDatePicker.getValue();
//        if (fromDate != null && toDate != null && payment != null) {
//            List<Payment> paymentList = new PaymentDAO().findPaymentByDate(fromDate, toDate);
//            int sizeList = paymentList.size();
//            if (sizeList == 0) {
//                AlertBox.showAlertForExeptionRegister("Thông báo!", "Không có dữ liệu trong khoảng thời gian này!");
//            } else {
//                ObservableList<Payment> payments = FXCollections.observableArrayList();
//                payments.addAll(paymentList);
//                paymentTable.setItems(payments);
//            }
//        }
//    }
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
            List<Payment> paymentList = new PaymentDAO().findPaymentByDate(fromDate, toDate);
            int sizeList = paymentList.size();
            if (sizeList == 0) {
                AlertBox.showAlertForExeptionRegister("Thông báo!", "Không có dữ liệu trong khoảng thời gian này!");
                paymentCountLabel.setText("1");
                paymentCountLabel.setText(String.valueOf(paymentList.size()));
            } else {
                ObservableList<Payment> payments = FXCollections.observableArrayList();
                payments.addAll(paymentList);
                paymentTable.setItems(payments);
                paymentCountLabel.setText(String.valueOf(paymentList.size()));
                noContentLabel.setVisible(false);
            }
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

    public void handleSearchButton(ActionEvent actionEvent) {
    }
}