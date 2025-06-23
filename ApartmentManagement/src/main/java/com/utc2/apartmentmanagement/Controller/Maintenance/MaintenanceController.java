package com.utc2.apartmentmanagement.Controller.Maintenance;

import com.utc2.apartmentmanagement.Controller.User.UserDashboardController;
import com.utc2.apartmentmanagement.DAO.Apartment.ApartmentDAO;
import com.utc2.apartmentmanagement.DAO.Maintenance.MaintenanceRequestDAO;
import com.utc2.apartmentmanagement.DAO.User.UserDAO;
import com.utc2.apartmentmanagement.Model.Maintenance.MaintenanceRequest;
import com.utc2.apartmentmanagement.Model.Session;
import com.utc2.apartmentmanagement.Utils.AlertBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
    private void initialize() throws SQLException {
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

    private void fillResidentInfo() throws SQLException {
        // TODO: Get actual data from logged-in user session
        // For now, using dummy data
        String userName = Session.getUserName();
        int userId = new UserDAO().getIdByUserName(userName);
        Map<String, Object> apartmentInf = new ApartmentDAO().getInformation(userId);


        apartmentIdTextField.setText(String.valueOf(apartmentInf.get("apartment_id")));
        residentIdTextField.setText(String.valueOf((apartmentInf.get("resident_id"))));

        // These fields should be read-only
        apartmentIdTextField.setEditable(false);
        residentIdTextField.setEditable(false);
    }

    @FXML
    private void handleSubmit() {
        // Validate required fields
        if (!validateForm()) return;

        try {
            // 1. Thu thập dữ liệu
            String apartmentID = apartmentIdTextField.getText();
            String residentID = residentIdTextField.getText();

            String issueType = issueTypeComboBox.getValue();
            String priorityText = priorityComboBox.getValue();
            String priority = extractPriority(priorityText);
            String location = locationComboBox.getValue();
            String subject = subjectTextField.getText();
            String description = descriptionTextArea.getText();
            String phoneNumber = phoneNumberTextField.getText();
            String availableTime = availableTimeComboBox.getValue();
            LocalDate requestDate = requestDatePicker.getValue();

            // 2. Insert bản ghi maintenance request
            MaintenanceRequestDAO requestDAO = new MaintenanceRequestDAO();
            int requestId = requestDAO.saveMaintenaceRequest(
                    apartmentID, residentID, requestDate,
                    location + " - " + description, priority, issueType
            );

            // 3. Nếu có ảnh, lưu ảnh và cập nhật image_filename
            if (selectedFile != null) {
                Path destinationDir = Paths.get("uploads", "maintenance");
                if (!Files.exists(destinationDir)) {
                    Files.createDirectories(destinationDir);
                }

                String fileExtension = getFileExtension(selectedFile.getName());
                String newFileName = "request_" + requestId + "_" + System.currentTimeMillis() + fileExtension;
                Path destinationFile = destinationDir.resolve(newFileName);

                Files.copy(selectedFile.toPath(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
                requestDAO.updateImageFileName(requestId, newFileName);
            }

            // 4. Thông báo
            showAlert("Success", "Maintenance request submitted successfully!",
                    "Your request has been recorded and will be processed according to its priority level.",
                    Alert.AlertType.INFORMATION);

            // 5. Xoá form hoặc quay về dashboard
            ((Pane) maintenanceView.getParent()).getChildren().clear();
            parentController.getContentArea().getChildren().setAll(parentController.getDashboardNodes());

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Something went wrong!", e.getMessage(), Alert.AlertType.ERROR);
        }
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

    @FXML
    public void handleAddFileImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh đại diện");

        // Lọc file chỉ cho phép chọn ảnh
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );

        // Mở cửa sổ chọn file ảnh
        File selectedFile = fileChooser.showOpenDialog(uploadPhotoButton.getScene().getWindow());

        if (selectedFile != null) {
            try {
                // 1. Tạo thư mục uploads/avatars nếu chưa tồn tại
                Path destinationDir = Paths.get("uploads", "maintenance");
                if (!Files.exists(destinationDir)) {
                    Files.createDirectories(destinationDir);
                    System.out.println("Đã tạo thư mục: " + destinationDir.toAbsolutePath());
                }

                // 2. Lấy thông tin user
                int userId = new UserDAO().getIdByUserName(Session.getUserName());
                System.out.println("User ID(Lấy thông tin user): " + userId);
                // 3. Xóa ảnh cũ (nếu có)
//                deleteOldAvatar(userId);

                // 4. Tạo tên file mới (unique)
                String fileExtension = getFileExtension(selectedFile.getName());
                String newFileName = "user_" + userId + "_" + System.currentTimeMillis() + fileExtension;
                Path destinationFile = destinationDir.resolve(newFileName);

                // 5. Copy ảnh vào thư mục
                Files.copy(selectedFile.toPath(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Đã copy ảnh đến: " + destinationFile.toAbsolutePath());

                // 6. Cập nhật database
                MaintenanceRequestDAO maintenanceRequestDAO = new MaintenanceRequestDAO();
                List<MaintenanceRequest> list = maintenanceRequestDAO.getAllMaintenanceRequest();
                int requestId = getRequestId(list);
                if (maintenanceRequestDAO.updateImageFileName(requestId, newFileName)) {
                    System.out.println("Đã cập nhật ảnh đính kèm vào database!");

                    // 9. Thông báo thành công
                    AlertBox.showAlert("Thành công", "Đã thay đổi ảnh đại diện thành công!");

                } else {
                    // Nếu cập nhật DB thất bại, xóa file đã copy
                    Files.deleteIfExists(destinationFile);
                    AlertBox.showAlert("Lỗi", "Không thể cập nhật ảnh đại diện vào database!");
                }

            } catch (IOException e) {
                System.err.println("Lỗi I/O: " + e.getMessage());
                AlertBox.showAlert("Lỗi", "Không thể lưu file ảnh: " + e.getMessage());
            } catch (SQLException e) {
                System.err.println("Lỗi SQL: " + e.getMessage());
                AlertBox.showAlert("Lỗi", "Lỗi database: " + e.getMessage());
            }
        }
    }
    public int getRequestId(List<MaintenanceRequest> list){
        String issueType = issueTypeComboBox.getValue();
        String priorityText = priorityComboBox.getValue();
        Date requestDate = Date.valueOf(requestDatePicker.getValue());
        for(MaintenanceRequest m : list){
            if(m.getRequestDate().equals(requestDate) && m.getIssueType().equals(issueType) && m.getPriority().equals(priorityText)){
                return m.getRequestID();
            }
        }
        return -1; // Not found
    }
    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex).toLowerCase();
        }
        return ".jpg"; // default extension
    }
}