package com.utc2.apartmentmanagement.Controller;

import com.utc2.apartmentmanagement.DAO.ApartmentDAO;
import com.utc2.apartmentmanagement.DAO.MaintenanceRequestDAO;
import com.utc2.apartmentmanagement.DAO.PaymentDAO;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
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
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import lombok.Getter;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;

public class DashboardController implements Initializable {
    @FXML
    public Button paymentButton;
    @FXML
    public Button report;
    @FXML
    public Button apartment;
    @FXML
    public Button payment;
    @FXML public Button reportButton;

    @FXML public Button hrButton;
    @FXML public Label Occupied;

    @FXML public Label Available;

    @FXML public Label Collected;

    @FXML public Label outStanding;

    @FXML public Label Pending;

    @FXML public Label Completed;

    @FXML public Label totalApartmentsLabel;

    @FXML public Label requestsLabel;

    @FXML public Label revenueLabel;

    @FXML public TableView<Map<String, Object>> recentActivitiesTable;

    @FXML public TableColumn<Map<String, Object>, String> roleColumn;

    @FXML public TableColumn<Map<String, Object>, LocalDate> dateColumn;

    @FXML public TableColumn<Map<String, Object>, LocalDate> timeColumn;

    @FXML public TableColumn<Map<String, Object>, LocalDate> activityColumn;

    @FXML public TableColumn<Map<String, Object>, String> userColumn;

    @FXML public TableColumn<Map<String, Object>, Boolean> statusColumn;
    @FXML public TableColumn actionColumn;

    @FXML public AnchorPane rootPane;
    @FXML
    private Button ApartmentButton;



    @FXML
    private Label Menu;

    @FXML
    private Label MenuBack;

    @FXML
    private AnchorPane slider;


    @FXML
    @Getter
    private AnchorPane contentArea;
    @Getter
    private List<Node> dashboardNodes;

    private boolean isSidebarVisible = true;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private ImageView Exit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Exit.setOnMouseClicked(event -> {
            System.exit(0);
        });
        setupMenuToggle();

        dashboardNodes = new ArrayList<>(contentArea.getChildren());




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
        URL url = getClass().getResource("/com/utc2/apartmentmanagement/fxml/ApartmentView.fxml");
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
        URL url = getClass().getResource("/com/utc2/apartmentmanagement/fxml/PaymentView.fxml");
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
        URL url = getClass().getResource("/com/utc2/apartmentmanagement/fxml/ReportView.fxml");
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

//    public void handleButtonSetting(ActionEvent actionEvent) throws IOException {
//        System.out.println("Đang cố gắng tải SettingView.fxml");
//        URL url = getClass().getResource("/com/utc2/apartmentmanagement/fxml/SettingView.fxml");
//        System.out.println("URL: " + (url != null ? url.toString() : "null"));
//
//        FXMLLoader loader = new FXMLLoader(url);
//        if (url == null) {
//            System.out.println("Không tìm thấy file SettingView.fxml");
//            return;
//        }
//
//        Parent SettingView = loader.load();
//
//        // In ra để debug
//        System.out.println("ContentArea: " + (contentArea != null ? "không null" : "null"));
//
//        // Thiết lập kích thước view để lấp đầy contentArea
//        AnchorPane.setTopAnchor(SettingView, 0.0);
//        AnchorPane.setRightAnchor(SettingView, 0.0);
//        AnchorPane.setBottomAnchor(SettingView, 0.0);
//        AnchorPane.setLeftAnchor(SettingView, 0.0);
//
//        // Xóa tất cả các view hiện tại và thêm ApartmentView
//        contentArea.getChildren().clear();
//        contentArea.getChildren().add(SettingView);
//        System.out.println("Đã thêm ReportView vào contentArea");
//    }
        double x,y = 0;
    @FXML
    public void loadMyProfile() {
        try {
            URL url = getClass().getResource("/com/utc2/apartmentmanagement/fxml/MyProfileView.fxml");
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
        System.out.println("Đang cố gắng tải ReportView.fxml");
        URL url = getClass().getResource("/com/utc2/apartmentmanagement/fxml/HRView.fxml");
        System.out.println("URL: " + (url != null ? url.toString() : "null"));

        FXMLLoader loader = new FXMLLoader(url);
        if (url == null) {
            System.out.println("Không tìm thấy file ReportView.fxml");
            return;
        }

        Parent ReportView = loader.load();
        HRViewController controller = loader.getController();
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
}