package com.utc2.apartmentmanagement.Controller.User;

import com.utc2.apartmentmanagement.Controller.MyProfileController;
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

import javafx.scene.control.cell.MapValueFactory;
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
    @FXML
    private AnchorPane myApartmentPane;

    @FXML private Button ViewFullDetailsBtn;

    @FXML
    private VBox apartmentsContainer;

    @FXML
    private ScrollPane apartmentsScrollPane;

    @Setter
    private UserDashboardController parentController;

    @FXML
    private AnchorPane MyApartmentView;


    public void handleCloseButton(ActionEvent event) {
        // Xoá apartment view
        ((Pane) MyApartmentView.getParent()).getChildren().clear();
        // Thêm lại dashboard nodes từ controller cha
        parentController.getContentArea().getChildren().setAll(parentController.getDashboardNodes());
    }
    private String apartment_id;
    private void getInformationResident() throws SQLException {
        String userName = Session.getUserName();
        int userId = new UserDAO().getIdByUserName(userName);
        Map<String, Object> apartmentInf = new ApartmentDAO().getInformation(userId);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.'); // Dùng dấu chấm thay vì dấu phẩy

        DecimalFormat df = new DecimalFormat("#,###", symbols);
        df.setMaximumFractionDigits(0);
        fullNameResident.setText(apartmentInf.get("full_name").toString());
        moveInDate.setText(apartmentInf.get("move_in_date").toString());
        price.setText(df.format(Double.parseDouble(apartmentInf.get("price_apartment").toString())));
        address.setText(apartmentInf.get("address").toString());
        apartmentId.setText(apartmentInf.get("apartment_id").toString());
        status.setText(apartmentInf.get("status").toString());
        floor.setText(apartmentInf.get("floor").toString());
        area.setText(apartmentInf.get("area").toString());
        bedrooms.setText(apartmentInf.get("bedrooms").toString());
        maintenanceFee.setText(df.format(Double.parseDouble(apartmentInf.get("maintenance_fee").toString())));
        apartment_id = apartmentInf.get("apartment_id").toString();
    }

    private void setUpColumnForRegistration(){
        serviceNameCol.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("service_name");
            return new SimpleStringProperty(value != null ? value.toString() : "null");
        });
        startDateCol.setCellValueFactory(cellData -> {
            Object dateObj = cellData.getValue().get("start_date");
            if (dateObj instanceof Date) {
                Date date = (Date) dateObj;
                return new SimpleObjectProperty<>(date.toLocalDate());
            }
            return new SimpleObjectProperty<>(null);
        });

        endDateCol.setCellValueFactory(cellData -> {
            Object dateObj = cellData.getValue().get("end_date");
            if (dateObj instanceof Date) {
                Date date = (Date) dateObj;
                return new SimpleObjectProperty<>(date.toLocalDate());
            }
            return new SimpleObjectProperty<>(null);
        });

        priceCol.setCellValueFactory(cellData -> {
            Object priceObj = cellData.getValue().get("price_service");
            if (priceObj != null) {
                if (priceObj instanceof Double) {
                    Double price = (Double) priceObj;
                    return new SimpleStringProperty(String.format("%,.0f", price));
                } else {
                    // Fallback for other number types
                    return new SimpleStringProperty(priceObj.toString());
                }
            } else {
                return new SimpleStringProperty("null");
            }
        });

        statusCol.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("status");
            return new SimpleStringProperty(value != null ? value.toString() : "null");
        });
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

        dateCol.setCellValueFactory(data -> {
            java.sql.Date date = (java.sql.Date) data.getValue().get("payment_date");
            return new ReadOnlyObjectWrapper<>(date.toLocalDate());
        });

        dueDateCol.setCellValueFactory(data -> {
            java.sql.Date date = (java.sql.Date) data.getValue().get("due_date");
            return new ReadOnlyObjectWrapper<>(date != null ? date.toLocalDate() : null);
        });

        AmountCol.setCellValueFactory(data -> {
            double price = (double) data.getValue().get("total_amount");
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setGroupingSeparator('.');
            DecimalFormat df = new DecimalFormat("#,###", symbols);
            df.setMaximumFractionDigits(0);
            return new SimpleStringProperty(df.format(price));
        });

        statusBillCol.setCellValueFactory(data -> new SimpleStringProperty((String) data.getValue().get("status")));
    }

    private void getBillByResident() throws SQLException {
        List<Map<String, Object>> result = new BillsDAO().getBillByApartmentId(apartment_id);
        setUpColumnForBill();
        ObservableList<Map<String, Object>> observableList = FXCollections.observableArrayList(result);
        tableBills.setItems(observableList);
    }
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
    double x = 0, y = 0;
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


