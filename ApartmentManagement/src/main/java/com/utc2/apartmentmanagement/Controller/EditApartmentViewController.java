package com.utc2.apartmentmanagement.Controller;

import com.utc2.apartmentmanagement.DAO.ApartmentDAO;
import com.utc2.apartmentmanagement.Model.Apartment;
import com.utc2.apartmentmanagement.Model.Building;
import com.utc2.apartmentmanagement.Utils.ValidateColumn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class EditApartmentViewController implements Initializable {
    @FXML
    public TextField buildingID;
    @FXML
    private TextField apartmentIdField;

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
    private ApartmentViewController parentController = new ApartmentViewController();

    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<String> statusList = Arrays.asList("Trống", "Đã thuê", "Đang bảo trì", "Không khả dụng");
        statusComboBox.setItems(FXCollections.observableArrayList(statusList));
        setupValidators();
        buildingID.setEditable(false);
        apartmentIdField.setEditable(false);
        floorField.setEditable(false);
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
    public void setApartment(Apartment apartment) throws IOException {
        if(apartment != null) {
            apartmentIdField.setText(apartment.getApartmentID());
            buildingID.setText(String.valueOf(apartment.getBuildingID()));
            floorField.setText(String.valueOf(apartment.getFloors()));
            areaField.setText(String.valueOf(apartment.getArea()));
            bedroomsField.setText(String.valueOf(apartment.getBedRoom()));
            priceField.setText(String.valueOf(apartment.getPriceApartment()));
            maintenanceFeeField.setText(String.valueOf(apartment.getMaintanceFee()));
            statusComboBox.setValue(apartment.getStatus());
        }
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

        if (buildingID.getText() == null) {
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
            // Lấy thông tin từ giao diện
            String apartmentID = apartmentIdField.getText();
            String buildingId = buildingID.getText();
            String floor = floorField.getText();
            String area = areaField.getText();
            String bedrooms = bedroomsField.getText();
            String price = priceField.getText();
            String maintenanceFee = maintenanceFeeField.getText();
            String status = statusComboBox.getValue();

            // Tạo đối tượng Apartment với thông tin đã chuẩn hóa
            Apartment newApartment = new Apartment(
                    apartmentID,
                    Integer.parseInt(buildingId),
                    Integer.parseInt(floor),
                    Double.parseDouble(area),
                    Integer.parseInt(bedrooms),
                    Double.parseDouble(price),
                    status,
                    parseWithoutExponential(maintenanceFee) // Dùng hàm để tránh "E"
            );

            // Cập nhật căn hộ
            if(new ApartmentDAO().updateApartment(newApartment)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText("Cập nhật thành công");
                alert.setContentText("Thông tin căn hộ đã được cập nhật thành công!");
                alert.showAndWait();
                refreshTable();
            }


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
            refreshTable();
            closeForm();
        }
    }

    public void refreshTable() {
        parentController.loadDataApartment();
    }

    private void closeForm() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    // Thiết lập controller cha
    public void setParentController(ApartmentViewController controller) {
        this.parentController = controller;
    }
    public static double parseWithoutExponential(String value) {
        try {
            // Sử dụng DecimalFormat để format theo số bình thường
            DecimalFormat format = new DecimalFormat("0.##########"); // đảm bảo không có chữ E
            return format.parse(value).doubleValue();
        } catch (Exception e) {
            return 0.0;  // Nếu không phải số hợp lệ thì trả về 0.0
        }
    }

}