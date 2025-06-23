package com.utc2.apartmentmanagement.Controller.Staff;

import com.utc2.apartmentmanagement.DAO.Complaint.ComplaintRequestDAO;
import com.utc2.apartmentmanagement.DAO.Maintenance.MaintenanceRequestDAO;
import com.utc2.apartmentmanagement.DAO.User.StaffDAO;
import com.utc2.apartmentmanagement.DAO.User.UserDAO;
import com.utc2.apartmentmanagement.Model.Complaint.Complaint;
import com.utc2.apartmentmanagement.Model.Maintenance.MaintenanceRequest;
import com.utc2.apartmentmanagement.Model.Session;
import com.utc2.apartmentmanagement.Utils.AlertBox;
import com.utc2.apartmentmanagement.Utils.PaginationUtils;
import com.utc2.apartmentmanagement.Views.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.Setter;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

public class StaffApprovalController implements Initializable {

    @FXML public Button cancelButton;
    @FXML public Label maintenanceCountLabel;
    @FXML public ComboBox<String> maintenanceStatusFilter;
    @FXML public ComboBox<String> maintenancePriorityFilter;
    @FXML public Button selectAllMaintenanceBtn;
    @FXML public Button deleteMaintenanceBtn;
    @FXML public Button refreshMaintenanceBtn;
    @FXML public Button approveAllMaintenanceBtn;
    @FXML public TableView<MaintenanceRequest> maintenanceTable;
    @FXML public TableColumn<MaintenanceRequest, Integer> requestIdCol;
    @FXML public TableColumn<MaintenanceRequest, String> issueTypeCol;
    @FXML public TableColumn<MaintenanceRequest, String> maintenanceDescCol;
    @FXML public TableColumn<MaintenanceRequest, String> maintenanceStatusCol;
    @FXML public TableColumn<MaintenanceRequest, String> priorityCol;
    @FXML public TableColumn<MaintenanceRequest, Date> requestDateCol;
    @FXML public TableColumn<MaintenanceRequest, Integer> assignedStaffCol;
    @FXML public TableColumn<MaintenanceRequest, Date> completionDateCol;
    @FXML public Label complaintsCountLabel;
    @FXML public ComboBox<String> complaintsStatusFilter;
    @FXML public ComboBox<String> complaintsPriorityFilter;
    @FXML public Button selectAllComplaintsBtn;
    @FXML public Button deleteComplaintsBtn;
    @FXML public Button refreshComplaintsBtn;
    @FXML public Button ApproveComplaintsBtn;
    @FXML public TableView<Complaint> complaintsTable;
    @FXML public TableColumn<Complaint, Integer> complaintIdCol;
    @FXML public TableColumn<Complaint, String> typeComplaint;
    @FXML public TableColumn<Complaint, String> complaintsDescCol;
    @FXML public TableColumn<Complaint, String> complaintsStatusCol;
    @FXML public TableColumn<Complaint, String> severityCol;
    @FXML public TableColumn<Complaint, Date> submitDateCol;
    @FXML public TableColumn<Complaint, Integer> complaintsAssignedStaffCol;
    @Setter
    private StaffDashboardController parentController;
    private static final List<MaintenanceRequest> MAINTENANCE_REQUEST_LIST = new MaintenanceRequestDAO().getAllMaintenanceRequest();
    private static final List<Complaint> COMPLAINT_REQUEST_LIST = new ComplaintRequestDAO().getAllComplaints();
    public AnchorPane staffDashboard;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpTableMaintenance(MAINTENANCE_REQUEST_LIST);
        setUpTableComplaint(COMPLAINT_REQUEST_LIST);
        setUpActionForCBTabMaintenance();
        setUpActionForCBTabComplaint();
        setUpReFreshBtn();
    }


    public void setUpColForTableMaintenance(){
        requestIdCol.setCellValueFactory(new PropertyValueFactory<>("requestID"));
        requestIdCol.setStyle("-fx-alignment: CENTER;");

        issueTypeCol.setCellValueFactory(new PropertyValueFactory<>("issueType"));
        issueTypeCol.setStyle("-fx-alignment: CENTER;");

        maintenanceDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        maintenanceDescCol.setStyle("-fx-alignment: CENTER;");

        maintenanceStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        maintenanceStatusCol.setStyle("-fx-alignment: CENTER;");

        priorityCol.setCellValueFactory(new PropertyValueFactory<>("priority"));
        priorityCol.setStyle("-fx-alignment: CENTER;");

        requestDateCol.setCellValueFactory(new PropertyValueFactory<>("requestDate"));
        requestDateCol.setStyle("-fx-alignment: CENTER;");

        assignedStaffCol.setCellValueFactory(new PropertyValueFactory<>("assignedStaffID"));
        assignedStaffCol.setCellFactory(column -> new TableCell<MaintenanceRequest, Integer>() {
            @Override
            protected void updateItem(Integer staffId, boolean empty) {
                super.updateItem(staffId, empty);

                // Dòng không có dữ liệu thật
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                    return;
                }

                // Dòng có dữ liệu
                if (staffId == null || staffId == 0) {
                    setText("Waiting...");
                } else {
                    setText(""+staffId); // hoặc show tên nếu bạn có mapping staffId → tên
                }
            }
        });

        assignedStaffCol.setStyle("-fx-alignment: CENTER;");

        completionDateCol.setCellValueFactory(new PropertyValueFactory<>("completionDate"));
        completionDateCol.setCellFactory(column -> new TableCell<MaintenanceRequest, Date>() {
            private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

            @Override
            protected void updateItem(Date date, boolean empty) {
                super.updateItem(date, empty);

                // Nếu dòng trống hoặc không có dữ liệu
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null); // <- ẩn hoàn toàn text
                    return;
                }

                // Nếu ngày null hoặc là giá trị giả lập
                if (date == null || isFakeNullDate(date)) {
                    setText("Waitting..."); // hoặc "--/--/----"
                } else {
                    setText(formatter.format(date));
                }
            }

            // Kiểm tra ngày giả null như 9999-12-31
            private boolean isFakeNullDate(Date date) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                return cal.get(Calendar.YEAR) == 9999;
            }
        });


        completionDateCol.setStyle("-fx-alignment: CENTER;");
    }

    public void setUpColForTableComplaint(){
        complaintIdCol.setCellValueFactory(new PropertyValueFactory<>("requestID"));
        complaintIdCol.setStyle("-fx-alignment: CENTER;");

        typeComplaint.setCellValueFactory(new PropertyValueFactory<>("typeComplaint"));
        typeComplaint.setStyle("-fx-alignment: CENTER;");

        complaintsDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        complaintsDescCol.setStyle("-fx-alignment: CENTER;");

        complaintsStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        complaintsStatusCol.setStyle("-fx-alignment: CENTER;");

        severityCol.setCellValueFactory(new PropertyValueFactory<>("priority"));
        severityCol.setStyle("-fx-alignment: CENTER;");

        submitDateCol.setCellValueFactory(new PropertyValueFactory<>("requestDate"));
        submitDateCol.setStyle("-fx-alignment: CENTER;");

        complaintsAssignedStaffCol.setCellValueFactory(new PropertyValueFactory<>("assignedStaffID"));
        complaintsAssignedStaffCol.setCellFactory(column -> new TableCell<Complaint, Integer>() {
            @Override
            protected void updateItem(Integer staffId, boolean empty) {
                super.updateItem(staffId, empty);

                // Dòng không có dữ liệu thật
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                    return;
                }

                // Dòng có dữ liệu
                if (staffId == null || staffId == 0) {
                    setText("Waiting...");
                } else {
                    setText(""+staffId); // hoặc show tên nếu bạn có mapping staffId → tên
                }
            }
        });
        complaintsAssignedStaffCol.setStyle("-fx-alignment: CENTER;");

    }
    public void setUpTableMaintenance(List<MaintenanceRequest> list){
        setUpColForTableComplaint();
        ObservableList<MaintenanceRequest> observableList = FXCollections.observableList(list);
        maintenanceTable.setItems(observableList);
        maintenanceCountLabel.setText(list.size()+" total requests");
    }

    public void setUpTableComplaint(List<Complaint> list){
        setUpColForTableMaintenance();
        ObservableList<Complaint> observableList = FXCollections.observableList(list);
        complaintsTable.setItems(observableList);
        complaintsCountLabel.setText(list.size()+" total complaints");
    }


    public void setUpReFreshBtn(){
        refreshMaintenanceBtn.setOnAction(event -> {
            handleRefreshForMaintenance();
        });
        refreshComplaintsBtn.setOnAction(event -> {
            handleRefreshForComplaint();
        });
    }


    public void setUpActionForCBTabMaintenance(){
        // Khi chọn Status
        maintenanceStatusFilter.setOnAction(event -> {
            handleFilterChangeForMaintenance("status", maintenanceStatusFilter.getValue());
        });

        // Khi chọn Priority
        maintenancePriorityFilter.setOnAction(event -> {
            handleFilterChangeForMaintenance("priority", maintenancePriorityFilter.getValue());
        });
    }


    public void setUpActionForCBTabComplaint(){
        // Khi chọn Status
        complaintsStatusFilter.setOnAction(event -> {
            handleFilterChangeForComplaint("status", complaintsStatusFilter.getValue());
        });

        // Khi chọn Priority
        complaintsPriorityFilter.setOnAction(event -> {
            handleFilterChangeForComplaint("priority", complaintsPriorityFilter.getValue());
        });
    }

    private void handleFilterChangeForMaintenance(String filterType, String filterValue) {
//        if (filterValue == null || filterValue.isEmpty()) {
//            // Xử lý trường hợp null/empty nếu cần
//            return;
//        }

        if (filterValue.equals("All Status") || filterValue.equals("All Priority")) {
            setUpTableMaintenance(MAINTENANCE_REQUEST_LIST);
        } else {
            setUpTableMaintenance(
                    new MaintenanceRequestDAO().getFilterStatusAndPriority(filterType, filterValue)
            );
        }
    }


    private void handleFilterChangeForComplaint(String filterType, String filterValue) {
//        if (filterValue == null || filterValue.isEmpty()) {
//            // Xử lý trường hợp null/empty nếu cần
//            return;
//        }

        if (filterValue.equals("All Status") || filterValue.equals("All Priority")) {
            setUpTableComplaint(COMPLAINT_REQUEST_LIST);
        } else {
            setUpTableComplaint(
                    new ComplaintRequestDAO().getFilterStatusAndPriority(filterType, filterValue)
            );
        }
    }


    public void handleRefreshForMaintenance(){
        setUpTableMaintenance(MAINTENANCE_REQUEST_LIST);
        maintenanceStatusFilter.setValue("All Status");
        maintenancePriorityFilter.setValue("All Priority");
    }

    public void handleRefreshForComplaint(){
        setUpTableComplaint(COMPLAINT_REQUEST_LIST);
        complaintsStatusFilter.setValue("All Status");
        complaintsPriorityFilter.setValue("All Priority");
    }

    @FXML
    public void handleCancel(ActionEvent actionEvent) {
        ((Pane) staffDashboard.getParent()).getChildren().clear();
        // Thêm lại dashboard nodes từ controller cha
        parentController.getContentArea().getChildren().setAll(parentController.getDashboardNodes());
    }

    @FXML
    public void handleApproveForMaintenance(ActionEvent actionEvent) throws SQLException {
        MaintenanceRequest maintenanceRequest = maintenanceTable.getSelectionModel().getSelectedItem();
        if(maintenanceRequest == null){
            AlertBox.showAlert("Thông báo", "Vui lòng chọn yêu cầu cần chấp thuận");
        }
        else if(!maintenanceRequest.getStatus().equals("pending")){
            AlertBox.showAlert("Thông báo", "Vui lòng chọn yêu cầu cần chấp thuận");

        }
        else{
            MAINTENANCE_REQUEST_LIST.remove(maintenanceRequest);
            setUpTableMaintenance(MAINTENANCE_REQUEST_LIST);
            AlertBox.showAlert("Thông báo", "Đã chấp thuận yêu cầu");
            // khi chấp thuận yêu cầu của cư dân để gửi lên admin thì mặc định id của staff đang login là assgined_staff
            // và sau khi lên admin thì sẽ được duyệt lại và assgined_staff lần nữa
            int userId = new UserDAO().getIdByUserName(Session.getUserName());
            MaintenanceRequestDAO maintenanceRequestDAO = new MaintenanceRequestDAO();
            if(maintenanceRequestDAO.updateAssignedStaffId(maintenanceRequest.getRequestID(), userId)
             && maintenanceRequestDAO.updateStatusRequest(maintenanceRequest.getRequestID(), "assigned")){
                System.out.println("Đã cập nhật nhân viên đảm nhiệm yêu cầu thành công");
            }
            else{
                System.out.println("Cập nhật đã xảy ra lỗi!");
            }
        }
    }

    @FXML
    public void handleApproveForComplaint(ActionEvent actionEvent) throws SQLException {
        Complaint complaint = complaintsTable.getSelectionModel().getSelectedItem();
        if(complaint == null){
            AlertBox.showAlert("Thông báo", "Vui lòng chọn yêu cầu cần chấp thuận");
        }
        else if(!complaint.getStatus().equals("pending")){
            AlertBox.showAlert("Thông báo", "Vui lòng chọn yêu cầu cần chấp thuận");

        }
        else{
            COMPLAINT_REQUEST_LIST.remove(complaint);
            setUpTableComplaint(COMPLAINT_REQUEST_LIST);
            AlertBox.showAlert("Thông báo", "Đã chấp thuận yêu cầu");
            // khi chấp thuận yêu cầu của cư dân để gửi lên admin thì mặc định id của staff đang login là assgined_staff
            // và sau khi lên admin thì sẽ được duyệt lại và assgined_staff lần nữa
            int userId = new UserDAO().getIdByUserName(Session.getUserName());
            ComplaintRequestDAO complaintRequestDAO = new ComplaintRequestDAO();
            if(complaintRequestDAO.updateField("assigned_staff_id", userId, complaint.getRequestID())
                && complaintRequestDAO.updateField("status", "assigned", complaint.getRequestID())
            ){
                System.out.println("Đã cập nhật nhân viên đảm nhiệm yêu cầu thành công");
            }
            else{
                System.out.println("Cập nhật đã xảy ra lỗi!");
            }
        }
    }
}