//    // Cấu trúc dữ liệu cho căn hộ
//    class ApartmentData {
//        String apartmentId;         // Mã căn hộ
//        String status;              // Trạng thái: occupied, available, maintenance, reserved
//        String buildingName;        // Tên tòa nhà
//        String buildingAddress;     // Địa chỉ tòa nhà
//        int buildingId;             // ID tòa nhà
//        int floor;                  // Tầng
//        double area;                // Diện tích (m²)
//        int bedrooms;               // Số phòng ngủ
//        double maintenanceFee;      // Phí bảo trì
//        double propertyValue;       // Giá trị căn hộ
//        String moveInDate;          // Ngày chuyển vào
//        String primaryResident;     // Cư dân chính
//        List<BillData> bills = new ArrayList<>();       // Danh sách hóa đơn
//        List<ServiceData> services = new ArrayList<>(); // Danh sách dịch vụ
//    }
//
//    // Cấu trúc dữ liệu cho hóa đơn
//    private class BillData {
//        int billId;                 // Mã hóa đơn
//        String billingDate;         // Ngày lập hóa đơn
//        String dueDate;             // Ngày đến hạn
//        double totalAmount;         // Tổng tiền
//        String status;              // Trạng thái: pending, paid, overdue, cancelled
//    }
//
//    // Cấu trúc dữ liệu cho dịch vụ
//    private class ServiceData {
//        String serviceName;         // Tên dịch vụ
//        String startDate;           // Ngày bắt đầu
//        String endDate;             // Ngày kết thúc
//        double price;               // Giá dịch vụ
//        String status;              // Trạng thái: active, cancelled, expired
//    }
//
//    // Danh sách căn hộ mẫu
//    private List<ApartmentData> sampleApartments = new ArrayList<>();
//
//    @FXML
//    public void initialize() {
//        // Tạo dữ liệu mẫu
//        createSampleData();
//
//        // Xóa thẻ mẫu đã có trong FXML (nếu có)
//        apartmentsContainer.getChildren().clear();
//
//        // Tạo các thẻ căn hộ động
//        populateApartments();
//    }
//
//    /**
//     * Tạo dữ liệu mẫu cho căn hộ
//     */
//    private void createSampleData() {
//        // Căn hộ mẫu 1
//        ApartmentData apt1 = new ApartmentData();
//        apt1.apartmentId = "A101";
//        apt1.status = "occupied";
//        apt1.buildingName = "Chung Cư Xanh";
//        apt1.buildingAddress = "123 Đường Lê Văn Lương, Quận 7, TP.HCM";
//        apt1.buildingId = 1;
//        apt1.floor = 1;
//        apt1.area = 75.5;
//        apt1.bedrooms = 2;
//        apt1.maintenanceFee = 1500000;
//        apt1.propertyValue = 1510000000;
//        apt1.moveInDate = "01/06/2022";
//        apt1.primaryResident = "Vũ Đức Cư";
//
//        // Thêm hóa đơn cho căn hộ 1
//        BillData bill1 = new BillData();
//        bill1.billId = 1;
//        bill1.billingDate = "01/05/2023";
//        bill1.dueDate = "15/05/2023";
//        bill1.totalAmount = 2500000;
//        bill1.status = "pending";
//        apt1.bills.add(bill1);
//
//        BillData bill2 = new BillData();
//        bill2.billId = 16;
//        bill2.billingDate = "01/01/2023";
//        bill2.dueDate = "15/01/2023";
//        bill2.totalAmount = 2350000;
//        bill2.status = "paid";
//        apt1.bills.add(bill2);
//
//        // Thêm dịch vụ cho căn hộ 1
//        ServiceData service1 = new ServiceData();
//        service1.serviceName = "Giữ xe ô tô";
//        service1.startDate = "01/01/2023";
//        service1.endDate = "31/12/2023";
//        service1.price = 500000;
//        service1.status = "active";
//        apt1.services.add(service1);
//
//        ServiceData service2 = new ServiceData();
//        service2.serviceName = "Giặt ủi";
//        service2.startDate = "15/02/2023";
//        service2.endDate = "15/08/2023";
//        service2.price = 300000;
//        service2.status = "active";
//        apt1.services.add(service2);
//
//        // Thêm vào danh sách căn hộ mẫu
//        sampleApartments.add(apt1);
//
//        // Căn hộ mẫu 2
//        ApartmentData apt2 = new ApartmentData();
//        apt2.apartmentId = "B201";
//        apt2.status = "available";
//        apt2.buildingName = "Tháp Sen";
//        apt2.buildingAddress = "456 Đường Nguyễn Văn Trỗi, Quận Phú Nhuận, TP.HCM";
//        apt2.buildingId = 2;
//        apt2.floor = 2;
//        apt2.area = 80.3;
//        apt2.bedrooms = 3;
//        apt2.maintenanceFee = 1600000;
//        apt2.propertyValue = 1766600000;
//        apt2.moveInDate = "01/09/2022";
//        apt2.primaryResident = "Bùi Văn Nhà";
//
//        // Thêm hóa đơn cho căn hộ 2
//        BillData bill3 = new BillData();
//        bill3.billId = 3;
//        bill3.billingDate = "01/05/2023";
//        bill3.dueDate = "15/05/2023";
//        bill3.totalAmount = 3200000;
//        bill3.status = "pending";
//        apt2.bills.add(bill3);
//
//        // Thêm dịch vụ cho căn hộ 2
//        ServiceData service3 = new ServiceData();
//        service3.serviceName = "Internet";
//        service3.startDate = "15/01/2023";
//        service3.endDate = "15/07/2023";
//        service3.price = 150000;
//        service3.status = "active";
//        apt2.services.add(service3);
//
//        // Thêm vào danh sách căn hộ mẫu
//        sampleApartments.add(apt2);
//    }
//
//    /**
//     * Tạo các thẻ căn hộ và hiển thị trên giao diện
//     */
//    private void populateApartments() {
//        // Định dạng tiền tệ
//        NumberFormat currencyFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
//
//        // Tạo thẻ cho mỗi căn hộ
//        for (ApartmentData apartment : sampleApartments) {
//            try {
//                // Tải template từ FXML file
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/utc2/apartmentmanagement/views/ApartmentCardView.fxml"));
//                AnchorPane apartmentCard = loader.load();
//
//                // Lấy các thành phần UI từ template
//                Label apartmentIdLabel = (Label) findNodeById(apartmentCard, "apartmentIdLabel");
//                Label statusLabel = (Label) findNodeById(apartmentCard, "statusLabel");
//                Label buildingNameLabel = (Label) findNodeById(apartmentCard, "buildingNameLabel");
//                Label floorValueLabel = (Label) findNodeById(apartmentCard, "floorValueLabel");
//                Label areaValueLabel = (Label) findNodeById(apartmentCard, "areaValueLabel");
//                Label bedroomsValueLabel = (Label) findNodeById(apartmentCard, "bedroomsValueLabel");
//                Label feeValueLabel = (Label) findNodeById(apartmentCard, "feeValueLabel");
//                Label buildingAddressLabel = (Label) findNodeById(apartmentCard, "buildingAddressLabel");
//                Label propertyValueLabel = (Label) findNodeById(apartmentCard, "propertyValueLabel");
//                Label moveInDateLabel = (Label) findNodeById(apartmentCard, "moveInDateLabel");
//                Label primaryResidentLabel = (Label) findNodeById(apartmentCard, "primaryResidentLabel");
//                Button viewDetailsButton = (Button) findNodeById(apartmentCard, "viewDetailsButton");
//                Button manageButton = (Button) findNodeById(apartmentCard, "manageButton");
//
//                // Cập nhật dữ liệu cho thẻ căn hộ
//                apartmentIdLabel.setText(apartment.apartmentId);
//                statusLabel.setText(apartment.status);
//                statusLabel.getStyleClass().add(apartment.status); // Thêm class CSS tương ứng với status
//                buildingNameLabel.setText(apartment.buildingName);
//                floorValueLabel.setText(String.valueOf(apartment.floor));
//                areaValueLabel.setText(apartment.area + " m²");
//                bedroomsValueLabel.setText(String.valueOf(apartment.bedrooms));
//                feeValueLabel.setText(currencyFormat.format(apartment.maintenanceFee) + " VND");
//                buildingAddressLabel.setText(apartment.buildingAddress);
//                propertyValueLabel.setText(currencyFormat.format(apartment.propertyValue) + " VND");
//                moveInDateLabel.setText(apartment.moveInDate);
//                primaryResidentLabel.setText(apartment.primaryResident);
//
//                // Thiết lập sự kiện cho các nút
//                viewDetailsButton.setOnAction(event -> openFullDetails(apartment));
//                manageButton.setOnAction(event -> manageApartment(apartment));
//
//                // Tạo dữ liệu cho bảng hóa đơn nếu có
//                TableView<BillData> billsTable = (TableView<BillData>) findNodeById(apartmentCard, "billsTable");
//                if (billsTable != null) {
//                    setupBillsTable(billsTable, apartment.bills, currencyFormat);
//                }
//
//                // Tạo dữ liệu cho bảng dịch vụ nếu có
//                TableView<ServiceData> servicesTable = (TableView<ServiceData>) findNodeById(apartmentCard, "servicesTable");
//                if (servicesTable != null) {
//                    setupServicesTable(servicesTable, apartment.services, currencyFormat);
//                }
//
//                // Thêm thẻ vào container
//                apartmentsContainer.getChildren().add(apartmentCard);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//                System.err.println("Lỗi khi tạo thẻ căn hộ: " + e.getMessage());
//            }
//        }
//    }
//
//    /**
//     * Tìm node theo ID trong parent node
//     * @param parent Node cha
//     * @param id ID của node cần tìm
//     * @return Node với ID tương ứng hoặc null nếu không tìm thấy
//     */
//    private javafx.scene.Node findNodeById(javafx.scene.Parent parent, String id) {
//        for (javafx.scene.Node node : parent.getChildrenUnmodifiable()) {
//            if (id.equals(node.getId())) {
//                return node;
//            }
//
//            if (node instanceof javafx.scene.Parent) {
//                javafx.scene.Node result = findNodeById((javafx.scene.Parent) node, id);
//                if (result != null) {
//                    return result;
//                }
//            }
//        }
//        return null;
//    }
//
//    /**
//     * Thiết lập bảng hóa đơn
//     * @param billsTable Bảng hóa đơn
//     * @param bills Danh sách hóa đơn
//     * @param currencyFormat Định dạng tiền tệ
//     */
//    private void setupBillsTable(TableView<BillData> billsTable, List<BillData> bills, NumberFormat currencyFormat) {
//        // Xóa dữ liệu cũ
//        billsTable.getItems().clear();
//
//        // Thiết lập cell factories cho các cột
//        for (TableColumn<BillData, ?> column : billsTable.getColumns()) {
//            switch (column.getText()) {
//                case "Bill ID":
//                    ((TableColumn<BillData, Integer>) column).setCellValueFactory(
//                            cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().billId).asObject());
//                    break;
//                case "Date":
//                    ((TableColumn<BillData, String>) column).setCellValueFactory(
//                            cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().billingDate));
//                    break;
//                case "Due Date":
//                    ((TableColumn<BillData, String>) column).setCellValueFactory(
//                            cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().dueDate));
//                    break;
//                case "Amount":
//                    ((TableColumn<BillData, String>) column).setCellValueFactory(
//                            cellData -> new javafx.beans.property.SimpleStringProperty(
//                                    currencyFormat.format(cellData.getValue().totalAmount) + " VND"));
//                    break;
//                case "Status":
//                    ((TableColumn<BillData, String>) column).setCellValueFactory(
//                            cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().status));
//                    break;
//            }
//        }
//
//        // Thêm dữ liệu vào bảng
//        billsTable.getItems().addAll(bills);
//    }
//
//    /**
//     * Thiết lập bảng dịch vụ
//     * @param servicesTable Bảng dịch vụ
//     * @param services Danh sách dịch vụ
//     * @param currencyFormat Định dạng tiền tệ
//     */
//    private void setupServicesTable(TableView<ServiceData> servicesTable, List<ServiceData> services, NumberFormat currencyFormat) {
//        // Xóa dữ liệu cũ
//        servicesTable.getItems().clear();
//
//        // Thiết lập cell factories cho các cột
//        for (TableColumn<ServiceData, ?> column : servicesTable.getColumns()) {
//            switch (column.getText()) {
//                case "Service Name":
//                    ((TableColumn<ServiceData, String>) column).setCellValueFactory(
//                            cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().serviceName));
//                    break;
//                case "Start Date":
//                    ((TableColumn<ServiceData, String>) column).setCellValueFactory(
//                            cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().startDate));
//                    break;
//                case "End Date":
//                    ((TableColumn<ServiceData, String>) column).setCellValueFactory(
//                            cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().endDate));
//                    break;
//                case "Price":
//                    ((TableColumn<ServiceData, String>) column).setCellValueFactory(
//                            cellData -> new javafx.beans.property.SimpleStringProperty(
//                                    currencyFormat.format(cellData.getValue().price) + " VND"));
//                    break;
//                case "Status":
//                    ((TableColumn<ServiceData, String>) column).setCellValueFactory(
//                            cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().status));
//                    break;
//            }
//        }
//
//        // Thêm dữ liệu vào bảng
//        servicesTable.getItems().addAll(services);
//    }
//
//    /**
//     * Mở cửa sổ chi tiết cho căn hộ
//     * @param apartment Dữ liệu căn hộ cần hiển thị
//     */
//    private void openFullDetails(ApartmentData apartment) {
//        try {
//            // Tải file FXML chi tiết căn hộ
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/utc2/apartmentmanagement/views/Detail.fxml"));
//            AnchorPane detailsPane = loader.load();
//
//            // Lấy controller
//            ViewDetailMyApartmentController controller = loader.getController();
//
//            // Truyền dữ liệu căn hộ cho controller
//            controller.setApartmentData(apartment);
//
//            // Tạo cửa sổ modal mới
//            Stage detailsStage = new Stage();
//            detailsStage.setTitle("Chi tiết căn hộ " + apartment.apartmentId);
//            detailsStage.initModality(Modality.APPLICATION_MODAL);
//            detailsStage.setScene(new Scene(detailsPane));
//
//            // Hiển thị cửa sổ
//            detailsStage.showAndWait();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.err.println("Lỗi khi mở cửa sổ chi tiết: " + e.getMessage());
//        }
//    }
//
//    /**
//     * Mở giao diện quản lý căn hộ
//     * @param apartment Căn hộ cần quản lý
//     */
//    private void manageApartment(ApartmentData apartment) {
//        // TODO: Triển khai giao diện quản lý căn hộ
//        System.out.println("Quản lý căn hộ: " + apartment.apartmentId);
//    }
//
//    /**
//     * Tải dữ liệu căn hộ từ cơ sở dữ liệu
//     */
//    private void loadApartmentData() {
//        // TODO: Lấy ID người dùng hiện tại và truy vấn các căn hộ liên quan
//        // Ví dụ query SQL:
//        /*
//        String currentUserId = UserSession.getCurrentUser().getUserId();
//        String query = "SELECT a.apartment_id, a.building_id, a.floor, a.area, a.bedrooms, a.status, " +
//                       "a.maintenance_fee, a.price_apartment, b.building_name, b.address, " +
//                       "r.full_name, r.move_in_date, r.is_primary_resident " +
//                       "FROM Apartment a " +
//                       "JOIN Building b ON a.building_id = b.building_id " +
//                       "JOIN Resident r ON a.apartment_id = r.apartment_id " +
//                       "WHERE r.user_id = ?";
//        */
//    }
}