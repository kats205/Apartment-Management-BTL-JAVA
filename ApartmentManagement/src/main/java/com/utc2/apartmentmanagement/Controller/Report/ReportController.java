package com.utc2.apartmentmanagement.Controller.Report;

import com.utc2.apartmentmanagement.Controller.User.UserDashboardController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import lombok.Setter;

public class ReportController {
    @Setter
    private UserDashboardController parentController;

    @FXML
    private AnchorPane ReportView;


    public void handleCloseButton(ActionEvent event) {
        // Xoá apartment view
        ((Pane) ReportView.getParent()).getChildren().clear();
        // Thêm lại dashboard nodes từ controller cha
        parentController.getContentArea().getChildren().setAll(parentController.getDashboardNodes());
    }
}
