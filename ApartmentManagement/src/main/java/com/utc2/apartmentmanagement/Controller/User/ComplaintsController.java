package com.utc2.apartmentmanagement.Controller.User;

import com.utc2.apartmentmanagement.Controller.UserDashboardController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import lombok.Setter;

import java.net.URL;
import java.util.ResourceBundle;

public class ComplaintsController implements Initializable {
    @FXML public AnchorPane ComplaintView;
    @Setter
    private UserDashboardController parentController;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    @FXML
    public void handleCancelButton(ActionEvent event) {
        // Xoá ComplaintView
        ((Pane) ComplaintView.getParent()).getChildren().clear();
        // Thêm lại dashboard nodes từ controller cha
        parentController.getContentArea().getChildren().setAll(parentController.getDashboardNodes());
    }


}
