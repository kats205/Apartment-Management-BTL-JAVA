package com.utc2.apartmentmanagement.Controller.Staff;

import com.utc2.apartmentmanagement.DAO.Maintenance.MaintenanceRequestDAO;
import com.utc2.apartmentmanagement.DAO.User.UserDAO;
import com.utc2.apartmentmanagement.Model.Session;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.utc2.apartmentmanagement.Controller.Maintenance.RequestStatusController.capitalizeFirstLetter;

public class MaintenanceHandlingController implements Initializable {

    @FXML
    public AnchorPane MaintenanceHandlingView;

    private StaffDashboardController parentStaffDashboardController;
    // Các nút chức năng
        @FXML public Button refreshButton;
        @FXML public Button startTaskButton;
        @FXML public Button completeTaskButton;
        // Lọc
        @FXML public Button allFilter;
        @FXML public Button urgentFilter;
        @FXML public Button highFilter;
        @FXML public Button mediumFilter;
        @FXML public Button lowFilter;
        private List<Map<String, Object>> allRequests = new ArrayList<>();

    // Các Label số lượng
    @FXML private Label totalTasksLabel;
    @FXML private Label inProgressLabel;
    @FXML private Label completedTodayLabel;
    @FXML private Label urgentTasksLabel;


    // Các biến của cột
    @FXML public TableView<Map<String,Object>> maintenanceTasksTable;
    @FXML public TableColumn<Map<String, Object>, String> requestIdColumn;
    @FXML public TableColumn<Map<String, Object>, String> apartmentColumn;
    @FXML public TableColumn<Map<String, Object>, String> residentColumn;
    @FXML public TableColumn<Map<String, Object>, String> descriptionColumn;
    @FXML public TableColumn<Map<String, Object>, String> priorityColumn;
    @FXML public TableColumn<Map<String, Object>, LocalDate> requestDateColumn;
    @FXML public TableColumn<Map<String, Object>, String> statusColumn;

    // Các biến TEXT
    @FXML public TextArea taskDetailsArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDataMaintenanceHandling(); // Load dữ liệu Bảng Maintenance Handling

