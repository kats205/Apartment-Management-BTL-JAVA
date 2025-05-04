package com.utc2.apartmentmanagement.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import lombok.Setter;

import java.net.URL;
import java.util.ResourceBundle;

@Setter
public class MyApartmentViewController {

    @Setter
    private UserDashboardController parentController;

    @FXML
    private AnchorPane MyApartmentView;


    public void handleCloseButton(ActionEvent event) {
        // Xoá apartment view
        ((Pane) MyApartmentView.getParent()).getChildren().clear();
        // Thêm lại dashboard nodes từ controller cha
        parentController.getContentArea().getChildren().setAll(parentController.getDashboardNodes());
    }


}
