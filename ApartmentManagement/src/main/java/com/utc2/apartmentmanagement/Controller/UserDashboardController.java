package com.utc2.apartmentmanagement.Controller;

import com.utc2.apartmentmanagement.Controller.User.MyApartmentController;
import com.utc2.apartmentmanagement.DAO.ApartmentDAO;
import com.utc2.apartmentmanagement.DAO.ResidentDAO;
import com.utc2.apartmentmanagement.DAO.UserDAO;
import com.utc2.apartmentmanagement.Model.Apartment;
import com.utc2.apartmentmanagement.Model.Session;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.security.cert.PolicyNode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.Node;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;

public class UserDashboardController implements Initializable {
    @FXML public Label apartmentIdTf;
    @FXML public Label buildingTF;
    @FXML public Label areaTF;
    @FXML public Label floorTF;


    // AnchorPane giao diện màn hình chính
    @Getter
    @FXML private AnchorPane contentArea;
    // tương tự với viewDetail
    @FXML private Button MyApartmentTop;
    // button view detail apartment gán giao diện đã làm vào màn hình chính
    @FXML private Button ViewDetailApartment;
    // tương tự với ViewDetail
    @FXML private Button MyApartmentLeft;

    // tương tự với viewDetail
    @FXML private Button serviceTop;
    // button view detail apartment gán giao diện đã làm vào màn hình chính
    @FXML private Button ViewDetailservice;
    // tương tự với ViewDetail
    @FXML private Button serviceLeft;

    @FXML private Button ComplaintTop;
    @FXML private Button reportIcidentTop;
    @FXML private Button MyProfileTop;
    @FXML private Button ReportIcidentLeft;
    @FXML private Button MyProfileLeft;


    // Button xử lý sự kiện đóng MyApartmentView
    @FXML private Button closeMyApartmentView;
    @Getter
    @FXML private List<Node> dashboardNodes;
    @FXML private ImageView Exit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Exit.setOnMouseClicked(e -> {
            System.exit(0);
        });
        try {
            getInformationResident();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // setup sự kiện cho apartment
        setOnActionForApartment();
        //setup sự kiện cho service
        setOnActionForService();
        //setup sự kiện cho ComPlaint
        setOnActionForComPlaint();
        //setup sự kiện cho MyProfile
        setOnActionForMyProfile();
        //setup sự kiện cho ReportIncident
        setOnActionForReportIncident();
        // Danh sách các node trong contentArea
        dashboardNodes = new ArrayList<>(contentArea.getChildren());
    }