        // Set up chuc nang cac Button
        setupButton();
        setupTableSelectionListener();
    }

    private void setupButton(){ // Thiết lập các nút
        startTaskButton.setOnAction(event -> {
            // Lấy hàng đang chọn
            Map<String, Object> selectedRequest = maintenanceTasksTable.getSelectionModel().getSelectedItem();

            if (selectedRequest == null) {
                showAlert(Alert.AlertType.WARNING, "No Request Selected", "Please select a maintenance request to start.");
                return;
            }

            String currentStatus = (String) selectedRequest.get("status");

            if (!"assigned".equalsIgnoreCase(currentStatus)) {
                showAlert(Alert.AlertType.INFORMATION, "Invalid Status", "Only requests with status 'assigned' can be started.");
                return;
            }

            // Hộp thoại xác nhận
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Start");
            confirm.setHeaderText(null);
            confirm.setContentText("Are you sure you want to start this maintenance task?");

            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isEmpty() || result.get() != ButtonType.OK) {
                return; // Người dùng ấn Cancel hoặc đóng hộp thoại
            }

            int requestId = (int) selectedRequest.get("request_id");
            boolean success = new MaintenanceRequestDAO().updateStatus(requestId, "in_progress");

            if (success) {
                selectedRequest.put("status", "in_progress");
                maintenanceTasksTable.refresh();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Maintenance request is now in progress.");
                loadDataMaintenanceHandling();
            } else {
                showAlert(Alert.AlertType.ERROR, "Update Failed", "An error occurred while updating the request status.");
            }
            loadDataMaintenanceHandling();
        });
        completeTaskButton.setOnAction(event -> {
            // Lấy hàng đang chọn
            Map<String, Object> selectedRequest = maintenanceTasksTable.getSelectionModel().getSelectedItem();

            if (selectedRequest == null) {
                showAlert(Alert.AlertType.WARNING, "No Request Selected", "Please select a maintenance request to complete.");
                return;
            }

            String currentStatus = (String) selectedRequest.get("status");

            if (!"in_progress".equalsIgnoreCase(currentStatus)) {
                showAlert(Alert.AlertType.INFORMATION, "Invalid Status", "Only requests with status 'in_progress' can be completed.");
                return;
            }

            // Xác nhận trước khi thực hiện
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Completion");
            confirm.setHeaderText(null);
            confirm.setContentText("Are you sure you want to mark this request as completed?");

            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isEmpty() || result.get() != ButtonType.OK) {
                return; // Người dùng ấn Cancel hoặc đóng hộp thoại
            }

            int requestId = (int) selectedRequest.get("request_id");
            boolean success = new MaintenanceRequestDAO().updateStatus(requestId, "completed");

            if (success) {
                selectedRequest.put("status", "completed");
                maintenanceTasksTable.refresh();
                showAlert(Alert.AlertType.INFORMATION, "Request Completed", "The maintenance request has been marked as 'completed'.");
                loadDataMaintenanceHandling();
            } else {
                showAlert(Alert.AlertType.ERROR, "Update Failed", "An error occurred while updating the request status.");
            }
            loadDataMaintenanceHandling();
        });
        refreshButton.setOnAction(event -> {
            loadDataMaintenanceHandling();
        });
        setupButtonFilter();

    }
        // Setup các Button Filter
        private void setupButtonFilter(){
            allFilter.setOnAction(this::filterByPriority);
            urgentFilter.setOnAction(this::filterByPriority);
            highFilter.setOnAction(this::filterByPriority);
            mediumFilter.setOnAction(this::filterByPriority);
            lowFilter.setOnAction(this::filterByPriority);
        }
    @FXML
    private void filterByPriority(ActionEvent event) {
        String selectedPriority = ((Button) event.getSource()).getText().toLowerCase();

        List<Map<String, Object>> filtered;

        if (selectedPriority.equals("all")) {
            filtered = allRequests;
        } else {
            filtered = allRequests.stream()
                    .filter(req -> selectedPriority.equalsIgnoreCase((String) req.get("priority")))
                    .collect(Collectors.toList());
        }

        System.out.println("Selected priority: " + selectedPriority);
        System.out.println("Total original: " + allRequests.size());
        System.out.println("Filtered: " + filtered.size());

        maintenanceTasksTable.getItems().setAll(filtered);

        updateStats(filtered); // Update thông số
    }

    // Thay đổi trạng thái khi lọc
    private void updateStats(List<Map<String, Object>> data) {
        int total = data.size();
        int inProgress = 0;
        int todayRequest = 0;
        int urgent = 0;

        LocalDate today = LocalDate.now();

        for (Map<String, Object> row : data) {
            String status = (String) row.get("status");
            String priority = (String) row.get("priority");

            if ("in_progress".equalsIgnoreCase(status)) {
                inProgress++;
            }

            if ("urgent".equalsIgnoreCase(priority)) {
                urgent++;
            }
            System.out.println("Request Date: " + row.get("request_date"));
            Object dateObj = row.get("request_date");
            if (dateObj instanceof Date) {
                LocalDate requestDate = ((Date) dateObj).toLocalDate();
                if (requestDate.equals(today)) {
                    todayRequest++;
                }
            }
        }


        totalTasksLabel.setText(String.valueOf(total));
        inProgressLabel.setText(String.valueOf(inProgress));
        urgentTasksLabel.setText(String.valueOf(urgent));
        completedTodayLabel.setText(String.valueOf(todayRequest));
    }

    private void setupTableSelectionListener() {
        maintenanceTasksTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                String issueType = (String) newSelection.get("issue_type");
                String description = (String) newSelection.get("description");
                String  priority = (String) newSelection.get("priority");
                taskDetailsArea.setText("Issue Type: " + capitalizeFirstLetter(issueType)+"\n" +
                                        "Detail Description: " + capitalizeFirstLetter(description)+"\n" +
                                        "Priority: " + capitalizeFirstLetter(priority));
            }
        });
    }
    // Load dữ liệu Bảng Maintenance Handling
    public void loadDataMaintenanceHandling(){
        try{
            getMaintenanceHandlingByStaffID();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    // Lấy dữ liệu các yêu cầu bảo trì của thằng staff cụ thể
    private void getMaintenanceHandlingByStaffID() throws SQLException {
        int staff_id = new UserDAO().getIdByUserName(Session.getUserName());
        setUpColumnForMaintenanceHandling(); // set cot cho du lieu

        List<Map<String,Object>> result = new MaintenanceRequestDAO().getMaintenanceRequestsByAssignedStaffId(staff_id);
        System.out.println("Result size: " + result.size());

        if (!result.isEmpty()) {
            System.out.println("First row keys: " + result.get(0).keySet());
        }else{
            System.out.println("Không có dữ liệu");
        }

        allRequests = result;

        ObservableList<Map<String, Object>> observableList = FXCollections.observableArrayList(result);
        maintenanceTasksTable.setItems(observableList);

        updateStats(result); // Set dữ liệu
    }
    // Set up cột hiển thị dữ liệu
    private void setUpColumnForMaintenanceHandling(){
        // Request ID
        requestIdColumn.setCellValueFactory(cellData ->{
            Object value = cellData.getValue().get("request_id");
            return new SimpleStringProperty(value != null ? value.toString() : "null");
        });
        requestIdColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");
        // Apartment
        apartmentColumn.setCellValueFactory(cellData ->{
            Object value = cellData.getValue().get("apartment_id");
            return new SimpleStringProperty(value != null ? value.toString() : "null");
        });
        apartmentColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");
        // Resident Name
        residentColumn.setCellValueFactory(cellData ->{
            Object value = cellData.getValue().get("resident_name");
            return new SimpleStringProperty(value != null ? value.toString() : "null");
        });
        residentColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");
        // Description
        descriptionColumn.setCellValueFactory(cellData ->{
            Object value = cellData.getValue().get("description");
            return new SimpleStringProperty(value != null ? value.toString() : "null");
        });
        descriptionColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");
        // Priority
        priorityColumn.setCellValueFactory(cellData ->{
            Object value = cellData.getValue().get("priority");
            return new SimpleStringProperty(value != null ? value.toString() : "null");
        });
        priorityColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");
        // Status
        statusColumn.setCellValueFactory(cellData ->{
            Object value = cellData.getValue().get("status");
            return new SimpleStringProperty(value != null ? value.toString() : "null");
        });
        statusColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");

        requestDateColumn.setCellValueFactory(cellData -> {
            Object dateObj = cellData.getValue().get("request_date");
            if (dateObj instanceof Date) {
                Date date = (Date) dateObj;
                return new SimpleObjectProperty<>(date.toLocalDate());
            }
            return new SimpleObjectProperty<>(null);
        });
        requestDateColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");
    }
    // Quay lai DashBoard
    public void handleCancel(ActionEvent actionEvent) {

    }










    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null); // hoặc title nếu bạn muốn nó làm header
        alert.setContentText(message);
        alert.showAndWait();
    }
}
