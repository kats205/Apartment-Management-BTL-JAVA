package com.utc2.apartmentmanagement.Controller.Staff;

import com.utc2.apartmentmanagement.Controller.User.MyProfileController;
import com.utc2.apartmentmanagement.DAO.Maintenance.MaintenanceRequestDAO;
import com.utc2.apartmentmanagement.DAO.User.StaffDAO;
import com.utc2.apartmentmanagement.Model.Maintenance.MaintenanceRequest;
import com.utc2.apartmentmanagement.Model.Session;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class StaffDashboardController implements Initializable {

    @FXML public ImageView Exit;
    @FXML public AnchorPane rootPane;
    public Button approvalButton1;
    public Button approvalButton;
    public Button approvalButton11;
    public TableView<MaintenanceRequest> pendingApprovalsTable;
    public TableColumn<MaintenanceRequest, Integer> requestIdColumn;
    public TableColumn<MaintenanceRequest, Integer> residentColumn;
    public TableColumn<MaintenanceRequest, String> apartmentColumn;
    public TableColumn<MaintenanceRequest, Date> dateSubmittedColumn;
    public TableColumn<MaintenanceRequest, String> statusColumn;
    public TableColumn<MaintenanceRequest, String> descriptionColumn;
    public TableColumn<MaintenanceRequest, String> priorityColumn;
    @Getter
    public AnchorPane contentArea;

    @Getter
    private List<Node> dashboardNodes;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Exit.setOnMouseClicked(e ->
                System.exit(0));
        try {
            roleDefination();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // khởi tạo bảng pending Approvals
        List<MaintenanceRequest> pendingApprovals = new MaintenanceRequestDAO().getAllMaintenanceRequest();
        InitTableView(pendingApprovals);
        setActionButton();
        dashboardNodes = new ArrayList<>(contentArea.getChildren());

    }

    public void setActionButton(){
        approvalButton.setOnAction(event -> {
            try{
                loadApprovalView();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        // Maintenance Handling
        approvalButton1.setOnAction(event -> {
            try {
                loadMaintenanceHandlingView();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
//        approvalButton11.setOnAction(event -> {
//            try {
//                loadProgessView();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
    }

    public void roleDefination() throws SQLException {
        String userName = Session.getUserName();
        String positionStaff = new StaffDAO().getDepartmentStaffByUserName(userName);
        System.out.println("User name: " + userName + " Position: " + positionStaff);
        if(positionStaff.equalsIgnoreCase("Ky thuat bao tri")){
            approvalButton.setVisible(false);
            approvalButton.setManaged(false);
        }
        if(positionStaff.equalsIgnoreCase("Dich vu khach hang")){
            approvalButton1.setVisible(false);
            approvalButton1.setManaged(false);
        }
    }

    public void InitColumn(){
        requestIdColumn.setCellValueFactory( new PropertyValueFactory<>("requestID"));
        requestIdColumn.setStyle("-fx-alignment: CENTER;");

        residentColumn.setCellValueFactory( new PropertyValueFactory<>("residentID"));
        residentColumn.setStyle("-fx-alignment: CENTER;");

        apartmentColumn.setCellValueFactory( new PropertyValueFactory<>("apartmentID"));
        apartmentColumn.setStyle("-fx-alignment: CENTER;");

        statusColumn.setCellValueFactory( new PropertyValueFactory<>("status"));
        statusColumn.setStyle("-fx-alignment: CENTER;");

        descriptionColumn.setCellValueFactory( new PropertyValueFactory<>("description"));
        descriptionColumn.setStyle("-fx-alignment: CENTER;");

        dateSubmittedColumn.setCellValueFactory( new PropertyValueFactory<>("requestDate"));
        dateSubmittedColumn.setStyle("-fx-alignment: CENTER;");

        priorityColumn.setCellValueFactory( new PropertyValueFactory<>("priority"));
        priorityColumn.setStyle("-fx-alignment: CENTER;");

    }
    public void InitTableView(List<MaintenanceRequest> pendingApprovals){
        if(pendingApprovals.isEmpty()) {
            pendingApprovals = new MaintenanceRequestDAO().getAllMaintenanceRequest();
        }
        ObservableList<MaintenanceRequest> requestsList = FXCollections.observableArrayList();
        InitColumn();
        requestsList.addAll(pendingApprovals);
        pendingApprovalsTable.getItems().addAll(requestsList);
    }

    public void loadApprovalView() throws IOException {
        System.out.println("Đang cố gắng tải ComplaintHandling.fxml");
        URL url = getClass().getResource("/com/utc2/apartmentmanagement/fxml/Staff/Approvalv2.fxml");
        System.out.println("URL: " + (url != null ? url.toString() : "null"));

        FXMLLoader loader = new FXMLLoader(url);
        if (url == null) {
            System.out.println("Không tìm thấy file ComplaintHandling.fxml");
            return;
        }

        Parent StaffApproval = loader.load();
        StaffApprovalController controller = loader.getController();
        controller.setParentController(this);  // Gán parent
        // In ra để debug
        System.out.println("ContentArea: " + (contentArea != null ? "không null" : "null"));

        // Thiết lập kích thước view để lấp đầy contentArea
        AnchorPane.setTopAnchor(StaffApproval, 0.0);
        AnchorPane.setRightAnchor(StaffApproval, 0.0);
        AnchorPane.setBottomAnchor(StaffApproval, 0.0);
        AnchorPane.setLeftAnchor(StaffApproval, 0.0);

        // Xóa tất cả các view hiện tại và thêm HRView
        contentArea.getChildren().clear();
        contentArea.getChildren().add(StaffApproval);
        System.out.println("Đã thêm ComplaintHandling vào contentArea");
    }

    // Load Maintenance Handling View
    public void loadMaintenanceHandlingView() throws IOException {
        System.out.println("Đang cố gắng tải MaintenanceHandling.fxml");
        URL url = getClass().getResource("/com/utc2/apartmentmanagement/fxml/Staff/MaintenanceHandling.fxml");
        System.out.println("URL: " + (url != null ? url.toString() : "null"));

        FXMLLoader loader = new FXMLLoader(url);
        if (url == null) {
            System.out.println("Không tìm thấy file MaintenanceHandling.fxml");
            return;
        }

        Parent StaffApproval = loader.load();
//        HRViewController controller = loader.getController();
//        controller.setParentController(this);  // Gán parent
        // In ra để debug
        System.out.println("ContentArea: " + (contentArea != null ? "không null" : "null"));

        // Thiết lập kích thước view để lấp đầy contentArea
        AnchorPane.setTopAnchor(StaffApproval, 0.0);
        AnchorPane.setRightAnchor(StaffApproval, 0.0);
        AnchorPane.setBottomAnchor(StaffApproval, 0.0);
        AnchorPane.setLeftAnchor(StaffApproval, 0.0);

        // Xóa tất cả các view hiện tại và thêm HRView
        contentArea.getChildren().clear();
        contentArea.getChildren().add(StaffApproval);
        System.out.println("Đã thêm Maintenance Handling vào contentArea");

    }

    // Load Progress View
    public void loadProgessView() throws IOException {
        System.out.println("Đang cố gắng tải Progress.fxml");
        URL url = getClass().getResource("/com/utc2/apartmentmanagement/fxml/Staff/Progress.fxml");
        System.out.println("URL: " + (url != null ? url.toString() : "null"));

        FXMLLoader loader = new FXMLLoader(url);
        if (url == null) {
            System.out.println("Không tìm thấy file Progress.fxml");
            return;
        }

        Parent StaffApproval = loader.load();
//        HRViewController controller = loader.getController();
//        controller.setParentController(this);  // Gán parent
        // In ra để debug
        System.out.println("ContentArea: " + (contentArea != null ? "không null" : "null"));

        // Thiết lập kích thước view để lấp đầy contentArea
        AnchorPane.setTopAnchor(StaffApproval, 0.0);
        AnchorPane.setRightAnchor(StaffApproval, 0.0);
        AnchorPane.setBottomAnchor(StaffApproval, 0.0);
        AnchorPane.setLeftAnchor(StaffApproval, 0.0);

        // Xóa tất cả các view hiện tại và thêm HRView
        contentArea.getChildren().clear();
        contentArea.getChildren().add(StaffApproval);
        System.out.println("Đã thêm Progress vào contentArea");
    }

    double x =0, y = 0;
    public void loadMyProfile(ActionEvent actionEvent) throws SQLException {
        try {
            URL url = getClass().getResource("/com/utc2/apartmentmanagement/fxml/User/MyProfileView.fxml");
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();

            // Gán controller
            MyProfileController controller = loader.getController();
            controller.setParentStaffDashBoard(this);
            controller.setDashboardStage((Stage) rootPane.getScene().getWindow()); // Stage của dashboard!

            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);

            // Kéo cửa sổ
            root.setOnMousePressed(event -> {
                x = event.getSceneX();
                y = event.getSceneY();
            });

            root.setOnMouseDragged(event -> {
                stage.setX(event.getScreenX() - x);
                stage.setY(event.getScreenY() - y);
            });
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), root);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
