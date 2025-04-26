package org.example.apartmentmanagement.Controller;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import org.example.apartmentmanagement.Utils.AlertBox;
import org.example.apartmentmanagement.Views.ApartmentView;
import org.example.apartmentmanagement.Views.PaymentForManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    public Button report;
    @FXML
    public Button payment;
    @FXML
    public Button apartment;
    @FXML
    public AnchorPane DisplayAnchorPane;
    @FXML
    private ImageView Exit;

    @FXML
    private Label Menu;

    @FXML
    private Label MenuBack;

    @FXML
    private AnchorPane slider;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Exit.setOnMouseClicked(event -> {
            System.exit(0);
        });

        slider.setTranslateX(-176);

        Menu.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(0);
            slide.play();

            slider.setTranslateX(-176);

            slide.setOnFinished((ActionEvent e)-> {
                Menu.setVisible(false);
                MenuBack.setVisible(true);
            });
        });

        MenuBack.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(-176);
            slide.play();

            slider.setTranslateX(0);

            slide.setOnFinished((ActionEvent e)-> {
                Menu.setVisible(true);
                MenuBack.setVisible(false);
            });
        });
    }

    public void openApartment(ActionEvent actionEvent) {
        ApartmentView view = new ApartmentView();
        Parent views= view.getView();
        DisplayAnchorPane.getChildren().clear();
        DisplayAnchorPane.getChildren().add(views);

        // Anchor full
        AnchorPane.setTopAnchor(views, 0.0);
        AnchorPane.setBottomAnchor(views, 0.0);
        AnchorPane.setLeftAnchor(views, 0.0);
        AnchorPane.setRightAnchor(views, 0.0);
    }

    public void openPayment(ActionEvent actionEvent) {
        try {
            // Tải giao diện FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PaymentForManager.fxml"));
            BorderPane billListView = loader.load();
            billListView.setMinSize(700,600);
            // Xóa các phần tử con hiện tại trong DisplayAnchorPane
            DisplayAnchorPane.getChildren().clear();
            // Thêm giao diện mới vào DisplayAnchorPane
            DisplayAnchorPane.getChildren().add(billListView);
            // Thiết lập anchor để giao diện chiếm toàn bộ không gian
            AnchorPane.setTopAnchor(billListView, 0.0);
            AnchorPane.setBottomAnchor(billListView, 0.0);
            AnchorPane.setLeftAnchor(billListView, 0.0);
            AnchorPane.setRightAnchor(billListView, 0.0);

        } catch (IOException e) {
            e.printStackTrace();
            // Hiển thị thông báo lỗi cho người dùng
            AlertBox.showAlertForExeptionRegister("Lỗi", "Không load giao diện được!");
        }
    }
}

