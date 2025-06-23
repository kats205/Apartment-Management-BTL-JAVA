package com.utc2.apartmentmanagement.Controller.Complaint;

import com.utc2.apartmentmanagement.Controller.User.UserDashboardController;
import com.utc2.apartmentmanagement.DAO.Apartment.ApartmentDAO;
import com.utc2.apartmentmanagement.DAO.Complaint.ComplaintRequestDAO;
import com.utc2.apartmentmanagement.DAO.User.UserDAO;
import com.utc2.apartmentmanagement.Model.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import lombok.Setter;
import lombok.SneakyThrows;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;
import java.util.ResourceBundle;

public class ComplaintsController implements Initializable {
    @FXML public AnchorPane ComplaintView;
    @Setter
    private UserDashboardController parentController;

    @FXML private ComboBox complaintTypeComboBox;
    @FXML private ComboBox priorityComboBox;
    @FXML private ComboBox locationComboBox;


    @FXML private TextField apartmentIdField;
    @FXML private TextField residentIdField;

    @FXML private DatePicker requestDatePicker;
    @FXML private TextArea descriptionArea;


    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set mặc định ngày gửi yêu cầu
        requestDatePicker.setValue(LocalDate.now());
        requestDatePicker.setDisable(true);

        fillResidentInfo();
    }


    private void fillResidentInfo() throws SQLException {
        // TODO: Get actual data from logged-in user session
        // For now, using dummy data
        String userName = Session.getUserName();
        int userId = new UserDAO().getIdByUserName(userName);
        Map<String, Object> apartmentInf = new ApartmentDAO().getInformation(userId);


        apartmentIdField.setText(String.valueOf(apartmentInf.get("apartment_id")));
        residentIdField.setText(String.valueOf((apartmentInf.get("resident_id"))));

        // These fields should be read-only
        apartmentIdField.setEditable(false);
        residentIdField.setEditable(false);
    }

    @FXML
    private void handleSubmitComplaint(){
        // Kiểm tra yêu cầu nhập dữ liêệu ( có hoặc không )
//        if(!validateFormComplaint()){
//            return;
//        }
        // Gán dữ liệu vào database
        String apartmentID = apartmentIdField.getText();
        String residentID = residentIdField.getText();

        String complaintType = (String) complaintTypeComboBox.getValue();
        String priorityText = (String) priorityComboBox.getValue();
        String priority = extractPriority(priorityText);
        String location = (String) locationComboBox.getValue();
        String description = descriptionArea.getText();
        LocalDate requestDate = requestDatePicker.getValue();

        // TODO: Save to database
        ComplaintRequestDAO requestDAO = new ComplaintRequestDAO();
        requestDAO.saveComplaintRequest(apartmentID, residentID, complaintType, requestDate, description, priority);

        showAlert("Success", "Complaint submitted successfully!",
                "Your complaint has been recorded and will be processed according to its priority level.",
                Alert.AlertType.INFORMATION);
    }

    private String extractPriority(String priorityText) {
        if (priorityText.startsWith("Low")) return "low";
        if (priorityText.startsWith("Medium")) return "medium";
        if (priorityText.startsWith("High")) return "high";
        if (priorityText.startsWith("Urgent")) return "urgent";
        return "medium"; // default
    }

    // TODO: kiểm tra dữ liệu đã nhập chưa
//    private boolean validateForm() {
//        StringBuilder errors = new StringBuilder();
//
//        if (issueTypeComboBox.getValue() == null) {
//            errors.append("- Please select an issue type\n");
//        }
//
//        if (priorityComboBox.getValue() == null) {
//            errors.append("- Please select a priority level\n");
//        }
//
//        if (locationComboBox.getValue() == null) {
//            errors.append("- Please select a location\n");
//        }
//
//        if (subjectTextField.getText().trim().isEmpty()) {
//            errors.append("- Please enter a subject\n");
//        }
//
//        if (descriptionTextArea.getText().trim().isEmpty()) {
//            errors.append("- Please enter a detailed description\n");
//        }
//
//        if (phoneNumberTextField.getText().trim().isEmpty()) {
//            errors.append("- Please enter your phone number\n");
//        } else if (!isValidPhoneNumber(phoneNumberTextField.getText().trim())) {
//            errors.append("- Please enter a valid phone number (10 digits starting with 0)\n");
//        }
//
//        if (availableTimeComboBox.getValue() == null) {
//            errors.append("- Please select your available time\n");
//        }
//
//        if (errors.length() > 0) {
//            showAlert("Validation Error", "Please fix the following errors:",
//                    errors.toString(), Alert.AlertType.ERROR);
//            return false;
//        }
//
//        return true;
//    }

    @FXML
    public void handleCancelButton(ActionEvent event) {
        // Xoá ComplaintView
        ((Pane) ComplaintView.getParent()).getChildren().clear();
        // Thêm lại dashboard nodes từ controller cha
        parentController.getContentArea().getChildren().setAll(parentController.getDashboardNodes());
    }

    private void showAlert(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}