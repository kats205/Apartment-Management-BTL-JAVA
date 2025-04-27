package com.utc2.apartmentmanagement.Controller;

import com.utc2.apartmentmanagement.Model.Apartment;
import com.utc2.apartmentmanagement.Model.Building;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class EditApartmentViewController implements Initializable {

    @FXML
    private TextField apartmentIdField;

    @FXML
    private ComboBox<Building> buildingComboBox;

    @FXML
    private TextField floorField;

    @FXML
    private TextField areaField;

    @FXML
    private TextField bedroomsField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField maintenanceFeeField;

    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;
    private Apartment currentApartment;
    private ApartmentViewController parentController;

    public void initialize(URL url, ResourceBundle resourceBundle) {

        List<String> statusList = Arrays.asList("Trống", "Đã thuê", "Đang bảo trì", "Không khả dụng");
        statusComboBox.setItems(FXCollections.observableArrayList(statusList));

        loadBuildingList();
        setupValidators();
    }

    // Phương thức để load danh sách tòa nhà
    private void loadBuildingList() {

        // Hiển thị tên tòa nhà trong combo box
        buildingComboBox.setCellFactory(param -> new ListCell<Building>() {
            @Override
            protected void updateItem(Building item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getBuildingName());
                }
            }
        });

        buildingComboBox.setButtonCell(new ListCell<Building>() {
            @Override
            protected void updateItem(Building item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getBuildingName());
                }
            }
        });
    }

    // Phương thức thiết lập các validator cho các trường nhập liệu
    private void setupValidators() {
        // Validator cho trường tầng - chỉ cho phép nhập số
        floorField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                floorField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        // Validator cho trường diện tích - chỉ cho phép nhập số và dấu chấm
        areaField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*\\.?\\d*")) {
                areaField.setText(oldValue);
            }
        });

        // Validator cho trường số phòng ngủ - chỉ cho phép nhập số
        bedroomsField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                bedroomsField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        // Validator cho trường giá thuê - chỉ cho phép nhập số
        priceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                priceField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        // Validator cho trường phí bảo trì - chỉ cho phép nhập số
        maintenanceFeeField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                maintenanceFeeField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    // Phương thức để đặt dữ liệu căn hộ cần chỉnh sửa
    public void setApartment(Apartment apartment) {
        this.currentApartment = apartment;

        // Đổ dữ liệu vào form

        // Tìm và chọn tòa nhà tương ứng
    }

    // Phương thức xử lý sự kiện nút Lưu
    @FXML
    public void handleSaveButton(ActionEvent event) {
        if (validateInputs()) {
            updateApartment();

            // Cập nhật lại dữ liệu trong bảng căn hộ
            if (parentController != null) {

            }

            closeForm();
        }
    }

    // Phương thức để kiểm tra dữ liệu nhập vào
    private boolean validateInputs() {
        StringBuilder errors = new StringBuilder();

        if (buildingComboBox.getValue() == null) {
            errors.append("- Vui lòng chọn tòa nhà\n");
        }

        if (floorField.getText().isEmpty()) {
            errors.append("- Vui lòng nhập tầng\n");
        }

        if (areaField.getText().isEmpty()) {
            errors.append("- Vui lòng nhập diện tích\n");
        }

        if (bedroomsField.getText().isEmpty()) {
            errors.append("- Vui lòng nhập số phòng ngủ\n");
        }

        if (priceField.getText().isEmpty()) {
            errors.append("- Vui lòng nhập giá thuê\n");
        }

        if (maintenanceFeeField.getText().isEmpty()) {
            errors.append("- Vui lòng nhập phí bảo trì\n");
        }

        if (statusComboBox.getValue() == null) {
            errors.append("- Vui lòng chọn trạng thái\n");
        }

        if (errors.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi dữ liệu");
            alert.setHeaderText("Vui lòng kiểm tra lại các thông tin sau:");
            alert.setContentText(errors.toString());
            alert.showAndWait();
            return false;
        }

        return true;
    }

    // Phương thức để cập nhật thông tin căn hộ
    private void updateApartment() {
        try {
            // xử lý thêm thông tin căn hộ qua nút lưu



            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText("Cập nhật thành công");
            alert.setContentText("Thông tin căn hộ đã được cập nhật thành công!");
            alert.showAndWait();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi dữ liệu");
            alert.setHeaderText("Dữ liệu không hợp lệ");
            alert.setContentText("Vui lòng kiểm tra lại định dạng các trường số!");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText("Không thể cập nhật căn hộ");
            alert.setContentText("Lỗi: " + e.getMessage());
            alert.showAndWait();
        }
    }

    // Phương thức xử lý sự kiện nút Hủy
    @FXML
    public void handleCancelButton(ActionEvent event) {
        // Hiển thị hộp thoại xác nhận
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận");
        alert.setHeaderText("Bạn có chắc muốn hủy bỏ các thay đổi?");
        alert.setContentText("Các thay đổi sẽ không được lưu.");

        if (alert.showAndWait().get() == ButtonType.OK) {
            closeForm();
        }
    }

    public void refreshTable() {
        // Tải lại dữ liệu từ cơ sở dữ liệu và cập nhật bảng

    }

    private void closeForm() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    // Thiết lập controller cha
    public void setParentController(ApartmentViewController controller) {
        this.parentController = controller;
    }
}