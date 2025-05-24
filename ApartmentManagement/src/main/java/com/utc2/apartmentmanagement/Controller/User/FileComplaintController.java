package com.utc2.apartmentmanagement.Controller.User;

import com.utc2.apartmentmanagement.Controller.UserDashboardController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import lombok.Setter;

import java.net.URL;
import java.util.ResourceBundle;

public class FileComplaintController implements Initializable {
    @FXML public ComboBox complaintTypeComboBox;
    @FXML public ComboBox<String> priorityComboBox;
    @FXML public TextField apartmentIdTextField;
    @FXML public TextField residentIdTextField;
    @FXML public Label serviceLabel;
    @FXML public ComboBox serviceComboBox;
    @FXML public Label registrationLabel;
    @FXML public TextField registrationIdTextField;
    @FXML public TextField subjectTextField;
    @FXML public DatePicker complaintDatePicker;
    @FXML public TextArea descriptionTextArea;
    @FXML public Button uploadPhotoButton;
    @FXML public Label photoNameLabel;
    @FXML public Button cancelButton;
    @FXML public Button submitButton;

    @Setter
    private UserDashboardController parentController;

    @FXML private AnchorPane ComplaintView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        priorityComboBox.getItems().addAll("low", "high", "medium", "urgent");
    }


    public void handleUploadPhoto(ActionEvent event) {
    }

    public void handleCancel(ActionEvent event) {
        ((Pane) ComplaintView.getParent()).getChildren().clear();
        // Thêm lại dashboard nodes từ controller cha
        parentController.getContentArea().getChildren().setAll(parentController.getDashboardNodes());
    }

    public void handleSubmit(ActionEvent event) {
    }


}
