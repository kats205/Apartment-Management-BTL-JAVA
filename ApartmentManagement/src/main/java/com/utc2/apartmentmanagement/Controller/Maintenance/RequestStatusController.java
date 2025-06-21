package com.utc2.apartmentmanagement.Controller.Maintenance;

import com.utc2.apartmentmanagement.Controller.User.UserDashboardController;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import lombok.Setter;

public class RequestStatusController {

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
    @FXML public TableView maintenanceTable;
    @FXML public TableColumn maintenanceIdColumn;
    @FXML public TableColumn maintenanceApartmentColumn;
    @FXML public TableColumn maintenanceResidentColumn;
    @FXML public TableColumn maintenanceDescriptionColumn;
    @FXML public  TableColumn maintenanceStatusColumn;
    @FXML public TableColumn maintenancePriorityColumn;
    @FXML public TableColumn maintenanceRequestDateColumn;
    @FXML public TableColumn maintenanceStaffColumn;
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