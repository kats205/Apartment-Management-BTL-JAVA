package com.utc2.apartmentmanagement.Controller.User;

import com.utc2.apartmentmanagement.Controller.UserDashboardController;
import com.utc2.apartmentmanagement.DAO.ResidentDAO;
import com.utc2.apartmentmanagement.DAO.ServiceRegistrationDAO;
import com.utc2.apartmentmanagement.DAO.UserDAO;
import com.utc2.apartmentmanagement.Model.Session;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lombok.Setter;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ServicesController implements Initializable {

    @FXML
    private TabPane servicesTabPane;

    @FXML
    private Label activeServicesLabel;

    @FXML
    private Label totalMonthlyCostLabel;

    @FXML
    private TableView<Map<String, Object>> servicesTableView;

    @FXML
    private TableColumn<Map<String, Object>, String> serviceNameColumn;

    @FXML
    private TableColumn<Map<String, Object>, String> descriptionColumn;

    @FXML
    private TableColumn<Map<String, Object>, LocalDate> startDateColumn;

    @FXML
    private TableColumn<Map<String, Object>, LocalDate> endDateColumn;


    @FXML
    private TableColumn<Map<String, Object>, String> statusColumn;

    @FXML
    private TableColumn<Map<String, Object>, String> priceColumn;

    @FXML
    private FlowPane servicesFlowPane;

    @FXML
    private Button CancelBtn;

    @FXML
    private Button CloseBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CloseBtn.setOnAction(this::handleCloseButton);
        try {
            getRegistrationByResident();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setUpColumnForRegistration(){
        serviceNameColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("service_name");
            return new SimpleStringProperty(value != null ? value.toString() : "null");
        });
        serviceNameColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");

        startDateColumn.setCellValueFactory(cellData -> {
            Object dateObj = cellData.getValue().get("start_date");
            if (dateObj instanceof Date) {
                Date date = (Date) dateObj;
                return new SimpleObjectProperty<>(date.toLocalDate());
            }
            return new SimpleObjectProperty<>(null);
        });
        startDateColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");

        endDateColumn.setCellValueFactory(cellData -> {
            Object dateObj = cellData.getValue().get("end_date");
            if (dateObj instanceof Date) {
                Date date = (Date) dateObj;
                return new SimpleObjectProperty<>(date.toLocalDate());
            }
            return new SimpleObjectProperty<>(null);
        });
        endDateColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");

        priceColumn.setCellValueFactory(cellData -> {
            Object priceObj = cellData.getValue().get("price_service");
            if (priceObj != null) {
                if (priceObj instanceof Double) {
                    Double price = (Double) priceObj;
                    return new SimpleStringProperty(String.format("%,.0f", price) + " VNĐ");
                } else {
                    // Fallback for other number types
                    return new SimpleStringProperty(priceObj.toString());
                }
            } else {
                return new SimpleStringProperty("null");
            }
        });
        priceColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");

        statusColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("status");
            return new SimpleStringProperty(value != null ? value.toString() : "null");
        });
        statusColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");

        descriptionColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("description");
            return new SimpleStringProperty(value != null ? value.toString() : "null");
        });
        descriptionColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");

    }


    private void getRegistrationByResident() throws SQLException {
        int userId = new UserDAO().getIdByUserName(Session.getUserName());
        String apartment_id = new ResidentDAO().getApartmentIdByUserID(userId);
        System.out.println("Apartment ID: " + apartment_id);

        // Step 1: Configure the table columns FIRST
        setUpColumnForRegistration();

        // Step 2: THEN get and set the data
        List<Map<String, Object>> result = new ServiceRegistrationDAO().getServiceRegistrationByApartmentId(apartment_id);

        // Debug: Print the data to verify
        System.out.println("Result size: " + result.size());
        if (!result.isEmpty()) {
            System.out.println("First row keys: " + result.get(0).keySet());
        }
        ObservableList<Map<String, Object>> observableList = FXCollections.observableArrayList(result);
        servicesTableView.setItems(observableList);

        activeServicesLabel.setText(result.size() + " active services");
        double total = 0;
        for(Map<String, Object> map : result){
            if(map.get("status").equals("active")){
                total += (double) map.get("price_service");
            }
        }
        totalMonthlyCostLabel.setText(String.format("%,.0f", total) + "VNĐ");
    }

    // Khởi tạo controller
    public void initialize() {
        setupServiceCards();
        setupTableView();
        updateTotalMonthlyCost();
    }

    @Setter
    private UserDashboardController parentController;

    @FXML
    private AnchorPane ServiceView;


    public void handleCloseButton(ActionEvent event) {
        // Xoá apartment view
        ((Pane) ServiceView.getParent()).getChildren().clear();
        // Thêm lại dashboard nodes từ controller cha
        parentController.getContentArea().getChildren().setAll(parentController.getDashboardNodes());
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