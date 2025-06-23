package com.utc2.apartmentmanagement.Controller.Service;

import com.google.gson.JsonObject;
import com.utc2.apartmentmanagement.DAO.Apartment.ApartmentDAO;
import com.utc2.apartmentmanagement.DAO.Billing.BillsDAO;
import com.utc2.apartmentmanagement.DAO.Service.ServiceDAO;
import com.utc2.apartmentmanagement.DAO.Service.ServiceRegistrationDAO;
import com.utc2.apartmentmanagement.DAO.User.ResidentDAO;
import com.utc2.apartmentmanagement.DAO.User.UserDAO;
import com.utc2.apartmentmanagement.Model.Billing.Bills;
import com.utc2.apartmentmanagement.Model.Session;
import com.utc2.apartmentmanagement.Payment.Gateway.VnPay.VnpayReturnServer;
import com.utc2.apartmentmanagement.Payment.Service.PaymentService;
import com.utc2.apartmentmanagement.Utils.QRCodeGenerator;
import com.utc2.apartmentmanagement.Payment.Gateway.VnPay.Vnpay;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.sql.Date;
import java.util.*;
import java.util.List;


import static com.utc2.apartmentmanagement.Utils.AlertBox.showAlert;

public class ServiceInformationController implements Initializable {
    @Getter
    @FXML public TextField serviceNameField;

    @Getter
    @FXML public TextArea serviceDescriptionArea;

    @Getter
    @FXML public Label subtotalLabel;

    @Getter
    @FXML public ComboBox<Integer> durationComboBox;

    @Getter
    @FXML public DatePicker startDatePicker;

    @FXML public TextField servicePriceField;
    @FXML public TextField billIdField;
    @FXML public TextField apartmentIdField;
    @FXML public DatePicker billingDatePicker;
    @FXML public TextField billedToField;
    @FXML public Label totalAmountLabel;
    @FXML public RadioButton vnpayRadio;
    @FXML public ToggleGroup paymentMethodGroup;
    @FXML public RadioButton transferRadio;
    @FXML public Button payButton;
    @FXML public ImageView QrCode;
    @FXML public AnchorPane anchorPaneQrCode;
    @FXML public HBox HboxButton;
    @FXML public Label labelDuration;

    private Button confirmPaymentButton;

    @Setter
    private ServicesController servicesController;

    // Thêm field
    private PaymentService paymentService = new PaymentService();
    private int currentPaymentId = -1; // Để lưu payment ID cho trường hợp transfer
    private String apartmentId;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            setUpValues();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // set sự kiện cho combobox mỗi khi người dùng thay đổi thời gian sử dụng dịch vụ
        durationComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            String priceStr = servicePriceField.getText().split(" /")[0].replace(".", "").trim();
            try {
                int price = Integer.parseInt(priceStr);
                updateTotalCost(price);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        anchorPaneQrCode.setManaged(false);
        anchorPaneQrCode.setVisible(false);

        // xử lý sự kiện click radioVnpay thì ẩn anchorPaneQrCode
        vnpayRadio.setOnAction(event -> {
            anchorPaneQrCode.setManaged(false);
            anchorPaneQrCode.setVisible(false);
            if(confirmPaymentButton!=null){
                confirmPaymentButton.setManaged(false);
                confirmPaymentButton.setVisible(false);
            }
            payButton.setVisible(true);
        });
    }

    private void setUpValues() throws SQLException {
        // các khoảng thời gian trong combobox
        durationComboBox.getItems().addAll(1,3,6,12);
        //mặc định là đăng ký trong vòng 1 tháng
        durationComboBox.setValue(1);

        // mặc định ngày bắt đầu dịch vụ là hôm nay
        LocalDate now = LocalDate.now();
        startDatePicker.setValue(now);
        billingDatePicker.setValue(now);

        // billing information
        List<Bills> billList = new BillsDAO().getAllbills();
        billIdField.setText(String.valueOf(billList.size()+1));

        String userName = Session.getUserName();
        int userId = new UserDAO().getIdByUserName(userName);
        Map<String, Object> apartmentInf = new ApartmentDAO().getInformation(userId);

        apartmentIdField.setText(String.valueOf(apartmentInf.get("apartment_id")));
        apartmentId = String.valueOf(apartmentInf.get("apartment_id"));
        billedToField.setText(String.valueOf(apartmentInf.get("full_name")));
    }

