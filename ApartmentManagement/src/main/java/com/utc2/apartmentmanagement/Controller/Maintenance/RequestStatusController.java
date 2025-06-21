package com.utc2.apartmentmanagement.Controller.Maintenance;

import com.utc2.apartmentmanagement.Controller.User.UserDashboardController;
import com.utc2.apartmentmanagement.DAO.DatabaseConnection;
import com.utc2.apartmentmanagement.DAO.Maintenance.MaintenanceRequestDAO;
import com.utc2.apartmentmanagement.DAO.User.ResidentDAO;
import com.utc2.apartmentmanagement.DAO.User.UserDAO;
import com.utc2.apartmentmanagement.Model.Session;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import lombok.Setter;


import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class RequestStatusController implements Initializable {

    @FXML public TabPane complaintsTabPane;
    @FXML public Label maintenanceCountLabel;
    @FXML public ComboBox maintenanceStatusFilter;
    @FXML public ComboBox maintenancePriorityFilter;
    @FXML public TextField maintenanceApartmentFilter;
    @FXML public Button filterMaintenanceButton;
    @FXML public Button clearMaintenanceFilterButton;
    @FXML public Button refreshMaintenanceButton;
    @FXML public Button addMaintenanceButton;
    @FXML public Button editMaintenanceButton;
    @FXML public Button deleteMaintenanceButton;
    @FXML public TableView<Map<String, Object>> maintenanceTable;
    @FXML public TableColumn<Map<String, Object>, String> maintenanceIdColumn;
    @FXML public TableColumn<Map<String, Object>, String> maintenanceApartmentColumn;
    @FXML public TableColumn<Map<String, Object>, String> maintenanceResidentColumn;
    @FXML public TableColumn<Map<String, Object>, String> maintenanceIssueTypeColumn;
    @FXML public TableColumn<Map<String, Object>, String> maintenanceDescriptionColumn;
    @FXML public  TableColumn<Map<String, Object>, String> maintenanceStatusColumn;
    @FXML public TableColumn<Map<String, Object>, String> maintenancePriorityColumn;
    @FXML public TableColumn<Map<String, Object>, LocalDate> maintenanceRequestDateColumn;
    @FXML public TableColumn<Map<String, Object>, String> maintenanceAssignedStaffColumn;
    @FXML public TableColumn<Map<String, Object>, LocalDate> maintenanceCompletionDateColumn;

    @FXML public Button closeMaintenanceButton;
    @FXML public Label serviceComplaintsCountLabel;
    @FXML public ComboBox serviceStatusFilter;
    @FXML public ComboBox serviceTypeFilter;
    @FXML public TextField serviceApartmentFilter;
    @FXML public Button filterServiceButton;
    @FXML public Button clearServiceFilterButton;
    @FXML public Button refreshServiceButton;
    @FXML public Button addServiceComplaintButton;
    @FXML public Button editServiceComplaintButton;
    @FXML public Button deleteServiceComplaintButton;
    @FXML public TableView serviceTable;
    @FXML public TableColumn serviceComplaintIdColumn;
    @FXML public TableColumn serviceNameColumn;
    @FXML public TableColumn serviceApartmentColumn;
    @FXML public TableColumn serviceComplaintDescColumn;
    @FXML public TableColumn serviceStatusColumn;
    @FXML public TableColumn serviceSeverityColumn;
    @FXML public TableColumn serviceSubmitDateColumn;
    @FXML public TableColumn serviceResolutionDateColumn;
    @FXML public Button closeServiceButton;
    @FXML public AnchorPane RequestStatusView;

    @Setter
    private UserDashboardController parentController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        loadDataMaintenanceRequest();
        loadDataComplaint();

        loadPrioritiesMaintenance();
        loadStatusMaintenanceTypes();

        setUpConTextMenuMaintenanceMenu();
    }
    private void setUpConTextMenuMaintenanceMenu(){
        ContextMenu contextMenu = new ContextMenu();

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(event -> {
            try {
                handleDeleteMaintenance();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        contextMenu.getItems().addAll(deleteItem);

        maintenanceTable.setRowFactory(tv -> {
            TableRow<Map<String,Object>> row = new TableRow<>();
            row.setOnContextMenuRequested(event -> {
                if(!row.isEmpty()){
                    maintenanceTable.getSelectionModel().select(row.getIndex());
                    contextMenu.show(row,event.getSceneX(),event.getSceneY());
                }
            });
            row.setOnMouseClicked(event -> {
                if(event.getButton() == MouseButton.PRIMARY){
                    contextMenu.hide();
                }
            });
            return row;
        });
    }

    // TODO: Load dữ liệu Maintenace Request
    public void loadDataMaintenanceRequest(){
        try {
            getMaintenanceRequestByResidentID();
            getTotalMaintainenceRequest();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    // TODO: Load dữ liệu Complaint
    public void loadDataComplaint(){
        // chưa có load bảng dữ liệu
        loadStatusComplaintTypes();
        loadServicesType();
    }

    // TODO: Load Status Maintenance Types
    private void loadStatusMaintenanceTypes() {
        maintenanceStatusFilter.getItems().addAll(
            "Pending", "Assigned", "In Progress", "Completed", "Cancelled", "All Status"
        );
        maintenanceStatusFilter.setValue("All Status");

    }
    // TODO: Load Priority Maintenance (tạm thời dùng cho Complaint luôn )
    private void loadPrioritiesMaintenance() {
        maintenancePriorityFilter.getItems().addAll(
            "Low", "Medium", "High", "Urgent", "All Priority"
        );
        maintenanceStatusFilter.setValue("All Status"); // set giá trị mặc định "All Status"

    }

    // TODO: Load Status Complaint
    private void loadStatusComplaintTypes() {
        serviceStatusFilter.getItems().addAll(
                "pending", "assigned", "in_progress", "completed", "cancelled", "All Status"
        );
    }
    // TODO: Load Services Types Complaint // chưa thêm loại gì á
    private void loadServicesType() {
        serviceTypeFilter.getItems().addAll(
                "low", "medium", "high", "urgent", "All Priority"
        );
    }


    // TODO: Gán dữ liệu MAINTAINECE REQUEST vào các ô dữ liệu
    private void setUpColumnForMaintaineceRequest(){
        // Request ID
        maintenanceIdColumn.setCellValueFactory(cellData ->{
            Object value = cellData.getValue().get("request_id");
            return new SimpleStringProperty(value != null ? value.toString() : "null");
        });
        maintenanceIdColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");

        // Description
        maintenanceDescriptionColumn.setCellValueFactory(cellData ->{
            Object value = cellData.getValue().get("description");
            return new SimpleStringProperty(value != null ? value.toString() : "null");
        });
        maintenanceDescriptionColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");

        // Status
        maintenanceStatusColumn.setCellValueFactory(cellData ->{
            Object value = cellData.getValue().get("status");
            return new SimpleStringProperty(value != null ? capitalizeFirstLetter(value.toString()) : "null");
        });
        maintenanceStatusColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");

        // Priority
        maintenancePriorityColumn.setCellValueFactory(cellData ->{
            Object value = cellData.getValue().get("priority");
            return new SimpleStringProperty(value != null ? capitalizeFirstLetter(value.toString()) : "null");
        });
        maintenancePriorityColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");

        // Request DATE
        maintenanceRequestDateColumn.setCellValueFactory(cellData -> {
            Object dateObj = cellData.getValue().get("request_date");
            if (dateObj instanceof Date) {
                Date date = (Date) dateObj;
                return new SimpleObjectProperty<>(date.toLocalDate());
            }
            return new SimpleObjectProperty<>(null);
        });
        maintenanceRequestDateColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");

        // Completion DATE
        maintenanceCompletionDateColumn.setCellValueFactory(cellData -> {
            Object dateObj = cellData.getValue().get("request_date");
            if (dateObj instanceof Date) {
                Date date = (Date) dateObj;
                return new SimpleObjectProperty<>(date.toLocalDate());
            }
            return new SimpleObjectProperty<>(null);
        });
        maintenanceCompletionDateColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");

        // Assigned STAFF
        maintenanceAssignedStaffColumn.setCellValueFactory(cellData -> {
            Object value = cellData.getValue().get("assigned_staff");
            return new SimpleStringProperty(value != null ? value.toString() : "Waiting...");
        });
        maintenanceAssignedStaffColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");

        // Issuse Type
        maintenanceIssueTypeColumn.setCellValueFactory(cellData ->{
            Object value = cellData.getValue().get("issue_type");
            return new SimpleStringProperty(value != null ? value.toString(): "NULL");
        });
        maintenanceIssueTypeColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");

    }
    // TODO: Viết hoa chữ cái đầu
    public static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
    // TODO: Lấy dữ liệu các YÊU CẦU BẢO TRÌ thông qua
    private void getMaintenanceRequestByResidentID() throws SQLException{
        int userID = new UserDAO().getIdByUserName(Session.getUserName());
        setUpColumnForMaintaineceRequest();

        int residentID = new ResidentDAO().getResidentIDByUserID(userID);
        List<Map<String, Object>> result = new MaintenanceRequestDAO().getMaintenanceRequestsByResidentId(residentID); //
        System.out.println("Result size: " + result.size());

        if (!result.isEmpty()) {
            System.out.println("First row keys: " + result.get(0).keySet());
        }else{
            System.out.println("Không có dữ liệu");
        }
        ObservableList<Map<String, Object>> observableList = FXCollections.observableArrayList(result);
        maintenanceTable.setItems(observableList);
    }
    private void getTotalMaintainenceRequest() throws SQLException {
        int userID = new UserDAO().getIdByUserName(Session.getUserName());
        int residentID = new ResidentDAO().getResidentIDByUserID(userID);
        int totalRequest = new MaintenanceRequestDAO().getTotalMaintenaceRequestByResidentID(residentID);
        maintenanceCountLabel.setText("( " +totalRequest + " total request )");

    }

    @FXML
    public void handleClearMaintenanceFilter(ActionEvent event) {
        // Logic to clear the maintenance filter
        System.out.println("Clear Maintenance Filter button clicked!");
    }

    // TODO: Lọc bảng Maintenance
    public void handleFilterMaintenance(ActionEvent event) throws SQLException {
        // truy cập
        int userID = new UserDAO().getIdByUserName(Session.getUserName());
        int residentID = new ResidentDAO().getResidentIDByUserID(userID);

        String status = (String) maintenanceStatusFilter.getValue();
        String  priority = (String) maintenancePriorityFilter.getValue();

        setUpColumnForMaintaineceRequest();
        System.out.println("Lọc với status :" + status + " và priority : " + priority);
        List<Map<String, Object>> result = new MaintenanceRequestDAO().getFilterStatusAndPriority(residentID, status, priority); //
        System.out.println("Result size: " + result.size());

        if (!result.isEmpty()) {
            System.out.println("First row keys: " + result.get(0).keySet());
        }else{
            System.out.println("Không có dữ liệu");
        }
        ObservableList<Map<String, Object>> observableList = FXCollections.observableArrayList(result);
        maintenanceTable.setItems(observableList);
    }

    // TODO: Refresh lại bảng sau khi lọc
    public void handleRefreshMyMaintenance(ActionEvent actionEvent) throws SQLException {
        loadDataMaintenanceRequest();
        System.out.println("Refresh thành công");
    }

    public void handleAddMaintenance(ActionEvent event) {
    }

    public void handleEditMaintenance(ActionEvent event) {
    }

    public void handleDeleteMaintenance() throws SQLException {
        Map<String, Object> selected = maintenanceTable.getSelectionModel().getSelectedItem();

        // TODO:: kiểm tra status chỉ khi status = pending mới được xóa
        if(selected!= null) {
            int requestId = (int) selected.get("request_id");

            // Hiển thị bảng xác nhận
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm delete ?");
            confirmAlert.setHeaderText("Do you really want delete this request");
            confirmAlert.setContentText("Request ID: " + requestId);

            Optional<ButtonType> result = confirmAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                // 1. Xóa trong DB
                deleteFromDatabase(requestId);

                // 2. Xóa khỏi bảng
                maintenanceTable.getItems().remove(selected);

                // 3. Cập nhật lại total request
                getTotalMaintainenceRequest();
                System.out.println("Xóa thành công!");
            }
            System.out.println("Xóa không thành công");
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Chưa chọn hàng nào để xóa.", ButtonType.OK);
            alert.showAndWait();
        }


    }

    private void deleteFromDatabase(int requestId) {
        String sql = "DELETE FROM MaintenanceRequest WHERE request_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Show dialog or log error
        }
    }

    public void handleEditServiceComplaint(ActionEvent event) {
    }


    public void handleViewMaintenanceDetails(ActionEvent actionEvent) {
    }

    public void handleEditMaintenanceRequest(ActionEvent actionEvent) {
    }




    public void handleViewServiceComplaintDetails(ActionEvent actionEvent) {
    }

    public void handleRefreshMyService(ActionEvent actionEvent) {
    }

    public void handleCancel(ActionEvent actionEvent) {
        // Xoá RequestStatusView
        ((Pane) RequestStatusView.getParent()).getChildren().clear();
        // Thêm lại dashboard nodes từ controller cha
        parentController.getContentArea().getChildren().setAll(parentController.getDashboardNodes());
    }
}