package com.utc2.apartmentmanagement.Controller;

import com.utc2.apartmentmanagement.Controller.Apartment.ApartmentViewController;
import com.utc2.apartmentmanagement.Controller.Payment.PaymentViewController;
import com.utc2.apartmentmanagement.Controller.Report.ReportViewController;
import com.utc2.apartmentmanagement.Controller.Staff.HRViewController;
import com.utc2.apartmentmanagement.Controller.Staff.ResidentsInfoController;
import com.utc2.apartmentmanagement.Controller.User.MyProfileController;
import com.utc2.apartmentmanagement.DAO.Apartment.ApartmentDAO;
import com.utc2.apartmentmanagement.DAO.Maintenance.MaintenanceRequestDAO;
import com.utc2.apartmentmanagement.DAO.Billing.PaymentDAO;
import com.utc2.apartmentmanagement.DAO.User.UserDAO;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import lombok.Getter;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;

public class DashboardController implements Initializable {

    @FXML public Button paymentButton;
    @FXML public Button report;
    @FXML public Button apartment;
    @FXML public Button payment;
    @FXML public Button reportButton;
    @FXML public Button hrButton;
    @FXML public Button ResidentBtn;
    @FXML private Button ApartmentButton;

    @FXML public Label Occupied;
    @FXML public Label Available;
    @FXML public Label Collected;
    @FXML public Label outStanding;
    @FXML public Label Pending;
    @FXML public Label Completed;
    @FXML public Label totalApartmentsLabel;
    @FXML public Label requestsLabel;
    @FXML public Label revenueLabel;
    @FXML private Label Menu;
    @FXML private Label MenuBack;

    @FXML public TableView<Map<String, String>> recentActivitiesTable;
    @FXML public TableColumn<Map<String, String>, String> roleColumn;
    @FXML public TableColumn<Map<String, String>, String> dateColumn;
    @FXML public TableColumn<Map<String, String>, String> timeColumn;
    @FXML public TableColumn<Map<String, String>, String> activityColumn;
    @FXML public TableColumn<Map<String, String>, String> userColumn;
    @FXML public TableColumn<Map<String, String>, String> statusColumn;
    @FXML public TableColumn<Map<String, String>, String> actionColumn;



    @Getter
    @FXML private AnchorPane contentArea;
    @FXML private AnchorPane rootPane;
    @FXML private AnchorPane slider;

    @FXML private ImageView Exit;

    @Getter
    private List<Node> dashboardNodes;

    private boolean isSidebarVisible = true;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Exit.setOnMouseClicked(event -> {
            System.exit(0);
        });
        setupMenuToggle();

        dashboardNodes = new ArrayList<>(contentArea.getChildren());

        setUpButtonApartment();

        setUpButtonReport();

        setUpButtonPayment();

        List<Map<String,String>> list = new UserDAO().listUserLastLogin();
        InitTableRecentActivities(list);
        // Các thiết lập khác...
        ApartmentDAO apartment = new ApartmentDAO();
        try {
            Occupied.setText(String.valueOf(apartment.countStatusApartment("occupied")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            Available.setText(String.valueOf(apartment.countStatusApartment("available")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            totalApartmentsLabel.setText(String.valueOf(apartment.countApartment()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        PaymentDAO payment = new PaymentDAO();
        try {
            double amount = payment.totalPaymentFromStatus("completed");
            DecimalFormat df = new DecimalFormat("#,##0.00");
            Collected.setText(df.format(amount));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            double amount = payment.totalPaymentFromStatus("pending");
            DecimalFormat df = new DecimalFormat("#,##0.00");
            outStanding.setText(df.format(amount));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        MaintenanceRequestDAO maintenanceRequestDAO = new MaintenanceRequestDAO();
        try {
            Completed.setText(String.valueOf(maintenanceRequestDAO.countRequestByStatus("completed")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            Pending.setText(String.valueOf(maintenanceRequestDAO.countRequestByStatus("pending")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        requestsLabel.setText(String.valueOf(maintenanceRequestDAO.getAllMaintenanceRequest().size()));



        try {
            double amountCompleted = payment.totalPaymentFromStatus("completed");
            double amountPending = payment.totalPaymentFromStatus("pending");
            DecimalFormat df = new DecimalFormat("#,##0");
            double total = amountCompleted + amountPending;
            revenueLabel.setText(df.format(total) + "VNĐ");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // setup column for table recent activities
    public void setUpColRecentActivities(){
        roleColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("Role")));
        roleColumn.setStyle("-fx-alignment: CENTER;");

        dateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("Date")));
        dateColumn.setStyle("-fx-alignment: CENTER; ");

        timeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("Time")));
        timeColumn.setStyle("-fx-alignment: CENTER; ");

        activityColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("Activity")));
        activityColumn.setStyle("-fx-alignment: CENTER; ");

        userColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("username")));
        userColumn.setStyle("-fx-alignment: CENTER; ");

        statusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("Status")));
        statusColumn.setStyle("-fx-alignment: CENTER; ");

        actionColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("Action")));
        actionColumn.setStyle("-fx-alignment: CENTER; ");

    }
    public void InitTableRecentActivities(List<Map<String,String>> listRecentActivities){
        setUpColRecentActivities();
        ObservableList<Map<String, String>> list = FXCollections.observableList(listRecentActivities);
        recentActivitiesTable.setItems(list);
    }


