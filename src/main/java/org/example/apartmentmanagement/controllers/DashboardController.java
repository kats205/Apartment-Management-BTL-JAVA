package org.example.apartmentmanagement.controllers;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import org.example.apartmentmanagement.Views.ApartmentView;

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
}

