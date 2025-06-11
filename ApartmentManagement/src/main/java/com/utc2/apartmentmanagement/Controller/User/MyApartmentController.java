package com.utc2.apartmentmanagement.Controller.User;

import com.utc2.apartmentmanagement.Controller.UserDashboardController;
import com.utc2.apartmentmanagement.DAO.*;
import com.utc2.apartmentmanagement.Model.Session;
import javafx.animation.FadeTransition;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import lombok.Setter;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.sql.Date;
import java.util.*;

public class MyApartmentController implements Initializable {

    @FXML public Label address;
    @FXML public Label price;
    @FXML public Label moveInDate;
    @FXML public Label fullNameResident;
    @FXML public Label apartmentId;
    @FXML public Label status;
    @FXML public Label floor;
    @FXML public Label area;
    @FXML public Label bedrooms;
    @FXML public Label maintenanceFee;

    @FXML public TableView<Map<String, Object>> tableService;
    @FXML public TableColumn<Map<String, Object>, String> serviceNameCol;
    @FXML public TableColumn<Map<String, Object>, LocalDate> startDateCol;
    @FXML public TableColumn<Map<String, Object>, LocalDate> endDateCol;
    @FXML public TableColumn<Map<String, Object>, String> priceCol;
    @FXML public TableColumn<Map<String, Object>, String> statusCol;

    @FXML public TableView<Map<String, Object>> tableBills;
    @FXML public TableColumn<Map<String, Object>, Integer> billIdCol;
    @FXML public TableColumn<Map<String, Object>, LocalDate> dateCol;
    @FXML public TableColumn<Map<String, Object>, LocalDate> dueDateCol;
    @FXML public TableColumn<Map<String, Object>, String> AmountCol;
    @FXML public TableColumn<Map<String, Object>, String> statusBillCol;

    @FXML private AnchorPane MyApartmentView;

    @Setter
    private UserDashboardController parentController;

