package com.utc2.apartmentmanagement.Controller.Staff;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class StaffApprovalController implements Initializable {

    @FXML public Button cancelButton;
    @FXML public Label maintenanceCountLabel;
    @FXML public ComboBox maintenanceStatusFilter;
    @FXML public ComboBox maintenancePriorityFilter;
    @FXML public Button selectAllMaintenanceBtn;
    @FXML public Button deleteMaintenanceBtn;
    @FXML public Button refreshMaintenanceBtn;
    @FXML public Button approveAllMaintenanceBtn;
    @FXML public TableView maintenanceTable;
    @FXML public TableColumn requestIdCol;
    @FXML public TableColumn issueTypeCol;
    @FXML public TableColumn maintenanceDescCol;
    @FXML public TableColumn maintenanceStatusCol;
    @FXML public TableColumn priorityCol;
    @FXML public TableColumn requestDateCol;
    @FXML public TableColumn assignedStaffCol;
    @FXML public TableColumn completionDateCol;
    @FXML public Label complaintsCountLabel;
    @FXML public ComboBox complaintsStatusFilter;
    @FXML public ComboBox complaintsPriorityFilter;
    @FXML public Button selectAllComplaintsBtn;
    @FXML public Button deleteComplaintsBtn;
    @FXML public Button refreshComplaintsBtn;
    @FXML public Button ApproveComplaintsBtn;
    @FXML public TableView complaintsTable;
    @FXML public TableColumn complaintIdCol;
    @FXML public TableColumn serviceNameCol;
    @FXML public TableColumn complaintsDescCol;
    @FXML public TableColumn complaintsStatusCol;
    @FXML public TableColumn severityCol;
    @FXML public TableColumn submitDateCol;
    @FXML public TableColumn resolutionDateCol;
    @FXML public TableColumn complaintsAssignedStaffCol;

    public void handleCancel(ActionEvent actionEvent) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
