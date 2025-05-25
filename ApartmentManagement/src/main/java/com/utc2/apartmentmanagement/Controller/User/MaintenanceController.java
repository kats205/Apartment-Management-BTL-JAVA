package com.utc2.apartmentmanagement.Controller.User;

import com.utc2.apartmentmanagement.Controller.UserDashboardController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import lombok.Setter;

import java.io.File;
import java.time.LocalDate;

public class MaintenanceController {

    @FXML public AnchorPane maintenanceView;
    @FXML private ComboBox<String> issueTypeComboBox;
    @FXML private ComboBox<String> priorityComboBox;
    @FXML private ComboBox<String> locationComboBox;
    @FXML private ComboBox<String> availableTimeComboBox;

    @FXML private TextField apartmentIdTextField;
    @FXML private TextField residentIdTextField;
    @FXML private TextField subjectTextField;
    @FXML private TextField phoneNumberTextField;

    @FXML private DatePicker requestDatePicker;
    @FXML private TextArea descriptionTextArea;

    @FXML private Label photoNameLabel;
    @FXML private Button uploadPhotoButton;
    @FXML private Button submitButton;
    @FXML private Button cancelButton;

    private File selectedFile;

    @Setter
    private UserDashboardController parentController;

    @FXML
    private void initialize() {
        // Set today's date
        requestDatePicker.setValue(LocalDate.now());
        requestDatePicker.setDisable(true); // Make it read-only

        // Load ComboBox data
        loadIssueTypes();
        loadPriorities();
        loadLocations();
        loadAvailableTimes();

        // Auto-fill resident info
        fillResidentInfo();
    }

    private void loadIssueTypes() {
        issueTypeComboBox.getItems().addAll(
                "Electrical Issues",
                "Plumbing Issues",
                "Air Conditioning",
                "Door/Lock Problems",
                "Elevator Issues",
                "Pest Control",
                "Window/Glass",
                "Paint/Wall Damage",
                "Floor/Tile Issues",
                "Other"
        );
    }

    private void loadPriorities() {
        priorityComboBox.getItems().addAll(
                "Low - Can wait 5-7 days",
                "Medium - Within 2-3 days",
                "High - Within 24 hours",
                "Urgent - Within 2-4 hours"
        );
    }

    private void loadLocations() {
        locationComboBox.getItems().addAll(
                "Living Room",
                "Master Bedroom",
                "Bedroom 2",
                "Bedroom 3",
                "Kitchen",
                "Master Bathroom",
                "Common Bathroom",
                "Balcony",
                "Entrance/Hallway",
                "Storage Room",
                "Other"
        );
    }

    private void loadAvailableTimes() {
        availableTimeComboBox.getItems().addAll(
                "Morning (8:00 - 12:00)",
                "Afternoon (13:00 - 17:00)",
                "Evening (17:00 - 20:00)",
                "Weekend - Saturday",
                "Weekend - Sunday",
                "Anytime",
                "Business Hours Only"
        );
    }

    private void fillResidentInfo() {
        // TODO: Get actual data from logged-in user session
        // For now, using dummy data
        apartmentIdTextField.setText("A101");
        residentIdTextField.setText("R001");

        // These fields should be read-only
        apartmentIdTextField.setEditable(false);
        residentIdTextField.setEditable(false);
    }