    private void setUpButtonApartment(){
        // Thiết lập sự kiện cho nút ApartmentButton
        ApartmentButton.setOnAction(event -> {
            try {
                loadApartmentView();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        apartment.setOnAction(event -> {
            try {
                loadApartmentView();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    private void setUpButtonPayment(){
        // Thiết lập sự kiện cho nút paymentButton
        paymentButton.setOnAction(event -> {
            try {
                loadPaymentView();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        payment.setOnAction(event -> {
            try {
                loadPaymentView();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void setUpButtonReport(){
        // Thiết lập sự kiện cho nút reportButton
        reportButton.setOnAction(event -> {
            try {
                loadReportView();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        report.setOnAction(event -> {
            try {
                loadReportView();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void setupMenuToggle() {
        // Initial setup for menu labels
        MenuBack.setVisible(false);

        // Add click event to both Menu and MenuBack labels
        Menu.setOnMouseClicked(event -> toggleSidebar());
        MenuBack.setOnMouseClicked(event -> toggleSidebar());
    }

    private void toggleSidebar() {
        // Tạo transition cho sidebar
        TranslateTransition sidebarTransition = new TranslateTransition(Duration.millis(300), slider);

        if (isSidebarVisible) {
            // Ẩn sidebar
            sidebarTransition.setToX(-150);

            // Điều chỉnh contentArea
            TranslateTransition contentTransition = new TranslateTransition(Duration.millis(300), contentArea);
            contentTransition.setToX(-75); // Dịch chuyển contentArea sang trái 150px
            contentTransition.play();

            // Đổi nút menu
            Menu.setVisible(false);
            MenuBack.setVisible(true);
        } else {
            // Hiện sidebar
            sidebarTransition.setToX(0);

            // Điều chỉnh contentArea
            TranslateTransition contentTransition = new TranslateTransition(Duration.millis(300), contentArea);
            contentTransition.setToX(0); // Đưa contentArea về vị trí ban đầu
            contentTransition.play();

            // Đổi nút menu
            Menu.setVisible(true);
            MenuBack.setVisible(false);
        }

        // Chạy animation cho sidebar
        sidebarTransition.play();

        // Đảo trạng thái
        isSidebarVisible = !isSidebarVisible;
    }

    // Phương thức để tải ApartmentView vào contentArea
    private void loadApartmentView() throws IOException {
        // Thêm debug để xác định vấn đề
        System.out.println("Đang cố gắng tải ApartmentView.fxml");
        URL url = getClass().getResource("/com/utc2/apartmentmanagement/fxml/Apartment/ApartmentView.fxml");
        System.out.println("URL: " + (url != null ? url.toString() : "null"));
        FXMLLoader loader = new FXMLLoader(url);

        if (url == null) {
            System.out.println("Không tìm thấy file ApartmentView.fxml");
            return;
        }

        Parent apartmentView = loader.load();
        ApartmentViewController controller = loader.getController();
        controller.setParentController(this);  // Gán parent

        // Hiển thị ApartmentView thay cho dashboard
        contentArea.getChildren().clear();
        contentArea.getChildren().add(apartmentView); // Hiện phần danh sách căn hộ

        // In ra để debug
        System.out.println("ContentArea: " + (contentArea != null ? "không null" : "null"));

        // Thiết lập kích thước view để lấp đầy contentArea
        AnchorPane.setTopAnchor(apartmentView, 0.0);
        AnchorPane.setRightAnchor(apartmentView, 0.0);
        AnchorPane.setBottomAnchor(apartmentView, 0.0);
        AnchorPane.setLeftAnchor(apartmentView, 0.0);

        // Xóa tất cả các view hiện tại và thêm ApartmentView
        contentArea.getChildren().clear();
        contentArea.getChildren().add(apartmentView);
        System.out.println("Đã thêm ApartmentView vào contentArea");
    }

    private void loadPaymentView() throws IOException {
        // Thêm debug để xác định vấn đề
        System.out.println("Đang cố gắng tải ApartmentView.fxml");
        URL url = getClass().getResource("/com/utc2/apartmentmanagement/fxml/Service/PaymentView.fxml");
        System.out.println("URL: " + (url != null ? url.toString() : "null"));

        FXMLLoader loader = new FXMLLoader(url);
        if (url == null) {
            System.out.println("Không tìm thấy file PaymentView.fxml");
            return;
        }

        Parent PaymentView = loader.load();
        PaymentViewController controller = loader.getController();
        controller.setParentController(this);  // Gán parent
        // In ra để debug
        System.out.println("ContentArea: " + (contentArea != null ? "không null" : "null"));

        // Thiết lập kích thước view để lấp đầy contentArea
        AnchorPane.setTopAnchor(PaymentView, 0.0);
        AnchorPane.setRightAnchor(PaymentView, 0.0);
        AnchorPane.setBottomAnchor(PaymentView, 0.0);
        AnchorPane.setLeftAnchor(PaymentView, 0.0);

        // Xóa tất cả các view hiện tại và thêm ApartmentView
        contentArea.getChildren().clear();
        contentArea.getChildren().add(PaymentView);
        System.out.println("Đã thêm PaymentView vào contentArea");
    }

    public void loadReportView() throws IOException {
        System.out.println("Đang cố gắng tải ReportView.fxml");
        URL url = getClass().getResource("/com/utc2/apartmentmanagement/fxml/Report/ReportView.fxml");
        System.out.println("URL: " + (url != null ? url.toString() : "null"));

        FXMLLoader loader = new FXMLLoader(url);
        if (url == null) {
            System.out.println("Không tìm thấy file ReportView.fxml");
            return;
        }

        Parent ReportView = loader.load();
        ReportViewController controller = loader.getController();
        controller.setParentController(this);  // Gán parent
        // In ra để debug
        System.out.println("ContentArea: " + (contentArea != null ? "không null" : "null"));

        // Thiết lập kích thước view để lấp đầy contentArea
        AnchorPane.setTopAnchor(ReportView, 0.0);
        AnchorPane.setRightAnchor(ReportView, 0.0);
        AnchorPane.setBottomAnchor(ReportView, 0.0);
        AnchorPane.setLeftAnchor(ReportView, 0.0);

        // Xóa tất cả các view hiện tại và thêm ApartmentView
        contentArea.getChildren().clear();
        contentArea.getChildren().add(ReportView);
        System.out.println("Đã thêm ReportView vào contentArea");
    }

        double x,y = 0;
    @FXML
    public void loadMyProfile() {
        try {
            URL url = getClass().getResource("/com/utc2/apartmentmanagement/fxml/User/MyProfileView.fxml");
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();

            // Gán controller
            MyProfileController controller = loader.getController();
            controller.setParentDashBoardController(this);
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


    public void loadHRView(ActionEvent actionEvent) throws IOException {
        System.out.println("Đang cố gắng tải HRView.fxml");
        URL url = getClass().getResource("/com/utc2/apartmentmanagement/fxml/Manager/HRView.fxml");
        System.out.println("URL: " + (url != null ? url.toString() : "null"));

        FXMLLoader loader = new FXMLLoader(url);
        if (url == null) {
            System.out.println("Không tìm thấy file HRView.fxml");
            return;
        }

        Parent HRView = loader.load();
        HRViewController controller = loader.getController();
        controller.setParentController(this);  // Gán parent
        // In ra để debug
        System.out.println("ContentArea: " + (contentArea != null ? "không null" : "null"));

        // Thiết lập kích thước view để lấp đầy contentArea
        AnchorPane.setTopAnchor(HRView, 0.0);
        AnchorPane.setRightAnchor(HRView, 0.0);
        AnchorPane.setBottomAnchor(HRView, 0.0);
        AnchorPane.setLeftAnchor(HRView, 0.0);

        // Xóa tất cả các view hiện tại và thêm HRView
        contentArea.getChildren().clear();
        contentArea.getChildren().add(HRView);
        System.out.println("Đã thêm HRView vào contentArea");
    }

    @FXML
    public void loadResidentView(ActionEvent actionEvent) throws IOException {
        System.out.println("Đang cố gắng tải ResidentsInfo.fxml");
        URL url = getClass().getResource("/com/utc2/apartmentmanagement/fxml/Manager/ResidentsInfo.fxml");
        System.out.println("URL: " + (url != null ? url.toString() : "null"));

        FXMLLoader loader = new FXMLLoader(url);
        if (url == null) {
            System.out.println("Không tìm thấy file ResidentsInfo.fxml");
            return;
        }

        Parent ResidentsInfo = loader.load();
        ResidentsInfoController controller = loader.getController();
        controller.setParentController(this);  // Gán parent
        // In ra để debug
        System.out.println("ContentArea: " + (contentArea != null ? "không null" : "null"));

        // Thiết lập kích thước view để lấp đầy contentArea
        AnchorPane.setTopAnchor(ResidentsInfo, 0.0);
        AnchorPane.setRightAnchor(ResidentsInfo, 0.0);
        AnchorPane.setBottomAnchor(ResidentsInfo, 0.0);
        AnchorPane.setLeftAnchor(ResidentsInfo, 0.0);

        // Xóa tất cả các view hiện tại và thêm HRView
        contentArea.getChildren().clear();
        contentArea.getChildren().add(ResidentsInfo);
        System.out.println("Đã thêm ResidentsInfo vào contentArea");
    }
}