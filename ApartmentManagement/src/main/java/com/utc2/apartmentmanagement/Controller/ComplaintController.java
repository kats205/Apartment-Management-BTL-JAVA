package com.utc2.apartmentmanagement.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import lombok.Setter;

public class ComplaintController {
    @Setter
    private UserDashboardController parentController;

    @FXML
    private AnchorPane ComplaintView;


    public void handleCloseButton(ActionEvent event) {
        // Xoá apartment view
        ((Pane) ComplaintView.getParent()).getChildren().clear();
        // Thêm lại dashboard nodes từ controller cha
        parentController.getContentArea().getChildren().setAll(parentController.getDashboardNodes());
    }
}