    private String apartment_id;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // load data về căn hộ
        try {
            getInformationResident();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // load data về service
        try {
            getRegistrationByResident();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
//        load data về bill
        try {
            getBillByResident();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleCloseButton(ActionEvent event) {
        // Xoá apartment view
        ((Pane) MyApartmentView.getParent()).getChildren().clear();
        // Thêm lại dashboard nodes từ controller cha
        parentController.getContentArea().getChildren().setAll(parentController.getDashboardNodes());
    }

    private void getInformationResident() throws SQLException {

        String userName = Session.getUserName();
        int userId = new UserDAO().getIdByUserName(userName);
        Map<String, Object> apartmentInf = new ApartmentDAO().getInformation(userId);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.'); // Dùng dấu chấm thay vì dấu phẩy

        DecimalFormat df = new DecimalFormat("#,###", symbols);
        df.setMaximumFractionDigits(0);

        if(apartmentInf.isEmpty()){
            System.out.println("No apartment information found for user ID: " + userId);
            return;
        }

        fullNameResident.setText(apartmentInf.get("full_name").toString());
        moveInDate.setText(apartmentInf.get("move_in_date").toString());
        price.setText(df.format(Double.parseDouble(apartmentInf.get("price_apartment").toString())) + " VNĐ");
        address.setText(apartmentInf.get("address").toString());
        apartmentId.setText(apartmentInf.get("apartment_id").toString());
        status.setText(apartmentInf.get("status").toString());
        floor.setText(apartmentInf.get("floor").toString());
        area.setText(apartmentInf.get("area").toString() + " m2");
        bedrooms.setText(apartmentInf.get("bedrooms").toString());
        maintenanceFee.setText(df.format(Double.parseDouble(apartmentInf.get("maintenance_fee").toString())) + " VNĐ");
        apartment_id = apartmentInf.get("apartment_id").toString();
    }

    private void setUpColumnForRegistration(){
        serviceNameCol.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("service_name");
            return new SimpleStringProperty(value != null ? value.toString() : "null");
        });
        serviceNameCol.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");
        serviceNameCol.setPrefWidth(180);

        startDateCol.setCellValueFactory(cellData -> {
            Object dateObj = cellData.getValue().get("start_date");
            if (dateObj instanceof Date) {
                Date date = (Date) dateObj;
                return new SimpleObjectProperty<>(date.toLocalDate());
            }
            return new SimpleObjectProperty<>(null);
        });
        startDateCol.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");

        endDateCol.setCellValueFactory(cellData -> {
            Object dateObj = cellData.getValue().get("end_date");
            if (dateObj instanceof Date) {
                Date date = (Date) dateObj;
                return new SimpleObjectProperty<>(date.toLocalDate());
            }
            return new SimpleObjectProperty<>(null);
        });
        endDateCol.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");


        priceCol.setCellValueFactory(cellData -> {
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
        priceCol.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");


        statusCol.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("status");
            return new SimpleStringProperty(value != null ? value.toString() : "null");
        });
        statusCol.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");
    }

    private void getRegistrationByResident() throws SQLException {
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
        tableService.setItems(observableList);
    }

    private void setUpColumnForBill(){
        billIdCol.setCellValueFactory(data -> new SimpleObjectProperty<>((Integer) data.getValue().get("bill_id")));
        billIdCol.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");

        dateCol.setCellValueFactory(data -> {
            Date date = (Date) data.getValue().get("payment_date");
            return new ReadOnlyObjectWrapper<>(date.toLocalDate());
        });
        dateCol.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");


        AmountCol.setCellValueFactory(data -> {
            double price = (double) data.getValue().get("total_amount");
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setGroupingSeparator('.');
            DecimalFormat df = new DecimalFormat("#,###", symbols);
            df.setMaximumFractionDigits(0);
            return new SimpleStringProperty(df.format(price) + " VNĐ");
        });
        AmountCol.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");

        statusBillCol.setCellValueFactory(data -> new SimpleStringProperty((String) data.getValue().get("status")));
        statusBillCol.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");
    }

    private void getBillByResident() throws SQLException {
        List<Map<String, Object>> result = new BillsDAO().getBillByApartmentId(apartment_id);
        setUpColumnForBill();
        ObservableList<Map<String, Object>> observableList = FXCollections.observableArrayList(result);
        tableBills.setItems(observableList);
    }

    double x = 0, y = 0;
    @FXML
    public void handleViewDetailsBtn(ActionEvent actionEvent) {
        try {
            URL url = getClass().getResource("/com/utc2/apartmentmanagement/fxml/User/ViewDetailMyApartmentView.fxml");
            FXMLLoader loader = new FXMLLoader(url);
            Parent contentRoot = loader.load();

            // Tạo BorderPane để có layout tốt hơn
            BorderPane root = new BorderPane();

            // Tạo header có nút đóng
            HBox header = new HBox();
            header.setAlignment(Pos.CENTER_RIGHT);
            header.setPadding(new Insets(5, 5, 5, 10));
            header.setStyle("-fx-background-color: #2D3447;");

            // Thêm tiêu đề vào bên trái header
            Label titleLabel = new Label("Chi tiết căn hộ");
            titleLabel.setTextFill(Color.WHITE);
            titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            HBox.setHgrow(titleLabel, Priority.ALWAYS);
            header.getChildren().add(titleLabel);

            // Tạo nút đóng
            Button closeButton = new Button("X");
            closeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-weight: bold;");
            closeButton.setOnAction(e -> {
                // Thêm hiệu ứng fade out trước khi đóng cửa sổ
                FadeTransition fadeOut = new FadeTransition(Duration.millis(200), root);
                fadeOut.setFromValue(1);
                fadeOut.setToValue(0);
                fadeOut.setOnFinished(event -> ((Stage) root.getScene().getWindow()).close());
                fadeOut.play();
            });

            // Hiệu ứng hover cho nút đóng
            closeButton.setOnMouseEntered(e -> closeButton.setStyle("-fx-background-color: #FF5555; -fx-text-fill: white; -fx-font-weight: bold;"));
            closeButton.setOnMouseExited(e -> closeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-weight: bold;"));
            header.getChildren().add(closeButton);

            // Đặt header và nội dung vào BorderPane
            root.setTop(header);
            root.setCenter(contentRoot);

            // Thêm styling cho container
            root.setStyle("-fx-background-color: white; " +
                    "-fx-border-color: #2D3447; " +
                    "-fx-border-width: 1px; " +
                    "-fx-border-radius: 5px; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 15, 0, 0, 0);");

            // Stage của dashboard!
            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);

            // Kéo cửa sổ
            header.setOnMousePressed(event -> {
                x = event.getSceneX();
                y = event.getSceneY();
            });

            header.setOnMouseDragged(event -> {
                stage.setX(event.getScreenX() - x);
                stage.setY(event.getScreenY() - y);
            });

            // Hiệu ứng fade in
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), root);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();

            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}