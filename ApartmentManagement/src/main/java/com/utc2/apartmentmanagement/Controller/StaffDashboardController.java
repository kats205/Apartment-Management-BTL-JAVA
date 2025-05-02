package com.utc2.apartmentmanagement.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class StaffDashboardController implements Initializable {

    @FXML public ImageView Exit;

    public void loadMyProfile(ActionEvent actionEvent) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Exit.setOnMouseClicked(e ->
                System.exit(0));
    }
}
