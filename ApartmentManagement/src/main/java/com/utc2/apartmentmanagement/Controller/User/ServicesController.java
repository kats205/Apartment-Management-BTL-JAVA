package com.utc2.apartmentmanagement.Controller.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;

public class ServicesController {

    @FXML
    private TabPane servicesTabPane;

    @FXML
    private Label activeServicesLabel;

    @FXML
    private Label totalMonthlyCostLabel;

    @FXML
    private TableView<?> servicesTableView;

    @FXML
    private TableColumn<?, ?> serviceNameColumn;

    @FXML
    private TableColumn<?, ?> descriptionColumn;

    @FXML
    private TableColumn<?, ?> startDateColumn;

    @FXML
    private TableColumn<?, ?> endDateColumn;

    @FXML
    private TableColumn<?, ?> monthlyFeeColumn;

    @FXML
    private TableColumn<?, ?> statusColumn;

    @FXML
    private TableColumn<?, ?> actionColumn;

    @FXML
    private FlowPane servicesFlowPane;

    // Khởi tạo controller
    public void initialize() {
        setupServiceCards();
        setupTableView();
        updateTotalMonthlyCost();
    }

    // Thiết lập các thẻ dịch vụ và gán sự kiện cho nút đăng ký
    private void setupServiceCards() {
        // Lấy tất cả các nút đăng ký từ servicesFlowPane
        servicesFlowPane.getChildren().forEach(card -> {
            Button registerButton = findRegisterButton(card);
            if (registerButton != null) {
                registerButton.setOnAction(event -> {
                    // Trích xuất thông tin dịch vụ từ thẻ
                    VBox cardVBox = (VBox) ((javafx.scene.layout.AnchorPane) card).getChildren().get(0);

                    String serviceName = getServiceName(cardVBox);
                    String price = getServicePrice(cardVBox);
                    String description = getServiceDescription(cardVBox);
                    ImageView serviceImageView = getServiceImage(cardVBox);

                    // Hiển thị dialog đăng ký dịch vụ
                    showRegisterServiceDialog(serviceName, price, description, serviceImageView.getImage());
                });
            }
        });
    }

    // Tìm nút đăng ký trong một thẻ dịch vụ
    private Button findRegisterButton(javafx.scene.Node card) {
        if (card instanceof javafx.scene.layout.AnchorPane) {
            VBox vbox = (VBox) ((javafx.scene.layout.AnchorPane) card).getChildren().get(0);
            return (Button) vbox.getChildren().stream()
                    .filter(node -> node instanceof Button)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    // Lấy tên dịch vụ từ thẻ dịch vụ
    private String getServiceName(VBox cardVBox) {
        for (javafx.scene.Node node : cardVBox.getChildren()) {
            if (node instanceof Label) {
                Label label = (Label) node;
                if (label.getTextFill().toString().equals("0x1a936fff")) {
                    return label.getText();
                }
            }
        }
        return "Unknown Service";
    }

    // Lấy giá dịch vụ từ thẻ dịch vụ
    private String getServicePrice(VBox cardVBox) {
        for (javafx.scene.Node node : cardVBox.getChildren()) {
            if (node instanceof Label) {
                Label label = (Label) node;
                if (label.getText().contains("VND") && !label.getText().equals("Total Monthly Cost:")) {
                    return label.getText();
                }
            }
        }
        return "0 VND / month";
    }

    // Lấy mô tả dịch vụ từ thẻ dịch vụ
    private String getServiceDescription(VBox cardVBox) {
        for (javafx.scene.Node node : cardVBox.getChildren()) {
            if (node instanceof Label) {
                Label label = (Label) node;
                if (label.isWrapText() && label.getPrefHeight() > 40) {
                    return label.getText();
                }
            }
        }
        return "";
    }

    // Lấy hình ảnh dịch vụ từ thẻ dịch vụ
    private ImageView getServiceImage(VBox cardVBox) {
        for (javafx.scene.Node node : cardVBox.getChildren()) {
            if (node instanceof ImageView) {
                return (ImageView) node;
            }
        }
        return new ImageView();
    }

    // Hiển thị dialog đăng ký dịch vụ
    private void showRegisterServiceDialog(String serviceName, String price, String description, Image image) {
        // Tải FXML của dialog đăng ký dịch vụ
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/utc2/apartmentmanagement/View/UserViews/RegisterServicesView.fxml"));

        // TODO: Xử lý sự kiện hiển thị dialog

    }

    // Đăng ký dịch vụ sau khi người dùng xác nhận
    private void registerService(RegisterServicesViewController controller) {
        // Lấy thông tin đăng ký từ controller
        LocalDate startDate = controller.getStartDate();
        int durationMonths = controller.getDurationMonths();
        double totalCost = controller.getTotalCost();

        // TODO: Lưu thông tin đăng ký vào cơ sở dữ liệu
        // Đoạn code này sẽ được thêm vào sau khi có model và service layer


        // Cập nhật bảng dịch vụ đã đăng ký
        refreshMyServicesTab();

        // Hiển thị thông báo thành công
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registration Successful");
        alert.setHeaderText("Service Registration Completed");
        alert.setContentText("You have successfully registered for the service. It will start on " +
                startDate.toString() + " for " + durationMonths + " months.");
        alert.showAndWait();
    }

    // Thiết lập bảng dịch vụ đã đăng ký
    private void setupTableView() {
        // TODO: Thiết lập các cột cho bảng và load dữ liệu
        // Sẽ được thêm sau khi có model



        // Thiết lập cột hành động (nút hủy dịch vụ)
        setupActionColumn();
    }

    // Thiết lập cột hành động với nút hủy dịch vụ
    private void setupActionColumn() {
        // TODO: Thêm nút hủy dịch vụ vào cột hành động


        // Sẽ được thêm sau khi có model
    }

    // Cập nhật tổng chi phí hàng tháng
    private void updateTotalMonthlyCost() {
        // TODO: Tính toán tổng chi phí từ các dịch vụ đã đăng ký
        // Sẽ được thêm sau khi có model

        // Giá trị mẫu
        totalMonthlyCostLabel.setText("1,450,000 VND");
    }

    // Làm mới tab dịch vụ đã đăng ký
    private void refreshMyServicesTab() {
        // TODO: Tải lại dữ liệu cho bảng dịch vụ
        // Sẽ được thêm sau khi có model

        // Cập nhật số lượng dịch vụ đang hoạt động
        updateActiveServicesCount();

        // Cập nhật tổng chi phí
        updateTotalMonthlyCost();
    }

    // Cập nhật số lượng dịch vụ đang hoạt động
    private void updateActiveServicesCount() {
        // TODO: Đếm số lượng dịch vụ đang hoạt động
        // Sẽ được thêm sau khi có model



        // Giá trị mẫu
        activeServicesLabel.setText("(5 active services)");
    }
}
