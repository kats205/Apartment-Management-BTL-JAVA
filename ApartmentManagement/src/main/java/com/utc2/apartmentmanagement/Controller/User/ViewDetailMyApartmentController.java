package com.utc2.apartmentmanagement.Controller.User;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.text.NumberFormat;
import java.util.Locale;

public class ViewDetailMyApartmentController {

    @FXML
    private Label apartmentIdLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Label buildingNameLabel;

    @FXML
    private ImageView apartmentImage;

    @FXML
    private Label addressLabel;

    @FXML
    private Label floorLabel;

    @FXML
    private Label areaLabel;

    @FXML
    private Label bedroomsLabel;

    @FXML
    private Label maintenanceFeeLabel;

    @FXML
    private Label propertyValueLabel;

    @FXML
    private Label primaryResidentLabel;

    @FXML
    private Label moveInDateLabel;

    @FXML
    private Button closeButton;

    private NumberFormat currencyFormat;

    /**
     * Khởi tạo controller
     */
    @FXML
    public void initialize() {
        // Khởi tạo định dạng tiền tệ
        currencyFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

        // Thiết lập sự kiện đóng cửa sổ
        closeButton.setOnAction(event -> closeWindow());
    }

    /**
     * Thiết lập dữ liệu căn hộ để hiển thị
     * @param apartment Dữ liệu căn hộ cần hiển thị
     */
    public void setApartmentData(MyApartmentController.ApartmentData apartment) {
        // Thiết lập thông tin căn hộ


        // Thử tải ảnh căn hộ


        // Thiết lập các thông tin chi tiết

    }

    /**
     * Đóng cửa sổ chi tiết
     */
    private void closeWindow() {
        // Lấy cửa sổ chứa controller này
        Stage stage = (Stage) closeButton.getScene().getWindow();
        // Đóng cửa sổ
        stage.close();
    }
}