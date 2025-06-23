package com.utc2.apartmentmanagement.Controller.Manager;

import com.utc2.apartmentmanagement.Controller.DashboardController;
import com.utc2.apartmentmanagement.DAO.Complaint.ComplaintRequestDAO;
import com.utc2.apartmentmanagement.DAO.Maintenance.MaintenanceRequestDAO;
import com.utc2.apartmentmanagement.DAO.User.StaffDAO;
import com.utc2.apartmentmanagement.DAO.User.UserDAO;
import com.utc2.apartmentmanagement.Model.Complaint.Complaint;
import com.utc2.apartmentmanagement.Model.Maintenance.MaintenanceRequest;
import com.utc2.apartmentmanagement.Model.User.Staff;
import com.utc2.apartmentmanagement.Utils.AlertBox;
import com.utc2.apartmentmanagement.Utils.PaginationUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import lombok.Setter;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class EmployeeAssignmentController implements Initializable {
    public BorderPane mainContainer;
    public TextField searchStaffMaintenance;
    public ComboBox<String> departmentFilterMaintenance;
    public TableView<Map<String, String>> staffTableMaintenance;
    public TableColumn<Map<String, String>, String> staffNameCol;
    public TableColumn<Map<String, String>, String> staffEmailCol;
    public TableColumn<Map<String, String>, String> staffPhoneCol;
    public ComboBox<String> priorityFilterMaintenance;
    public TableView<MaintenanceRequest> maintenanceRequestsTable;
    public TableColumn<MaintenanceRequest, String> requestTypeCol;
    public TableColumn<MaintenanceRequest, Integer> requestIdCol;
    public TableColumn<MaintenanceRequest, String> requestStatusCol;
    public TableColumn<MaintenanceRequest, String> requestPriorityCol;
    public TableColumn<MaintenanceRequest, Date> requestDateCol;
    public TextField requestIdMaintenance;
    public TextField apartmentIdMaintenance;
    public TextField residentNameMaintenance;
    public TextArea descriptionMaintenance;
    public TextField requestDateMaintenance;
    public ComboBox<String> priorityMaintenance;
    public ComboBox<String> statusMaintenance;
    public TextField createdDateMaintenance;
    public Label selectedStaffMaintenance;
    public TextField searchStaffComplaint;
    public ComboBox<String> departmentFilterComplaint;
    public TableView staffTableComplaint;
    public TableColumn staffNameColComplaint;
    public TableColumn staffRoleColComplaint;
    public TableColumn staffEmailColComplaint;
    public TableColumn staffPhoneColComplaint;
    public ComboBox priorityFilterComplaint;
    public TableView complaintsTable;
    public TableColumn complaintIdCol;
    public TableColumn complaintTypeCol;
    public TableColumn complaintStatusCol;
    public TableColumn complaintPriorityCol;
    public TableColumn submitDateCol;
    public TableColumn completionDateColComplaint;
    public TextField complaintId;
    public TextField apartmentIdComplaint;
    public TextField residentNameComplaint;
    public TextField typeComplaint;
    public TextArea descriptionComplaint;
    public ComboBox priorityComplaint;
    public ComboBox statusComplaint;
    public TextField createdDateComplaint;
    public Label selectedStaffComplaint;
    public AnchorPane assignTasksView;
    public TextField issueTypeMaintenance;
    public Button assignStaffBtn;
    @Setter
    private DashboardController parentController;

    private static final List<Map<String, String>> STAFF_LIST = new UserDAO().listUserAssignTasks();
    private static final List<MaintenanceRequest> MAINTENANCE_REQUESTS = new MaintenanceRequestDAO().getAllMaintenanceRequest();

    private static final List<Complaint> COMPLAINTS_REQUESTS = new ComplaintRequestDAO().getAllComplaints();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        InitTableStaff(STAFF_LIST);
        InitTableMaintenanceRequests(statusFilter(MAINTENANCE_REQUESTS));

        priorityFilterMaintenance.getItems().addAll("low", "medium", "high", "urgent");
        priorityFilterComplaint.getItems().addAll("low", "medium", "high", "urgent");
        priorityFilterMaintenance.setOnAction(event -> {
            String selectedPriority = priorityFilterMaintenance.getValue();
            List<MaintenanceRequest> filtered = filterByPriority(selectedPriority);
            InitTableMaintenanceRequests(filtered);
        });
        priorityComplaint.setOnAction(event -> {
            String selectedPriority = priorityFilterMaintenance.getValue();
            List<MaintenanceRequest> filtered = filterByPriority(selectedPriority);
            InitTableMaintenanceRequests(filtered);
        });
    }

    public List<MaintenanceRequest> filterByPriority(String priority) {
        if (priority == null || priority.isEmpty()) {
            return statusFilter(MAINTENANCE_REQUESTS);
        }
        return statusFilter(MAINTENANCE_REQUESTS).stream()
                .filter(req -> req.getPriority().equalsIgnoreCase(priority))
                .toList();
    }

    public List<MaintenanceRequest> statusFilter(List<MaintenanceRequest> list){
        return list.stream()
                .filter(m -> m.getStatus().equals("pending"))
                .filter(m -> m.getAssignedStaffID() == 0)
                .toList();
    }

    public void cancelMaintenance(ActionEvent event) {
        // Xoá apartment view
        ((Pane) assignTasksView.getParent()).getChildren().clear();
        // Thêm lại dashboard nodes từ controller cha
        parentController.getContentArea().getChildren().setAll(parentController.getDashboardNodes());
    }


    public void setUpColForTableStaff(){
        staffNameCol.setCellValueFactory(cellData -> {
            String value = cellData.getValue().get("full_name");
            return new SimpleStringProperty(value != null ? value : "");
        });

        staffEmailCol.setCellValueFactory(cellData -> {
            String value = cellData.getValue().get("email");
            return new SimpleStringProperty(value != null ? value : "");
        });

        staffPhoneCol.setCellValueFactory(cellData -> {
            String value = cellData.getValue().get("phone_number");
            return new SimpleStringProperty(value != null ? value : "");
        });
    }

    public void InitTableStaff(List<Map<String, String>> list){
        setUpColForTableStaff();
        ObservableList<Map<String, String>> data = FXCollections.observableArrayList(list);
        staffTableMaintenance.setItems(data);
    }
    public void InitTableStaff(Map<String, String> list){
        setUpColForTableStaff();
        ObservableList<Map<String, String>> data = FXCollections.observableArrayList(list);
        staffTableMaintenance.setItems(data);
    }
    public void searchStaffMaintenance(ActionEvent event) {
        String searchText = searchStaffMaintenance.getText();
        for(Map<String, String> staff : STAFF_LIST) {
            if(staff.get("full_name").contains(searchText)){
                InitTableStaff(staff);
            }
        }
    }

    public void setUpColForTableMaintenanceRequests() {
        // Request ID
        requestIdCol.setCellValueFactory(new PropertyValueFactory<>("requestID"));

        // Issue Type
        requestTypeCol.setCellValueFactory(new PropertyValueFactory<>("issueType"));

        // Status
        requestStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Priority
        requestPriorityCol.setCellValueFactory(new PropertyValueFactory<>("priority"));

        // Request Date
        requestDateCol.setCellValueFactory(new PropertyValueFactory<>("requestDate"));
     }

    public void InitTableMaintenanceRequests(List<MaintenanceRequest> list) {
        setUpColForTableMaintenanceRequests();
        ObservableList<MaintenanceRequest> data = FXCollections.observableArrayList(list);
        maintenanceRequestsTable.setItems(data);
    }
    public void refreshStaffMaintenance(ActionEvent event) {
        InitTableStaff(STAFF_LIST);
        priorityFilterMaintenance.setValue(""); // hoặc null

    }

    public void refreshMaintenanceRequests(ActionEvent event) {
        InitTableMaintenanceRequests(statusFilter(MAINTENANCE_REQUESTS));
    }



    public void clearSelectionMaintenance(ActionEvent event) {
        setInitValue();
    }

    public void setInitValue(){
        requestIdMaintenance.setText("");
        apartmentIdMaintenance.setText("");
        residentNameMaintenance.setText("");
        descriptionMaintenance.setText("");
        priorityMaintenance.setValue("");
        statusMaintenance.setValue("");
        createdDateMaintenance.setText("");
        issueTypeMaintenance.setText("");
        staffTableMaintenance.getSelectionModel().clearSelection();
        maintenanceRequestsTable.getSelectionModel().clearSelection();

    }

    public void searchStaffComplaint(ActionEvent event) {
    }

    public void refreshStaffComplaint(ActionEvent event) {
    }

    public void refreshComplaints(ActionEvent event) {
    }

    public void assignStaffComplaint(ActionEvent event) {
    }

    public void clearSelectionComplaint(ActionEvent event) {
    }

    public void cancelComplaint(ActionEvent event) {
        // Xoá apartment view
        ((Pane) assignTasksView.getParent()).getChildren().clear();
        // Thêm lại dashboard nodes từ controller cha
        parentController.getContentArea().getChildren().setAll(parentController.getDashboardNodes());
    }
    @FXML
    public void handleClickTableView(MouseEvent mouseEvent) {
        MaintenanceRequest selectedRequest = maintenanceRequestsTable.getSelectionModel().getSelectedItem();
        if(selectedRequest!=null && selectedRequest.getAssignedStaffID() == 0) {
            setValueForTextFields(selectedRequest);
        }else{
            AlertBox.showAlert("Thông báo", "Yêu cầu này đã được phân công nhân viên hoặc không tồn tại.");
        }
    }

    public void setValueForTextFields(MaintenanceRequest request) {
        requestIdMaintenance.setText(String.valueOf(request.getRequestID()));
        apartmentIdMaintenance.setText(request.getApartmentID());
        residentNameMaintenance.setText(String.valueOf(request.getResidentID()));
        descriptionMaintenance.setText(request.getDescription());
        issueTypeMaintenance.setText(String.valueOf(request.getIssueType()));
        priorityMaintenance.setValue(request.getPriority());
        statusMaintenance.setValue(request.getStatus());
        createdDateMaintenance.setText(String.valueOf(request.getRequestDate()));
    }
    @FXML
    public void handleClickTableStaff(MouseEvent mouseEvent) {
        Map<String, String> staffSelected = staffTableMaintenance.getSelectionModel().getSelectedItem();
        int requestId = Integer.parseInt(requestIdMaintenance.getText());
        System.out.println("Selected Request ID: " + requestId);
        if(staffSelected!=null){
            int staffId = getStaffId(staffSelected.get("phone_number"), staffSelected.get("full_name"), staffSelected.get("email"));
            System.out.println("Selected Staff ID: " + staffId);
            saveMaintenanceRequest(staffId, requestId);
        }
        selectedStaffMaintenance.setText(staffSelected.get("full_name"));
    }

    public void saveMaintenanceRequest(int staffId, int requestId) {
        assignStaffBtn.setOnAction(event -> {
            if(new MaintenanceRequestDAO().updateAssignedStaff(requestId, staffId)){
                System.out.println("Phân công nhân viên thành công");
                AlertBox.showAlert("Thông báo", "Phân công nhân viên thành công.");
            }else{
                AlertBox.showAlert("Thông báo", "Phân công nhân viên thất bại. Vui lòng thử lại.");
            }
        });

    }

    public int getStaffId(String phoneNumber, String fullName, String email) {
        for(Map<String, String> staff :STAFF_LIST){
            if(staff.get("phone_number").equals(phoneNumber) &&
            staff.get("full_name").equals(fullName) &&
            staff.get("email").equals(email)) {
                return Integer.parseInt(staff.get("staff_id"));
            }
        }
        return -1; // Return -1 if no matching staff found
    }
}
