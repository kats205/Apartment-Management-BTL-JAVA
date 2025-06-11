package com.utc2.apartmentmanagement.Controller.User;

import com.twilio.rest.api.v2010.account.incomingphonenumber.Local;
import com.utc2.apartmentmanagement.Controller.UserDashboardController;
import com.utc2.apartmentmanagement.DAO.MaintenanceRequestDAO;
import com.utc2.apartmentmanagement.DAO.ResidentDAO;
import com.utc2.apartmentmanagement.DAO.UserDAO;
import com.utc2.apartmentmanagement.Model.MaintenanceRequest;
import com.utc2.apartmentmanagement.Model.Session;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import lombok.Setter;


import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

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
        try {
            getMaintainenceRequestByResidentID();
            getTotalMaintainenceRequest();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

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
            return new SimpleStringProperty(value != null ? value.toString() : "null");
        });
        maintenanceStatusColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");

        // Priority
        maintenancePriorityColumn.setCellValueFactory(cellData ->{
            Object value = cellData.getValue().get("priority");
            return new SimpleStringProperty(value != null ? value.toString() : "null");
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
            return new SimpleStringProperty(value != null ? value.toString() : "NULL");
        });
        maintenanceAssignedStaffColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");
    }

    // TODO: Lấy dữ liệu các YÊU CẦU BẢO TRÌ thông qua
    private void getMaintainenceRequestByResidentID() throws SQLException{
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
        // còn hiển thi so luong đồ nữa còn viết tiếp
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

    public void handleFilterMaintenance(ActionEvent event) {
    }

    public void handleRefreshMaintenance(ActionEvent event) {
    }

    public void handleAddMaintenance(ActionEvent event) {
    }

    public void handleEditMaintenance(ActionEvent event) {
    }

    public void handleDeleteMaintenance(ActionEvent event) {
    }


    public void handleEditServiceComplaint(ActionEvent event) {
    }


    public void handleViewMaintenanceDetails(ActionEvent actionEvent) {
    }

    public void handleEditMaintenanceRequest(ActionEvent actionEvent) {
    }

    public void handleRefreshMyMaintenance(ActionEvent actionEvent) {
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