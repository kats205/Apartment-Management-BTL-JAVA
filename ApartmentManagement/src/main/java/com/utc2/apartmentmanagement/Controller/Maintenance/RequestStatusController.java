package com.utc2.apartmentmanagement.Controller.Maintenance;

import com.utc2.apartmentmanagement.Controller.User.UserDashboardController;
import com.utc2.apartmentmanagement.DAO.Complaint.ComplaintRequestDAO;
import com.utc2.apartmentmanagement.DAO.DatabaseConnection;
import com.utc2.apartmentmanagement.DAO.Maintenance.MaintenanceRequestDAO;
import com.utc2.apartmentmanagement.DAO.User.ResidentDAO;
import com.utc2.apartmentmanagement.DAO.User.UserDAO;
import com.utc2.apartmentmanagement.Model.Session;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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

    @FXML public Label maintenanceCountLabel;
    @FXML public ComboBox maintenanceStatusFilter;
    @FXML public ComboBox maintenancePriorityFilter;
    @FXML public TextField   myMaintenanceIssueFilter;

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

    // Các thao tác trên Complaint
    @FXML public Button closeMaintenanceButton;
    @FXML public Label myServiceComplaintsCountLabel;
    @FXML public ComboBox serviceStatusFilter;
    @FXML public ComboBox serviceTypeFilter;
    @FXML public TextField serviceApartmentFilter;
    @FXML public Button filterServiceButton;
    @FXML public Button clearServiceFilterButton;
    @FXML public Button refreshServiceButton;
    @FXML public Button addServiceComplaintButton;
    @FXML public Button editServiceComplaintButton;
    @FXML public Button deleteServiceComplaintButton;
    @FXML public TableView<Map<String, Object>> serviceComplaintsTable;

    // Bảng My Complaint
    @FXML public TableColumn<Map<String, Object>, String> serviceComplaintIdColumn;
    @FXML public TableColumn<Map<String, Object>, String> typeComplaintColumn;
    @FXML public TableColumn<Map<String, Object>, String> serviceComplaintDescColumn;
    @FXML public TableColumn<Map<String, Object>, String> serviceStatusColumn;
    @FXML public TableColumn<Map<String, Object>, String> serviceSeverityColumn;
    @FXML public TableColumn<Map<String, Object>, LocalDate> serviceSubmitDateColumn;
    @FXML public TableColumn<Map<String, Object>, String>  serviceAssignedStaffColumn;
