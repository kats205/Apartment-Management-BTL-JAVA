//package com.utc2.apartmentmanagement.Controller.User;
//
//import com.google.gson.JsonObject;
//import com.utc2.apartmentmanagement.DAO.ApartmentDAO;
//import com.utc2.apartmentmanagement.DAO.BillsDAO;
//import com.utc2.apartmentmanagement.DAO.UserDAO;
//import com.utc2.apartmentmanagement.DAO.VnPayDAO;
//import com.utc2.apartmentmanagement.Model.Bills;
//import com.utc2.apartmentmanagement.Model.Session;
//import com.utc2.apartmentmanagement.Utils.QRCodeGenerator;
//import com.utc2.apartmentmanagement.Vnpay.Vnpay;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.control.*;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.TextArea;
//import javafx.scene.control.TextField;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.AnchorPane;
//import javafx.scene.layout.HBox;
//import javafx.stage.Stage;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.net.URL;
//import java.sql.SQLException;
//import java.text.NumberFormat;
//import java.time.LocalDate;
//import java.util.*;
//import java.util.List;
//
//
////import static com.utc2.apartmentmanagement.Utils.AlertBox.showAlert;
//
//public class ServiceInformationController implements Initializable {
//    @Getter
//    @FXML public TextField serviceNameField;
//
//    @Getter
//    @FXML public TextArea serviceDescriptionArea;
//
//    @Getter
//    @FXML public Label subtotalLabel;
//
//    @Getter
//    @FXML public ComboBox<Integer> durationComboBox;
//
//    @Getter
//    @FXML public DatePicker startDatePicker;
//
//    @FXML public TextField servicePriceField;
//    @FXML public TextField billIdField;
//    @FXML public TextField apartmentIdField;
//    @FXML public DatePicker billingDatePicker;
//    @FXML public TextField billedToField;
//    @FXML public Label totalAmountLabel;
//    @FXML public RadioButton vnpayRadio;
//    @FXML public ToggleGroup paymentMethodGroup;
//    @FXML public RadioButton transferRadio;
//    @FXML public Button payButton;
//    @FXML public ImageView QrCode;
//    @FXML public AnchorPane anchorPaneQrCode;
//    public HBox HboxButton;
//
//    private Button confirmPaymentButton;
//
//    @Setter
//    private ServicesController servicesController;
//
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//
//        try {
//            setUpValues();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//        // set sự kiện cho combobox mỗi khi người dùng thay đổi thời gian sử dụng dịch vụ
//        durationComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
//            String priceStr = servicePriceField.getText().split(" /")[0].replace(".", "").trim();
//            try {
//                int price = Integer.parseInt(priceStr);
//                updateTotalCost(price);
//            } catch (NumberFormatException e) {
//                e.printStackTrace();
//            }
//        });
//
//        anchorPaneQrCode.setManaged(false);
//        anchorPaneQrCode.setVisible(false);
//
//        // xử lý sự kiện click radioVnpay thì ẩn anchorPaneQrCode
//        vnpayRadio.setOnAction(event -> {
//            anchorPaneQrCode.setManaged(false);
//            anchorPaneQrCode.setVisible(false);
//            confirmPaymentButton.setManaged(false);
//            confirmPaymentButton.setVisible(false);
//            payButton.setVisible(true);
//        });
//    }
//
//    private void setUpValues() throws SQLException {
//        // các khoảng thời gian trong combobox
//        durationComboBox.getItems().addAll(1,3,6,12);
//        //mặc định là đăng ký trong vòng 1 tháng
//        durationComboBox.setValue(1);
//
//        // mặc định ngày bắt đầu dịch vụ là hôm nay
//        LocalDate now = LocalDate.now();
//        startDatePicker.setValue(now);
//        billingDatePicker.setValue(now);
//
//        // billing information
//        List<Bills> billList = new BillsDAO().getAllbills();
//        billIdField.setText(String.valueOf(billList.size()+1));
//
//        String userName = Session.getUserName();
//        int userId = new UserDAO().getIdByUserName(userName);
//        Map<String, Object> apartmentInf = new ApartmentDAO().getInformation(userId);
//
//        apartmentIdField.setText(String.valueOf(apartmentInf.get("apartment_id")));
//
//        billedToField.setText(String.valueOf(apartmentInf.get("full_name")));
//    }
//
//    // Thiết lập thông tin dịch vụ
//    public void setServiceInfo(String name, String price, String description, String unit) {
//
//        serviceNameField.setText(name);
//        servicePriceField.setText(price + " / " + unit);
//        serviceDescriptionArea.setText(description);
//
//        updateTotalCost(Integer.parseInt(price.replace(".", "")));
//    }
//
//
//
//    // Cập nhật tổng chi phí dựa trên thời hạn đã chọn
//    private void updateTotalCost(int price) {
//        int duration = durationComboBox.getValue() != null ? durationComboBox.getValue() : 1;
//        double total =  price * duration;
//
//        NumberFormat format = NumberFormat.getInstance(new Locale("vi", "VN"));
//        String formattedTotal = format.format(total) + " VND";
//
//        totalAmountLabel.setText(formattedTotal);
//    }
//
//
//    public void proceedToPayment() throws FileNotFoundException {
//        if(vnpayRadio.isSelected()){
//            // Đóng popup đăng ký
//            Stage stage = (Stage) serviceNameField.getScene().getWindow();
//            stage.close();
//
//            // Tiến hành thanh toán
//            String rawText = totalAmountLabel.getText();
//            String cleaned = rawText.replace("VND", "").replace(".", "").trim(); // "300000"
//
//            long price = Long.parseLong(cleaned);
//            JsonObject paymentResult = new Vnpay().handlePayVnpay(price);
//
//            if (paymentResult != null) {
//
//                VnPayDAO vnPayDAO = new VnPayDAO();
////                vnPayDAO.addPaymentByVnpay(paymentResult, 1);
//
//
//
//                // TODO: Lưu thông tin đăng ký vào database sau khi thanh toán thành công
//                showAlert("Notification","Redirecting to payment gateway...");
//            } else {
//                showAlert("Notification","Failed to create payment URL");
//            }
//        }
//        else if(transferRadio.isSelected()){
//            // Lấy số tiền từ label (đã xử lý bỏ "VND")
//            String totalText = totalAmountLabel.getText().replace("VND", "").replace(".", "").trim();
//            String content = "Thanh toán dịch vụ: " + totalText + " VND";
//            String outputDir = System.getProperty("user.dir") + "/qrcodes";
//            new File(outputDir).mkdirs(); // Tạo thư mục nếu chưa có
//            String filePath = outputDir + "/payment_qr.png";
//
//            try {
//                QRCodeGenerator.generateQRCode(content, filePath, 250, 250);
//                System.out.println("Đã tạo QR code tại: " + filePath);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            anchorPaneQrCode.setManaged(true);
//            anchorPaneQrCode.setVisible(true);
//            Image image = new Image(new FileInputStream("qrcodes/payment_qr.png"));
//            QrCode.setImage(image);
//
//            // Hiển thị nút "Đã thanh toán" và ẩn nút "Thanh toán ngay"
//            payButton.setVisible(false);
//            showPaymentConfirmButton();
//
//        }
//        else{
//            showAlert("Notification","Please select payment method!");
//        }
//    }
//    private void showPaymentConfirmButton() {
//        // Tạo nút xác nhận thanh toán
//        Button confirmPaymentButton = new Button("PAID");
//        confirmPaymentButton.setStyle(
//                "-fx-background-color: #4caf50; " +
//                        "-fx-text-fill: white; " +
//                        "-fx-font-size: 14px; " +
//                        "-fx-font-weight: bold; " +
//                        "-fx-background-radius: 8; " +
//                        "-fx-cursor: hand;"
//        );
//        confirmPaymentButton.setPrefWidth(200);
//
//        confirmPaymentButton.setOnAction(e -> {
//            // Hiển thị thông báo thành công
//            showPaymentSuccess();
//        });
//
//        HboxButton.getChildren().add(confirmPaymentButton);
//        this.confirmPaymentButton = confirmPaymentButton;
//    }
//
//    private void showPaymentSuccess() {
//        // Ẩn QR code
//        anchorPaneQrCode.setVisible(false);
//        anchorPaneQrCode.setManaged(false);
//
//        // Hiển thị thông báo thành công đơn giản
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Thành công");
//        alert.setHeaderText("🎉 Thanh toán thành công!");
//        alert.setContentText("Cảm ơn quý cư dân đã thanh toán dịch vụ.\nGiao dịch đã được xử lý thành công!");
//
//        // Tùy chỉnh màu sắc
//        alert.getDialogPane().setStyle(
//                "-fx-background-color: #e8f5e8; " +
//                        "-fx-border-color: #4caf50; " +
//                        "-fx-border-radius: 10;"
//        );
//
//        alert.showAndWait().ifPresent(response -> {
//            // Đóng cửa sổ sau khi người dùng click OK
//            Stage stage = (Stage) serviceNameField.getScene().getWindow();
//            stage.close();
//        });
//    }
//
//
//    @FXML
//    public void handleCancelButton(ActionEvent actionEvent) {
//        Stage stage = (Stage) serviceNameField.getScene().getWindow();
//        stage.close();
//    }
//}