    // Thiết lập thông tin dịch vụ
    public void setServiceInfo(String name, String price, String description, String unit) {

        serviceNameField.setText(name);
        servicePriceField.setText(price + " / " + unit);
        // nếu unit khác tháng thì sẽ ẩn phần duration, các đơn vị khác chỉ mặc định đăng ký 1 lần và không có thời gian hạn
        if(!unit.equalsIgnoreCase("thang")){
            // ẩn combobox
            durationComboBox.setVisible(false);
            durationComboBox.setManaged(false);
            durationComboBox.setValue(null);
            // ẩn label
            labelDuration.setVisible(false);
            labelDuration.setManaged(false);
        }
        serviceDescriptionArea.setText(description);

        updateTotalCost(Integer.parseInt(price.replace(".", "")));
    }



    // Cập nhật tổng chi phí dựa trên thời hạn đã chọn
    private void updateTotalCost(int price) {
        int duration = durationComboBox.getValue() != null ? durationComboBox.getValue() : 1;
        double total =  price * duration;

        NumberFormat format = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedTotal = format.format(total) + " VND";

        totalAmountLabel.setText(formattedTotal);
    }

    public void savePayment(String serviceName) throws SQLException {
        int serviceId = new ServiceDAO().getServiceIdByServiceName(serviceName);
        int userID = new UserDAO().getIdByUserName(Session.getUserName());
        int residentID = new ResidentDAO().getResidentIDByUserID(userID);
        Date startDate = Date.valueOf(startDatePicker.getValue());
        System.out.println("startDate: " + startDate);
        int duration = durationComboBox.getValue() != null ? durationComboBox.getValue() : 1;

        // Chuyển startDate sang LocalDate để cộng ngày
        LocalDate endLocalDate = startDate.toLocalDate().plusMonths(duration);

        // Chuyển lại về java.sql.Date
        Date endDate = Date.valueOf(endLocalDate);
        if(duration == 1){
            endDate = startDate;
        }
        System.out.println("endDate: " + endDate);

        boolean saveServiceRegistration = new ServiceRegistrationDAO().addServiceRegistration(serviceId, apartmentId, startDate, endDate, "active", residentID, residentID);

//                boolean saveBill = new BillsDAO().addBill(new Bills(0,apartmentId, startDate, totalAmount, "paid", Date.valueOf(LocalDate.now()), Date.valueOf(LocalDate.now()), Integer.parseInt(billedToStr)));


        if(saveServiceRegistration){
            System.out.println("Save successful");
        }
        else{
            System.out.println("Save failed");
        }
    }
    // Cập nhật phương thức proceedToPayment()
    public void proceedToPayment() throws FileNotFoundException {
        if(vnpayRadio.isSelected()){
            try {
                // Lấy thông tin cần thiết
                String apartmentId = apartmentIdField.getText();
                String rawAmount = totalAmountLabel.getText().replace("VND", "").replace(".", "").trim();
                double totalAmount = Double.parseDouble(rawAmount);
                String billedToStr = billedToField.getText();
                String serviceName = serviceNameField.getText();

                // Set thông tin thanh toán cho VnpayReturnServer
                VnpayReturnServer.setPaymentInfo(apartmentId, totalAmount, getUserIdFromName(billedToStr), serviceName);


                // Khởi động server để nhận callback
                VnpayReturnServer.start();

                // Đóng popup đăng ký
                Stage stage = (Stage) serviceNameField.getScene().getWindow();
                stage.close();

                // Tiến hành thanh toán
                long price = (long) totalAmount;
                JsonObject paymentResult = new Vnpay().handlePayVnpay(price);
                // lưu dịch vụ nào được đăng ký bởi người dùng nào
                savePayment(serviceName);
//                int serviceId = new ServiceDAO().getServiceIdByServiceName(serviceName);
//                int userID = new UserDAO().getIdByUserName(Session.getUserName());
//                int residentID = new ResidentDAO().getResidentIDByUserID(userID);
//                Date startDate = Date.valueOf(startDatePicker.getValue());
//                System.out.println("startDate: " + startDate);
//                int duration = durationComboBox.getValue() != null ? durationComboBox.getValue() : 1;
//
//                // Chuyển startDate sang LocalDate để cộng ngày
//                LocalDate endLocalDate = startDate.toLocalDate().plusMonths(duration);
//
//                // Chuyển lại về java.sql.Date
//                Date endDate = Date.valueOf(endLocalDate);
//                if(duration == 1){
//                    endDate = startDate;
//                }
//                System.out.println("endDate: " + endDate);
//
//                boolean saveServiceRegistration = new ServiceRegistrationDAO().addServiceRegistration(serviceId, apartmentId, startDate, endDate, "active", residentID, residentID);
//
////                boolean saveBill = new BillsDAO().addBill(new Bills(0,apartmentId, startDate, totalAmount, "paid", Date.valueOf(LocalDate.now()), Date.valueOf(LocalDate.now()), Integer.parseInt(billedToStr)));
//
//
//                if(saveServiceRegistration){
//                    System.out.println("Save successful");
//                }
//                else{
//                    System.out.println("Save failed");
//                }
                if (paymentResult != null) {
                    showAlert("Notification","Redirecting to payment gateway...");
                } else {
                    showAlert("Notification","Failed to create payment URL");
                    VnpayReturnServer.stop(); // Dừng server nếu không tạo được URL
                }

            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Có lỗi xảy ra khi xử lý thanh toán: " + e.getMessage());
            }
        }
        else if(transferRadio.isSelected()){
            try {
                // Tạo payment với trạng thái pending
                String apartmentId = apartmentIdField.getText();
                String rawAmount = totalAmountLabel.getText().replace("VND", "").replace(".", "").trim();
                double totalAmount = Double.parseDouble(rawAmount);
                String billedToStr = billedToField.getText();
                String serviceName = serviceNameField.getText();

                paymentService.processTransferPayment(apartmentId, totalAmount, getUserIdFromName(billedToStr), serviceName);

                savePayment(serviceName);
                // Hiển thị QR code
                showQRCodeForTransfer();

            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Có lỗi khi tạo thanh toán: " + e.getMessage());
            }
        }
        else{
            showAlert("Notification","Please select payment method!");
        }
    }
    // Thêm phương thức helper
    private int getUserIdFromName(String fullName) throws SQLException {
        // Cần implement logic để lấy user ID từ tên
        // Có thể dùng UserDAO để query
        UserDAO userDAO = new UserDAO();
        return userDAO.getIdByUserName(Session.getUserName()); // Cần tạo method này trong UserDAO
    }

