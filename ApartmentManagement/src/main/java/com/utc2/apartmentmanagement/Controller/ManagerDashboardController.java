package com.utc2.apartmentmanagement.Controller;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.utc2.apartmentmanagement.DAO.ApartmentDAO;
import com.utc2.apartmentmanagement.Model.Apartment;
import com.utc2.apartmentmanagement.Utils.TableUtils;

import java.text.DecimalFormat;
import java.util.List;

import static javafx.application.Application.launch;

public class ManagerDashboardController extends Application {
    @FXML
    private static StackPane contentPane;
    @Override
    public void start(Stage primaryStage) {
       TableView<Apartment> table = new TableView<>();

    TableColumn<Apartment, Integer> colApartmentId = new TableColumn<>("Apartment ID");
    colApartmentId.setCellValueFactory(new PropertyValueFactory<>("apartmentID"));
    colApartmentId.setPrefWidth(100);
    colApartmentId.setCellFactory(tc -> TableUtils.createCenteredCell());

    TableColumn<Apartment, Integer> colBuildingId = new TableColumn<>("Building ID");
    colBuildingId.setCellValueFactory(new PropertyValueFactory<>("buildingID"));
    colBuildingId.setPrefWidth(100);
    colBuildingId.setCellFactory(tc-> TableUtils.createCenteredCell());

    TableColumn<Apartment, String> colFloor = new TableColumn<>("Floor");
    colFloor.setCellValueFactory(new PropertyValueFactory<>("floors"));
    colFloor.setPrefWidth(100);
    colFloor.setCellFactory(tc-> TableUtils.createCenteredCell());

    TableColumn<Apartment, Double> colPrice = new TableColumn<>("Price ($)");
    colPrice.setCellValueFactory(new PropertyValueFactory<>("priceApartment"));
    colPrice.setPrefWidth(200);
    colPrice.setCellFactory(tc-> TableUtils.createCenteredCell());

        TableColumn<Apartment, Double> colArea = new TableColumn<>("Area (m²)");
        colArea.setCellValueFactory(new PropertyValueFactory<>("area"));
        colArea.setPrefWidth(100);
        colArea.setCellFactory(tc-> TableUtils.createCenteredCell());

        TableColumn<Apartment, Integer> colBedrooms = new TableColumn<>("Bedrooms");
        colBedrooms.setCellValueFactory(new PropertyValueFactory<>("bedRoom"));
        colBedrooms.setPrefWidth(100);
        colBedrooms.setCellFactory(tc-> TableUtils.createCenteredCell());



        TableColumn<Apartment, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colStatus.setPrefWidth(100);
        colStatus.setCellFactory(tc-> TableUtils.createCenteredCell());


        TableColumn<Apartment, Double> colMaintenanceFee = new TableColumn<>("Maintenance Fee");
        colMaintenanceFee.setCellValueFactory(new PropertyValueFactory<>("maintanceFee"));
        colMaintenanceFee.setPrefWidth(200);
        colMaintenanceFee.setCellFactory(tc-> TableUtils.createCenteredCell());

        // Định dạng cột Price (tách riêng để không bị ảnh hưởng bởi vòng lặp)
    colPrice.setCellFactory(column -> new TextFieldTableCell<Apartment, Double>() {
        private final DecimalFormat df = new DecimalFormat("#,##0"); // Định dạng số nguyên (không có E)
        @Override
        public void updateItem(Double item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
            } else {
                setText(df.format(item)); // Hiển thị số không có ký hiệu khoa học
            }
        }
    });


    table.getColumns().addAll(colApartmentId, colBuildingId, colFloor, colPrice, colArea, colBedrooms, colStatus, colMaintenanceFee);

    // Lấy dữ liệu từ Database
    ObservableList<Apartment> apartmentList = FXCollections.observableArrayList();
    List<Apartment> apartmentList1 = new ApartmentDAO().getAllApartments();
    apartmentList.addAll(apartmentList1);
    table.setItems(apartmentList);

    VBox vbox = new VBox(table);
    VBox.setVgrow(table, Priority.ALWAYS);

    // Hiển thị giao diện
    Scene scene = new Scene(vbox, 1080, 700);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Apartment List from Database");
    primaryStage.show();
    }

    public void showResidents() {
        contentPane.getChildren().setAll(new Label("👥 Quản lý Cư dân"));
    }

    public void showStaff() {
        contentPane.getChildren().setAll(new Label("👔 Quản lý Nhân viên"));
    }

    public void showServices() {
        contentPane.getChildren().setAll(new Label("🛠 Quản lý Dịch vụ"));
    }

    public void showMaintenanceFees() {
        contentPane.getChildren().setAll(new Label("💰 Quản lý Phí bảo trì"));
    }

    public void showContracts() {
        contentPane.getChildren().setAll(new Label("📜 Quản lý Hợp đồng"));
    }

    public void showReports() {
        contentPane.getChildren().setAll(new Label("📊 Báo cáo"));
    }

    public void logout() {
        System.out.println("Đăng xuất...");
        // Thêm code để quay lại màn hình đăng nhập
    }

    public static void main(String[] args) {
        launch(args);
    }


}
