package com.utc2.apartmentmanagement.Controller;

import com.utc2.apartmentmanagement.DAO.ApartmentDAO;
import com.utc2.apartmentmanagement.Model.Apartment;
import com.utc2.apartmentmanagement.Utils.AlertBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class FormAddApartmentController implements Initializable {
    @FXML public TextField apartmentIdField;
    @FXML public ComboBox<Integer> buildingComboBox;
    @FXML public ComboBox<Integer> floorComboBox;
    @FXML public TextField areaField;
    @FXML public ComboBox<Integer> bedroomsComboBox;
    @FXML public TextField priceField;
    @FXML public ComboBox<String> statusComboBox;
    @FXML public TextField maintenanceFeeField;
    @FXML public Button saveButton;
    @FXML public Button cancelButton;

    @FXML
    private Label errorMessageLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize comboboxes
        try {
            initializeComboBoxes();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Hide error message initially
        if (errorMessageLabel != null) {
            errorMessageLabel.setVisible(false);
        }

        // Add focus listeners for real-time validation
        setupFocusListeners();
    }

    private void initializeComboBoxes() throws SQLException {
        // Building options
        List<Integer> buildingList = new ApartmentDAO().getAllBuildingId();
        buildingComboBox.getItems().addAll(buildingList);

        // Floor options
        List<Integer> floorList = new ApartmentDAO().getAllFloorId();
        floorComboBox.getItems().addAll(floorList);

        // Bedrooms options
        bedroomsComboBox.getItems().addAll(1,2,3,4);

        // Status options
        statusComboBox.getItems().addAll("available", "occupied", "maintenance", "reserved");
    }

    private void setupFocusListeners() {
        // Add focus lost listeners for real-time validation
        apartmentIdField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) { // focus lost
                validateApartmentId();
            }
        });

        areaField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) { // focus lost
                validateArea();
            }
        });

        priceField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) { // focus lost
                validatePrice();
            }
        });

        maintenanceFeeField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) { // focus lost
                validateMaintenanceFee();
            }
        });
    }

    @FXML
    public void handleSaveButton() {
        if (validateAllFields()) {
            handleSaveButton();
        }
    }

    @FXML
    public void handleCancelButton() {
        // Close the window or reset the form
        clearForm();
    }
    private boolean validateAllFields() {
        boolean isValid = true;

        // Reset error styles
        resetErrorStyles();

        // Validate apartment ID
        if (!validateApartmentId()) {
            isValid = false;
        }

        // Validate building selection
        if (buildingComboBox.getValue() == null) {
            setErrorStyle(buildingComboBox);
            showError("Vui lòng chọn tòa nhà");
            isValid = false;
        }

        // Validate floor selection
        if (floorComboBox.getValue() == null) {
            setErrorStyle(floorComboBox);
            showError("Vui lòng chọn tầng");
            isValid = false;
        }

        // Validate area
        if (!validateArea()) {
            isValid = false;
        }

        // Validate bedrooms selection
        if (bedroomsComboBox.getValue() == null) {
            setErrorStyle(bedroomsComboBox);
            showError("Vui lòng chọn số phòng ngủ");
            isValid = false;
        }

        // Validate price
        if (!validatePrice()) {
            isValid = false;
        }

        // Validate status selection
        if (statusComboBox.getValue() == null) {
            setErrorStyle(statusComboBox);
            showError("Vui lòng chọn trạng thái");
            isValid = false;
        }

        // Validate maintenance fee
        if (!validateMaintenanceFee()) {
            isValid = false;
        }

        return isValid;
    }

    /**
     * Validates the apartment ID format (e.g., A101, B202)
     */
    private boolean validateApartmentId() {
        String apartmentId = apartmentIdField.getText().trim();

        // Check if empty
        if (apartmentId.isEmpty()) {
            setErrorStyle(apartmentIdField);
            showError("Mã căn hộ không được để trống");
            return false;
        }

        // Check format (letter followed by numbers)
        Pattern pattern = Pattern.compile("^[A-Z][0-9]{3}$");
        if (!pattern.matcher(apartmentId).matches()) {
            setErrorStyle(apartmentIdField);
            showError("Mã căn hộ phải có dạng: 1 chữ cái in hoa và 3 số (VD: A101)");
            return false;
        }

        // TODO: Check if apartment ID already exists in database

        return true;
    }

    /**
     * Validates the area input (must be a positive number)
     */
    private boolean validateArea() {
        String areaText = areaField.getText().trim();

        // Check if empty
        if (areaText.isEmpty()) {
            setErrorStyle(areaField);
            showError("Diện tích không được để trống");
            return false;
        }

        try {
            double area = Double.parseDouble(areaText);
            if (area <= 0) {
                setErrorStyle(areaField);
                showError("Diện tích phải là số dương");
                return false;
            }
        } catch (NumberFormatException e) {
            setErrorStyle(areaField);
            showError("Diện tích phải là số hợp lệ");
            return false;
        }

        return true;
    }

    /**
     * Validates the price input (must be a positive number)
     */
    private boolean validatePrice() {
        String priceText = priceField.getText().trim();

        // Check if empty
        if (priceText.isEmpty()) {
            setErrorStyle(priceField);
            showError("Giá thuê không được để trống");
            return false;
        }

        try {
            // Remove commas if present
            priceText = priceText.replace(",", "");
            double price = Double.parseDouble(priceText);
            if (price <= 0) {
                setErrorStyle(priceField);
                showError("Giá thuê phải là số dương");
                return false;
            }
        } catch (NumberFormatException e) {
            setErrorStyle(priceField);
            showError("Giá thuê phải là số hợp lệ");
            return false;
        }

        return true;
    }

    /**
     * Validates the maintenance fee input (must be a positive number)
     */
    private boolean validateMaintenanceFee() {
        String feeText = maintenanceFeeField.getText().trim();

        // Check if empty
        if (feeText.isEmpty()) {
            setErrorStyle(maintenanceFeeField);
            showError("Phí bảo trì không được để trống");
            return false;
        }

        try {
            // Remove commas if present
            feeText = feeText.replace(",", "");
            double fee = Double.parseDouble(feeText);
            if (fee <= 0) {
                setErrorStyle(maintenanceFeeField);
                showError("Phí bảo trì phải là số dương");
                return false;
            }
        } catch (NumberFormatException e) {
            setErrorStyle(maintenanceFeeField);
            showError("Phí bảo trì phải là số hợp lệ");
            return false;
        }

        return true;
    }

    private void resetErrorStyles() {
        apartmentIdField.setStyle("");
        buildingComboBox.setStyle("");
        floorComboBox.setStyle("");
        areaField.setStyle("");
        bedroomsComboBox.setStyle("");
        priceField.setStyle("");
        statusComboBox.setStyle("");
        maintenanceFeeField.setStyle("");

        if (errorMessageLabel != null) {
            errorMessageLabel.setVisible(false);
        }
    }

    private void setErrorStyle(Control control) {
        control.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearForm() {
        apartmentIdField.clear();
        buildingComboBox.setValue(null);
        floorComboBox.setValue(null);
        areaField.clear();
        bedroomsComboBox.setValue(null);
        priceField.clear();
        statusComboBox.setValue(null);
        maintenanceFeeField.clear();

        resetErrorStyles();
    }
    private void showError(String message) {
        if (errorMessageLabel != null) {
            errorMessageLabel.setText(message);
            errorMessageLabel.setTextFill(Color.RED);
            errorMessageLabel.setVisible(true);
        }
    }
    public void handleCancelButton(ActionEvent actionEvent) {
        ((Stage) apartmentIdField.getScene().getWindow()).close();
    }

    public void handleSaveButton(ActionEvent actionEvent){
        String newApartmentId = apartmentIdField.getText();
        int newBuildingId = buildingComboBox.getValue();
        int newFloor = floorComboBox.getValue();
        double newArea = Double.parseDouble(areaField.getText());
        int newBedroom = bedroomsComboBox.getValue();
        double newPrice = Double.parseDouble(priceField.getText());
        String newStatus = statusComboBox.getValue();
        double newMaintenanceFee = Double.parseDouble(maintenanceFeeField.getText());

        Apartment newApartment = new Apartment(newApartmentId, newBuildingId, newFloor, newArea, newBedroom, newPrice, newStatus, newMaintenanceFee);
        if(new ApartmentDAO().addApartment(newApartment)){
            AlertBox.showAlertForUser("Thành công", "Căn hộ đã được thêm thành công!");
        }
        else{
            AlertBox.showAlertForUser("Thất bại", "Căn hộ đã tồn tại!");
        }
    }

}