    private void setOnActionForApartment(){
        // set sự kiện cho button nằm bên sideBar
        ViewDetailApartment.setOnAction(e -> {;
            try {
                loadMyApartmentView();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        // set sự kiện cho button nằm header nav
        MyApartmentLeft.setOnAction(e -> {;
            try {
                loadMyApartmentView();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        MyApartmentTop.setOnAction(e -> {;
            try {
                loadMyApartmentView();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void setOnActionForService(){
        // set sự kiện cho button nằm bên sideBar
        ViewDetailservice.setOnAction(e -> {;
            try {
                loadServiceView();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        // set sự kiện cho button nằm header nav
        serviceLeft.setOnAction(e -> {;
            try {
                loadServiceView();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        serviceTop.setOnAction(e -> {;
            try {
                loadServiceView();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }


    private void setOnActionForComPlaint(){
        // set sự kiện cho button nằm bên sideBar
        ComplaintTop.setOnAction(e -> {;
            try {
                loadComplaintView();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }


    private void setOnActionForReportIncident(){
        // set sự kiện cho button nằm bên sideBar
        reportIcidentTop.setOnAction(e -> {;
            try {
                loadReportIncidentView();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        ReportIcidentLeft.setOnAction(e -> {;
            try {
                loadReportIncidentView();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void setOnActionForMyProfile(){
        // set sự kiện cho button nằm bên sideBar
        MyProfileLeft.setOnAction(e -> {;
            try {
                loadMyProfileView();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        // set sự kiện cho button nằm header nav
        MyProfileTop.setOnAction(e -> {;
            try {
                loadMyProfileView();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    // lấy các thông tin cơ bản của căn hộ từ người dùng đăng nhập vào
    private void getInformationResident() throws SQLException {
        String userName = Session.getUserName();
        int userId = new UserDAO().getIdByUserName(userName);
        String apartment_id = new ResidentDAO().getApartmentIdByUserID(userId);
        System.out.println("userName: " + userName);
        System.out.println("apartment_id: " + apartment_id);
        List<Apartment> apartmentList = new ApartmentDAO().getAllApartments();
        apartmentIdTf.setText(apartment_id);
        List<Object> objectList = new ApartmentDAO().getApartmentInfoByApartmentID(apartment_id);
        buildingTF.setText(objectList.get(0).toString());
        floorTF.setText(objectList.get(1).toString());
        areaTF.setText(objectList.get(2).toString());
    }

    @Setter
    private UserDashboardController parentController;
    // load giao diện MyApartment
    public void loadMyApartmentView() throws IOException {
        System.out.println("Đang cố gắng tải MyApartmentView.fxml");
        // khi có giao diện thì gán lại đường dẫn cho phù hợp
        URL url = getClass().getResource("/com/utc2/apartmentmanagement/fxml/User/MyApartmentView.fxml");
        System.out.println("URL: " + (url != null ? url.toString() : "null"));

        FXMLLoader loader = new FXMLLoader(url);
        if (url == null) {
            System.out.println("Không tìm thấy file MyApartmentView.fxml");
            return;
        }

        Parent ReportView = loader.load();
        MyApartmentController controller = loader.getController();
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
        System.out.println("Đã thêm MyApartmentView vào contentArea");
    }

    // load giao diện service
    public void loadServiceView() throws IOException {
        System.out.println("Đang cố gắng tải Service.fxml");
        // khi có giao diện thì gán lại đường dẫn cho phù hợp
        URL url = getClass().getResource("/com/utc2/apartmentmanagement/fxml/ServiceView.fxml");
        System.out.println("URL: " + (url != null ? url.toString() : "null"));

        FXMLLoader loader = new FXMLLoader(url);
        if (url == null) {
            System.out.println("Không tìm thấy file Service.fxml");
            return;
        }

        Parent ReportView = loader.load();
        ServiceController controller = loader.getController();
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
        System.out.println("Đã thêm Service vào contentArea");
    }

    public void loadComplaintView() throws IOException {
        System.out.println("Đang cố gắng tải ComplaintView.fxml");
        // khi có giao diện thì gán lại đường dẫn cho phù hợp
        URL url = getClass().getResource("/com/utc2/apartmentmanagement/fxml/ComplaintView.fxml");
        System.out.println("URL: " + (url != null ? url.toString() : "null"));

        FXMLLoader loader = new FXMLLoader(url);
        if (url == null) {
            System.out.println("Không tìm thấy file ComplaintView.fxml");
            return;
        }

        Parent ReportView = loader.load();
        ComplaintController controller = loader.getController();
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
        System.out.println("Đã thêm ComplaintView vào contentArea");
    }

    public void loadReportIncidentView() throws IOException {
        System.out.println("Đang cố gắng tải ReportIncidentView.fxml");
        // khi có giao diện thì gán lại đường dẫn cho phù hợp
        URL url = getClass().getResource("/com/utc2/apartmentmanagement/fxml/ReportIncidentView.fxml");
        System.out.println("URL: " + (url != null ? url.toString() : "null"));

        FXMLLoader loader = new FXMLLoader(url);
        if (url == null) {
            System.out.println("Không tìm thấy file ReportIncidentView.fxml");
            return;
        }

        Parent ReportView = loader.load();
        ReportController controller = loader.getController();
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
        System.out.println("Đã thêm ReportIncidentView vào contentArea");
    }

    double x = 0, y = 0;
    public void loadMyProfileView() throws IOException {
        try {
            URL url = getClass().getResource("/com/utc2/apartmentmanagement/fxml/MyProfileView.fxml");
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();

            // Gán controller
            MyProfileController controller = loader.getController();
            controller.setParentUserDashBoardController(this);
            controller.setDashboardStage((Stage) contentArea.getScene().getWindow()); // Stage của dashboard!

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