    @FXML
    private void handleSubmit() {
        // Validate required fields
        if (!validateForm()) {
            return;
        }

        // Prepare data for database
        String issueType = issueTypeComboBox.getValue();
        String priorityText = priorityComboBox.getValue();
        String priority = extractPriority(priorityText);
        String location = locationComboBox.getValue();
        String subject = subjectTextField.getText();
        String description = descriptionTextArea.getText();
        String phoneNumber = phoneNumberTextField.getText();
        String availableTime = availableTimeComboBox.getValue();
        LocalDate requestDate = requestDatePicker.getValue();

        // TODO: Save to database
        // MaintenanceRequest request = new MaintenanceRequest();
        // request.setApartmentId(apartmentIdTextField.getText());
        // request.setResidentId(residentIdTextField.getText());
        // request.setRequestDate(requestDate);
        // request.setDescription(description);
        // request.setStatus("pending");
        // request.setPriority(priority);
        // maintenanceDAO.save(request);

        // Show success message
        showAlert("Success", "Maintenance request submitted successfully!",
                "Your request has been recorded and will be processed according to its priority level.",
                AlertType.INFORMATION);

        // Clear form or close window
        clearForm();
    }

    @FXML
    private void handleCancel() {
        // Confirm cancellation
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Cancel Request");
        alert.setHeaderText("Are you sure you want to cancel?");
        alert.setContentText("All entered information will be lost.");

        if (alert.showAndWait().get() == ButtonType.OK) {
            // Close the window
            // Xoá ComplaintView
            ((Pane) maintenanceView.getParent()).getChildren().clear();
            // Thêm lại dashboard nodes từ controller cha
            parentController.getContentArea().getChildren().setAll(parentController.getDashboardNodes());
        }
    }

    @FXML
    private void handleUploadPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Photo");

        // Set file filters
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        // Show dialog
        Stage stage = (Stage) uploadPhotoButton.getScene().getWindow();
        selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            // Check file size (5MB limit)
            long fileSizeInMB = selectedFile.length() / (1024 * 1024);
            if (fileSizeInMB > 5) {
                showAlert("File Too Large", "Please select a file smaller than 5MB",
                        "", AlertType.WARNING);
                selectedFile = null;
                photoNameLabel.setText("No file selected");
            } else {
                photoNameLabel.setText(selectedFile.getName());
            }
        }
    }

    private boolean validateForm() {
        StringBuilder errors = new StringBuilder();

        if (issueTypeComboBox.getValue() == null) {
            errors.append("- Please select an issue type\n");
        }

        if (priorityComboBox.getValue() == null) {
            errors.append("- Please select a priority level\n");
        }

        if (locationComboBox.getValue() == null) {
            errors.append("- Please select a location\n");
        }

        if (subjectTextField.getText().trim().isEmpty()) {
            errors.append("- Please enter a subject\n");
        }

        if (descriptionTextArea.getText().trim().isEmpty()) {
            errors.append("- Please enter a detailed description\n");
        }

        if (phoneNumberTextField.getText().trim().isEmpty()) {
            errors.append("- Please enter your phone number\n");
        } else if (!isValidPhoneNumber(phoneNumberTextField.getText().trim())) {
            errors.append("- Please enter a valid phone number (10 digits starting with 0)\n");
        }

        if (availableTimeComboBox.getValue() == null) {
            errors.append("- Please select your available time\n");
        }

        if (errors.length() > 0) {
            showAlert("Validation Error", "Please fix the following errors:",
                    errors.toString(), AlertType.ERROR);
            return false;
        }

        return true;
    }

    private boolean isValidPhoneNumber(String phone) {
        // Vietnamese phone number validation: starts with 0 and has 10 digits
        return phone.matches("0\\d{9}");
    }

    private String extractPriority(String priorityText) {
        if (priorityText.startsWith("Low")) return "low";
        if (priorityText.startsWith("Medium")) return "medium";
        if (priorityText.startsWith("High")) return "high";
        if (priorityText.startsWith("Urgent")) return "urgent";
        return "medium"; // default
    }

    private void showAlert(String title, String header, String content, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearForm() {
        issueTypeComboBox.setValue(null);
        priorityComboBox.setValue(null);
        locationComboBox.setValue(null);
        subjectTextField.clear();
        descriptionTextArea.clear();
        phoneNumberTextField.clear();
        availableTimeComboBox.setValue(null);
        photoNameLabel.setText("No file selected");
        selectedFile = null;
    }
}
