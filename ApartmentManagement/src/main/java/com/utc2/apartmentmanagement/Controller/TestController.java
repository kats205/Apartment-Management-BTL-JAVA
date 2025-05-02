package com.utc2.apartmentmanagement.Controller;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TestController implements Initializable {
    @FXML
    public Button paymentButton;

    @FXML
    private Button ApartmentButton;

    @FXML
    private Button reportButton;

    @FXML
    private Button hrButton;

    @FXML
    private Button profileButton;

    @FXML
    private Button apartment;

    @FXML
    private Button payment;

    @FXML
    private Button report;

    @FXML
    private ImageView Exit;

    @FXML
    private Label Menu;

    @FXML
    private Label MenuBack;

    @FXML
    private AnchorPane slider;

    @FXML
    private AnchorPane contentArea;

    @FXML
    private Label totalApartmentsLabel;

    @FXML
    private Label revenueLabel;

    @FXML
    private Label requestsLabel;

    @FXML
    private TableView<?> recentActivitiesTable;

    @FXML
    private TableColumn<?, ?> dateColumn;

    @FXML
    private TableColumn<?, ?> actionColumn1;

    @FXML
    private TableColumn<?, ?> timeColumn;

    @FXML
    private TableColumn<?, ?> activityColumn;

    @FXML
    private TableColumn<?, ?> userColumn;

    @FXML
    private TableColumn<?, ?> statusColumn;

    @FXML
    private TableColumn<?, ?> actionColumn;

    private boolean isSidebarVisible = true;

    @FXML
    private BorderPane mainBorderPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupMenuToggle();
        setupDashboardStats();
        setupEventHandlers();


        mainBorderPane.setBottom(null);
        mainBorderPane.setRight(null);

        Exit.setOnMouseClicked(event -> {
            System.exit(0);
        });
    }

    private void setupDashboardStats() {
        // Set dashboard statistics (in a real app, these would come from a database)
        totalApartmentsLabel.setText("125");
        revenueLabel.setText("₫258M");
        requestsLabel.setText("42");
    }

    // tạo hiệu ứng trượt
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

    private void setupEventHandlers() {
        // Set event handlers for menu buttons
        ApartmentButton.setOnAction(event -> {
            try {
                loadView("ApartmentView.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        paymentButton.setOnAction(event -> {
            try {
                loadView("PaymentView.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        reportButton.setOnAction(event -> {
            try {
                loadView("ReportView.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        hrButton.setOnAction(event -> {
            try {
                loadView("HRView.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        profileButton.setOnAction(event -> {
            try {
                loadView("ProfileView.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Top menu handlers
        apartment.setOnAction(event -> {
            try {
                loadView("ApartmentView.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        payment.setOnAction(event -> {
            try {
                loadView("PaymentView.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        report.setOnAction(event -> {
            try {
                loadView("ReportView.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadView(String fxmlFileName) throws IOException {
        System.out.println("Đang cố gắng tải " + fxmlFileName);
        URL url = getClass().getResource("/com/utc2/apartmentmanagement/fxml/" + fxmlFileName);
        System.out.println("URL: " + (url != null ? url.toString() : "null"));

        if (url == null) {
            System.out.println("Không tìm thấy file " + fxmlFileName);
            return;
        }

        FXMLLoader loader = new FXMLLoader(url);
        Parent view = loader.load();

        // Thiết lập kích thước view để lấp đầy contentArea
        AnchorPane.setTopAnchor(view, 0.0);
        AnchorPane.setRightAnchor(view, 0.0);
        AnchorPane.setBottomAnchor(view, 0.0);
        AnchorPane.setLeftAnchor(view, 0.0);

        // Xóa tất cả các view hiện tại và thêm view mới
        contentArea.getChildren().clear();
        contentArea.getChildren().add(view);
        System.out.println("Đã thêm " + fxmlFileName + " vào contentArea");
    }
}