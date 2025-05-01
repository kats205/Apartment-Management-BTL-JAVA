package com.utc2.apartmentmanagement.Controller;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import lombok.Getter;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML
    public Button paymentButton;
    @FXML
    public Button ReportButton;
    @FXML
    public Button apartmentButton1;
    @FXML
    public Button paymentButton1;
    @FXML
    public Button reportButton1;
    @FXML
    private Button ApartmentButton;

    @FXML
    private ImageView Exit;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupMenuToggle();
//        mainBorderPane.setBottom(null); // Hoặc sử dụng Pane rỗng
//        mainBorderPane.setRight(null);  // Hoặc sử dụng Pane rỗng

        dashboardNodes = new ArrayList<>(contentArea.getChildren());

        Exit.setOnMouseClicked(event -> {
            System.exit(0);
        });

        slider.setTranslateX(-200);

        Menu.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(0);
            slide.play();

            slider.setTranslateX(-150);

            slide.setOnFinished((ActionEvent e)-> {
                Menu.setVisible(false);
                MenuBack.setVisible(true);
            });
        });

        MenuBack.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(-200);
            slide.play();

            slider.setTranslateX(0);

            slide.setOnFinished((ActionEvent e)-> {
                Menu.setVisible(true);
                MenuBack.setVisible(false);
            });
        });

        // Thiết lập sự kiện cho nút ApartmentButton
        ApartmentButton.setOnAction(event -> {
            try {
                loadApartmentView();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        apartmentButton1.setOnAction(event -> {
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
        paymentButton1.setOnAction(event -> {
            try {
                loadPaymentView();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        ReportButton.setOnAction(event -> {
            try {
                loadReportView();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        reportButton1.setOnAction(event -> {
            try {
                loadReportView();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        // Các thiết lập khác...


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
        // Create translation transition for sidebar
        TranslateTransition sidebarTransition = new TranslateTransition(Duration.millis(300), slider);

        // Create translation transition for content area
        TranslateTransition contentTransition = new TranslateTransition(Duration.millis(300), contentArea);

        if (isSidebarVisible) {
            // Hide sidebar
            sidebarTransition.setToX(-slider.getWidth());
            contentTransition.setToX(-slider.getWidth());
            // Swap menu labels
            Menu.setVisible(false);
            MenuBack.setVisible(true);
        } else {
            // Show sidebar
            sidebarTransition.setToX(0);
            contentTransition.setToX(150);

            // Swap menu labels
            Menu.setVisible(true);
            MenuBack.setVisible(false);
        }

        // Play both transitions
        sidebarTransition.play();
        contentTransition.play();

        // Toggle sidebar visibility state
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

    public void handleButtonSetting(ActionEvent actionEvent) throws IOException {
        System.out.println("Đang cố gắng tải SettingView.fxml");
        URL url = getClass().getResource("/com/utc2/apartmentmanagement/fxml/SettingView.fxml");
        System.out.println("URL: " + (url != null ? url.toString() : "null"));

        FXMLLoader loader = new FXMLLoader(url);
        if (url == null) {
            System.out.println("Không tìm thấy file SettingView.fxml");
            return;
        }

        Parent SettingView = loader.load();

        // In ra để debug
        System.out.println("ContentArea: " + (contentArea != null ? "không null" : "null"));

        // Thiết lập kích thước view để lấp đầy contentArea
        AnchorPane.setTopAnchor(SettingView, 0.0);
        AnchorPane.setRightAnchor(SettingView, 0.0);
        AnchorPane.setBottomAnchor(SettingView, 0.0);
        AnchorPane.setLeftAnchor(SettingView, 0.0);

        // Xóa tất cả các view hiện tại và thêm ApartmentView
        contentArea.getChildren().clear();
        contentArea.getChildren().add(SettingView);
        System.out.println("Đã thêm ReportView vào contentArea");
    }

    @FXML
    public void loadMyProfile(ActionEvent actionEvent) throws IOException {
        System.out.println("Đang cố gắng tải ReportView.fxml");
        URL url = getClass().getResource("/com/utc2/apartmentmanagement/fxml/MyProfileView.fxml");
        System.out.println("URL: " + (url != null ? url.toString() : "null"));

        FXMLLoader loader = new FXMLLoader(url);
        if (url == null) {
            System.out.println("Không tìm thấy file ReportView.fxml");
            return;
        }

        Parent ReportView = loader.load();
        SettingController controller = loader.getController();
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