//    @FXML public TableColumn serviceResolutionDateColumn;

    @FXML public AnchorPane RequestStatusView;

    @Setter
    private UserDashboardController parentController;


    private FilteredList<Map<String, Object>> issueTypeFilteredList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        loadDataMaintenanceRequest();
        loadDataComplaint();

        loadStatusMaintenanceTypes();
        loadPrioritiesMaintenance();

        loadStatusComplaintTypes();
        loadServicesType();

        setUpConTextMenuMaintenanceMenu(); // Load Maintenance menu hỗ trợ
        setUpConTextMenuComplaintMenu(); // Load Complaint menu hỗ trợ

        setupFilterListeners();
        setupAutoServiceComplaintFilterListeners();

    }
    // TODO: Load Maintenance menu hỗ trợ
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
    // TODO: Load Complaint menu hỗ trợ
    private void setUpConTextMenuComplaintMenu(){
        ContextMenu contextMenu = new ContextMenu();

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(event -> {
            try {
                handleEditServiceComplaint();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        contextMenu.getItems().addAll(deleteItem);

        serviceComplaintsTable.setRowFactory(tv -> {
            TableRow<Map<String,Object>> row = new TableRow<>();
            row.setOnContextMenuRequested(event -> {
                if(!row.isEmpty()){
                    serviceComplaintsTable.getSelectionModel().select(row.getIndex());
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
        try {
            getComplaintByResidentID();
            getTotalComplaint();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
                "Pending", "Assigned", "In progress", "Completed", "Cancelled", "All Status"
        );
        serviceStatusFilter.setValue("All Status");
    }
    // TODO: Load Services Types Complaint
    private void loadServicesType() {
        serviceTypeFilter.getItems().addAll(
                "Maintenance","Service","All Type"
        );
        serviceTypeFilter.setValue("All Type");
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
            Object value = cellData.getValue().get("assigned_staff_id");
            // Kiểm tra null hoặc 0
            if (value == null || (value instanceof Integer && (Integer) value == 0)) {
                return new SimpleStringProperty("Waiting...");
            }
            return new SimpleStringProperty(value.toString());
        });
        maintenanceAssignedStaffColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");

        // Issuse Type
        maintenanceIssueTypeColumn.setCellValueFactory(cellData ->{
            Object value = cellData.getValue().get("issue_type");
            return new SimpleStringProperty(value != null ? value.toString(): "NULL");
        });
        maintenanceIssueTypeColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");

    }
    // TODO: Gán dữ liệu MAINTAINECE REQUEST vào các ô dữ liệu
    private void setUpColumnForComplaint(){
        // Complaint ID
        serviceComplaintIdColumn.setCellValueFactory(cellData ->{
            Object value = cellData.getValue().get("complaint_id");
            return new SimpleStringProperty(value != null ? value.toString() : "null");
        });
        serviceComplaintIdColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");

        // Description
        serviceComplaintDescColumn.setCellValueFactory(cellData ->{
            Object value = cellData.getValue().get("description");
            return new SimpleStringProperty(value != null ? value.toString() : "null");
        });
        serviceComplaintDescColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");
        // Type Complaint
        typeComplaintColumn.setCellValueFactory(cellData ->{
            Object value = cellData.getValue().get("type_complaint");
            return new SimpleStringProperty(value != null ? value.toString() : "null");
        });
        typeComplaintColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");

        // Status
        serviceStatusColumn.setCellValueFactory(cellData ->{
            Object value = cellData.getValue().get("status");
            return new SimpleStringProperty(value != null ? capitalizeFirstLetter(value.toString()) : "null");
        });
        serviceStatusColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");

        // Priority
        serviceSeverityColumn.setCellValueFactory(cellData ->{
            Object value = cellData.getValue().get("priority");
            return new SimpleStringProperty(value != null ? capitalizeFirstLetter(value.toString()) : "null");
        });
        serviceSeverityColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");

        // Submit date
        serviceSubmitDateColumn.setCellValueFactory(cellData -> {
            Object dateObj = cellData.getValue().get("created_at");
            if (dateObj instanceof Date) {
                Date date = (Date) dateObj;
                return new SimpleObjectProperty<>(date.toLocalDate());
            }
            return new SimpleObjectProperty<>(null);
        });
        serviceSubmitDateColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");

        // Staff
        serviceAssignedStaffColumn.setCellValueFactory(cellData ->{
            Object value = cellData.getValue().get("assigned_staff_id");
            // Kiểm tra null hoặc 0
            if (value == null || (value instanceof Integer && (Integer) value == 0)) {
                return new SimpleStringProperty("Waiting...");
            }
            return new SimpleStringProperty(value.toString());
        });
        serviceAssignedStaffColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");

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

        maintenanceCountLabel.setText("( " + observableList.size() + " total request )");

    }
    // TODO: Tổng cộng các yêu câu BẢO TRÌ
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
    public void handleFilterMaintenance() throws SQLException {
        // truy cập
        int userID = new UserDAO().getIdByUserName(Session.getUserName());
        int residentID = new ResidentDAO().getResidentIDByUserID(userID);

        String status = (String) maintenanceStatusFilter.getValue();
        String  priority = (String) maintenancePriorityFilter.getValue();
        String keyword = myMaintenanceIssueFilter.getText();

        setUpColumnForMaintaineceRequest();
        System.out.println("Lọc với status :" + status + " và priority : " + priority);

        if (status.equals("In Progress")){
            status = "in_progress";
        }
        List<Map<String, Object>> result = new MaintenanceRequestDAO().getFilterStatusAndPriority(residentID, status, priority); //
        ObservableList<Map<String, Object>> observableList = FXCollections.observableArrayList(result);

        // Debug
        System.out.println("Result size: " + result.size());
        if (!result.isEmpty()) {
            System.out.println("First row keys: " + result.get(0).keySet());
        }else{
            System.out.println("Không có dữ liệu");
        }
        issueTypeFilteredList = new FilteredList<>(observableList, p -> true);
        issueTypeFilteredList.setPredicate(item -> {
            if (keyword == null || keyword.isBlank()) return true;
            Object issueType = item.get("issue_type");
            return issueType != null && issueType.toString().toLowerCase().contains(keyword.toLowerCase());
        });

        maintenanceTable.setItems(issueTypeFilteredList);
        maintenanceCountLabel.setText("( " + issueTypeFilteredList.size() + " total request )");

    }
    private void setupFilterListeners() {
        myMaintenanceIssueFilter.textProperty().addListener((obs, oldVal, newVal) -> {
            try {
                handleFilterMaintenance();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        maintenanceStatusFilter.valueProperty().addListener((obs, oldVal, newVal) -> {
            try {
                handleFilterMaintenance();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        maintenancePriorityFilter.valueProperty().addListener((obs, oldVal, newVal) -> {
            try {
                handleFilterMaintenance();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }


    // TODO: Refresh lại bảng sau khi lọc
    public void handleRefreshMyMaintenance(ActionEvent actionEvent) throws SQLException {
        loadDataMaintenanceRequest();
        maintenanceStatusFilter.setValue("All Status");
        maintenancePriorityFilter.setValue("All Priority");
        System.out.println("Refresh thành công");
    }

    // Nút Xóa
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

// Service
    // TODO: Lấy dữ liệu các Complaint thông qua ID
    private void getComplaintByResidentID() throws SQLException{
        int userID = new UserDAO().getIdByUserName(Session.getUserName());
        setUpColumnForComplaint();

        int residentID = new ResidentDAO().getResidentIDByUserID(userID);
        List<Map<String, Object>> result = new ComplaintRequestDAO().getComplaintByResidentId(residentID); //
        System.out.println("Result size: " + result.size());

        if (!result.isEmpty()) {
            System.out.println("First row keys: " + result.get(0).keySet());
        }else{
            System.out.println("Không có dữ liệu");
        }
        ObservableList<Map<String, Object>> observableList = FXCollections.observableArrayList(result);
        serviceComplaintsTable.setItems(observableList);
    }
    private void getTotalComplaint() throws SQLException {
        int userID = new UserDAO().getIdByUserName(Session.getUserName());
        int residentID = new ResidentDAO().getResidentIDByUserID(userID);
        int totalRequest = new ComplaintRequestDAO().getTotalComplaintByResidentID(residentID);
        myServiceComplaintsCountLabel.setText("( " +totalRequest + " total complaint )");

    }

    // Button Select All
    public void handleViewServiceComplaintDetails() throws SQLException {
        // truy cập
        int userID = new UserDAO().getIdByUserName(Session.getUserName());
        int residentID = new ResidentDAO().getResidentIDByUserID(userID);

        String status = (String) serviceStatusFilter.getValue();
        String  type = (String) serviceTypeFilter.getValue();
        if (status.equals("In Progress")){
            status = "in_progress";
        }
        setUpColumnForComplaint();

        System.out.println("Lọc với status :" + status + " và service type : " + type);
        List<Map<String, Object>> result = new ComplaintRequestDAO().getFilterStatusAndType(residentID, status,type); //
        ObservableList<Map<String, Object>> observableList = FXCollections.observableArrayList(result);

        // Debug
        System.out.println("Result size: " + result.size());
        if (!result.isEmpty()) {
            System.out.println("First row keys: " + result.get(0).keySet());
        }else{
            System.out.println("Không có dữ liệu");
        }
        serviceComplaintsTable.setItems(observableList);

        int totalFiltered = new ComplaintRequestDAO().getFilteredComplaintCount(residentID, status, type);
        myServiceComplaintsCountLabel.setText("( " + totalFiltered + " total complaint )");
    }
    private void setupAutoServiceComplaintFilterListeners() {
        serviceStatusFilter.valueProperty().addListener((obs, oldVal, newVal) -> {
            try {
                handleViewServiceComplaintDetails();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        serviceTypeFilter.valueProperty().addListener((obs, oldVal, newVal) -> {
            try {
                handleViewServiceComplaintDetails();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
    // Nut Delete
    public void handleEditServiceComplaint() throws SQLException {
        Map<String, Object> selected = serviceComplaintsTable.getSelectionModel().getSelectedItem();

        // TODO:: kiểm tra status chỉ khi status = pending mới được xóa
        if(selected!= null) {
            int requestId = (int) selected.get("complaint_id");

            // Hiển thị bảng xác nhận
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm delete ?");
            confirmAlert.setHeaderText("Do you really want delete this complaint");
            confirmAlert.setContentText("Complaint ID: " + requestId);

            Optional<ButtonType> result = confirmAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                // 1. Xóa trong DB
                deleteFromComplaintDatabase(requestId);

                // 2. Xóa khỏi bảng
                serviceComplaintsTable.getItems().remove(selected);

                // 3. Cập nhật lại total request
                getTotalComplaint();
                System.out.println("Xóa thành công!");
            }
            System.out.println("Xóa không thành công");
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Chưa chọn hàng nào để xóa.", ButtonType.OK);
            alert.showAndWait();
        }
    }
    private void deleteFromComplaintDatabase(int complaintId){
        String sql = "DELETE FROM Complaint WHERE complaint_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, complaintId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Show dialog or log error
        }
    }


    // Nut Refresh
    public void handleRefreshMyService(ActionEvent actionEvent) {
        loadDataComplaint();
        serviceStatusFilter.setValue("All Status");
        serviceTypeFilter.setValue("All Type");
        System.out.println("Refresh thành công");
    }
    // Button Cancel
    public void handleCancel(ActionEvent actionEvent) {
        // Xoá RequestStatusView
        ((Pane) RequestStatusView.getParent()).getChildren().clear();
        // Thêm lại dashboard nodes từ controller cha
        parentController.getContentArea().getChildren().setAll(parentController.getDashboardNodes());
    }

}