package com.utc2.apartmentmanagement.Controller.Service;

import com.utc2.apartmentmanagement.Controller.User.UserDashboardController;
import com.utc2.apartmentmanagement.DAO.Service.ServiceDAO;
import com.utc2.apartmentmanagement.DAO.Service.ServiceRegistrationDAO;
import com.utc2.apartmentmanagement.DAO.User.ResidentDAO;
import com.utc2.apartmentmanagement.DAO.User.UserDAO;
import com.utc2.apartmentmanagement.Model.Service.Service;
import com.utc2.apartmentmanagement.Model.Session;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import lombok.Setter;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.List;


public class ServicesController implements Initializable {

    @FXML public Button registerService;
    @FXML private Label activeServicesLabel;
    @FXML private Label totalMonthlyCostLabel;
    @FXML private TableView<Map<String, Object>> servicesTableView;
    @FXML private TableColumn<Map<String, Object>, String> serviceNameColumn;
    @FXML private TableColumn<Map<String, Object>, String> descriptionColumn;
    @FXML private TableColumn<Map<String, Object>, LocalDate> startDateColumn;
    @FXML private TableColumn<Map<String, Object>, LocalDate> endDateColumn;
    @FXML private TableColumn<Map<String, Object>, String> statusColumn;
    @FXML private TableColumn<Map<String, Object>, String> priceColumn;
    @FXML private FlowPane servicesFlowPane;
    @FXML private Button CancelBtn1, CancelBtn2;

    @Setter
    private UserDashboardController parentController;

    @FXML
    private AnchorPane ServiceView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CancelBtn1.setOnAction(this::handleCloseButton);
        CancelBtn2.setOnAction(this::handleCloseButton);
        try {
            getRegistrationByResident();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        setupServiceCards();
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

        setUpColumnForRegistration();

        List<Map<String, Object>> result = new ServiceRegistrationDAO().getServiceRegistrationByApartmentId(apartment_id);

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


    public void handleCloseButton(ActionEvent event) {
        ((Pane) ServiceView.getParent()).getChildren().clear();
        parentController.getContentArea().getChildren().setAll(parentController.getDashboardNodes());
    }

    private void setupServiceCards() {
        List<Service> serviceList = new ServiceDAO().getAllServices();
        servicesFlowPane.getChildren().clear();
     //   servicesFlowPane.getChildren().addAll(createListVBox(serviceList));
    }

//    // Tạo service card với flow mới
//    private VBox createCardService(String serviceName, String description, String priceService, String unit, String addressImage){
//        VBox card = new VBox();
//        card.setPrefHeight(350);
//        card.setPrefWidth(300);
//        card.spacingProperty().setValue(10);
//        card.setPadding(new Insets(15, 10, 15, 10));
//        card.setStyle("-fx-padding: 10; -fx-background-color: #f8f8f8; -fx-border-color: #cccccc; -fx-border-radius: 8; -fx-background-radius: 8;");
//        DropShadow dropShadow = new DropShadow();
//        dropShadow.setRadius(5);
//        dropShadow.setOffsetX(0);
//        dropShadow.setOffsetY(5);
//        dropShadow.setColor(Color.rgb(0, 0, 0, 0.1));
//        card.setEffect(dropShadow);
//
//        // Hình ảnh dịch vụ
//        ImageView imageView;
//        try {
//            if (addressImage == null || addressImage.isBlank()) {
//                throw new IllegalArgumentException("Image path is null or empty");
//            }
//            Image image = new Image(addressImage, true);
//            imageView = new ImageView(image);
//        } catch (Exception e) {
//            Image placeholder = new Image(Objects.requireNonNull(getClass().getResource("/com/utc2/apartmentmanagement/Images/th.jpg")).toExternalForm());
//            imageView = new ImageView(placeholder);
//        }
//
//        imageView.setFitHeight(150);
//        imageView.setFitWidth(280);
//        imageView.setPreserveRatio(true);
//        card.getChildren().add(imageView);
//
//        // Tên dịch vụ
//        Label serviceNameLabel = new Label(serviceName);
//        serviceNameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1a936f;");
//        card.getChildren().add(serviceNameLabel);
//
//        // Giá dịch vụ
//        Label priceServiceLabel = new Label(priceService + " / " + unit);
//        priceServiceLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333;");
//        card.getChildren().add(priceServiceLabel);
//
//        // Mô tả dịch vụ
//        Label descriptionLabel = new Label(description);
//        descriptionLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #333333;");
//        descriptionLabel.setWrapText(true);
//        card.getChildren().add(descriptionLabel);
//
//        // Nút đăng ký dịch vụ - FIXED FLOW
//        Button register = new Button("Register Service");
//        register.setPrefHeight(40);
//        register.setPrefWidth(200);
//        register.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-color: #1A936F; -fx-text-fill: white;" +
//                "-fx-cursor: hand; -fx-background-radius: 5;");
//
//        register.setOnAction(e -> {
//            System.out.println("Đã nhấn đăng ký dịch vụ: " + serviceName);
//            ServiceInformationController controller = loadServiceInformationView(e);
//            if(controller != null){
//                // Truyền thông tin service vào popup
//                controller.setServiceInfo(serviceName, priceService, description, unit);
//                // Set callback để xử lý khi user confirm đăng ký
//            } else {
//                System.out.println("registerViewController null!");
//            }
//        });
//
//        card.getChildren().add(register);
//        card.setAlignment(Pos.CENTER);
//        return card;
//    }
//
//    private ServiceInformationController loadServiceInformationView(ActionEvent event){
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/utc2/apartmentmanagement/fxml/User/ServiceInformation.fxml"));
//        try{
//
//            Parent root = loader.load();
//            Stage stage = new Stage();
//            stage.setScene(new Scene(root, 1200, 800));
//            stage.setTitle("Service Information");
//            stage.show();
//            ServiceInformationController controller = loader.getController();
//
//            controller.setServicesController(this);
//            System.out.println("Load service information successful!");
//            return controller;
//        } catch (IOException e){
//            System.out.println("Load service information failed!");
//            e.printStackTrace();
//        }
//        return null;
//    }

//    private List<VBox> createListVBox(List<Service> serviceList){
//        List<VBox> vboxList = new ArrayList<>();
//        for(Service service : serviceList){
//            String serviceName = service.getServiceName();
//            String description = service.getDescription();
//            String priceService = String.format("%,.0f", service.getPrice());
//            String unit = service.getUnit();
//            VBox vbox = createCardService(serviceName, description, priceService, unit, null);
//            vboxList.add(vbox);
//        }
//        return vboxList;
//    }


}