    // Cập nhật phương thức showQRCodeForTransfer
    private void showQRCodeForTransfer() throws FileNotFoundException {
        // Lấy số tiền từ label
        String totalText = totalAmountLabel.getText().replace("VND", "").replace(".", "").trim();
        String content = "Thanh toán dịch vụ: " + totalText + " VND";
        String outputDir = System.getProperty("user.dir") + "/qrcodes";
        new File(outputDir).mkdirs();
        String filePath = outputDir + "/payment_qr.png";

        try {
            QRCodeGenerator.generateQRCode(content, filePath, 250, 250);
            System.out.println("Đã tạo QR code tại: " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        anchorPaneQrCode.setManaged(true);
        anchorPaneQrCode.setVisible(true);
        Image image = new Image(new FileInputStream("qrcodes/payment_qr.png"));
        QrCode.setImage(image);

        // Hiển thị nút "Đã thanh toán" và ẩn nút "Thanh toán ngay"
        payButton.setVisible(false);
        showPaymentConfirmButton();
    }

    private void showPaymentConfirmButton() {
        // Tạo nút xác nhận thanh toán
        Button confirmPaymentButton = new Button("PAID");
        confirmPaymentButton.setStyle(
                "-fx-background-color: #4caf50; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand;"
        );
        confirmPaymentButton.setPrefWidth(200);

        confirmPaymentButton.setOnAction(e -> {
            // Hiển thị thông báo thành công
            showPaymentSuccess();
        });

        HboxButton.getChildren().add(confirmPaymentButton);
        this.confirmPaymentButton = confirmPaymentButton;
    }

    private void showPaymentSuccess() {
        // Ẩn QR code
        anchorPaneQrCode.setVisible(false);
        anchorPaneQrCode.setManaged(false);

        // Hiển thị thông báo thành công đơn giản
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thành công");
        alert.setHeaderText("🎉 Thanh toán thành công!");
        alert.setContentText("Cảm ơn quý cư dân đã thanh toán dịch vụ.\nGiao dịch đã được xử lý thành công!");

        // Tùy chỉnh màu sắc
        alert.getDialogPane().setStyle(
                "-fx-background-color: #e8f5e8; " +
                        "-fx-border-color: #4caf50; " +
                        "-fx-border-radius: 10;"
        );

        alert.showAndWait().ifPresent(response -> {
            // Đóng cửa sổ sau khi người dùng click OK
            Stage stage = (Stage) serviceNameField.getScene().getWindow();
            stage.close();
        });
    }


    @FXML
    public void handleCancelButton(ActionEvent actionEvent) {
        Stage stage = (Stage) serviceNameField.getScene().getWindow();
        stage.close();
    }
}
