package com.utc2.apartmentmanagement.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.scene.image.Image;

public class SettingViewController implements Initializable {

    @FXML
    private ImageView userAvatar;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label roleLabel;

    @FXML
    private Label lastLoginLabel;

    @FXML
    private TextField fullNameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField officeField;

    @FXML
    private PasswordField currentPasswordField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button saveProfileBtn;

    @FXML
    private Button changePasswordBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    private Button changeAvatarBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private void loadUserInfo() {

    }

    private void setupEventHandlers() {
        // Xử lý sự kiện lưu thông tin cá nhân
        saveProfileBtn.setOnAction(event -> saveUserProfile());

        // Xử lý sự kiện đổi mật khẩu
        changePasswordBtn.setOnAction(event -> changePassword());

        // Xử lý sự kiện đăng xuất
        logoutBtn.setOnAction(event -> logout());

        // Xử lý sự kiện thay đổi ảnh đại diện
        changeAvatarBtn.setOnAction(event -> changeAvatar());
    }

    private void saveUserProfile() {
        String fullName = fullNameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();

        if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Dữ liệu không hợp lệ", "Vui lòng điền đầy đủ thông tin cá nhân");
            return;
        }

        // Kiểm tra định dạng email
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            showAlert(Alert.AlertType.WARNING, "Dữ liệu không hợp lệ", "Email không đúng định dạng");
            return;
        }

        // Kiểm tra định dạng số điện thoại
        if (!phone.matches("0[0-9]{9}")) {
            showAlert(Alert.AlertType.WARNING, "Dữ liệu không hợp lệ", "Số điện thoại phải bắt đầu bằng 0 và có 10 chữ số");
            return;
        }


    }

    private void changePassword() {

    }

    private void changeAvatar() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh đại diện");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            try {
                // Trong ứng dụng thực tế, bạn nên lưu đường dẫn ảnh vào cơ sở dữ liệu
                // Đây chỉ là ví dụ hiển thị ảnh đã chọn
                Image image = new Image(selectedFile.toURI().toString());
                userAvatar.setImage(image);

                // Thông báo thành công
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Ảnh đại diện đã được cập nhật");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể đọc tệp ảnh: " + e.getMessage());
            }
        }
    }

    private void logout() {
        try {
            // Xóa thông tin phiên đăng nhập


            // Chuyển về màn hình đăng nhập
            // Đoạn code này sẽ phụ thuộc vào cấu trúc ứng dụng của bạn
            // Đây chỉ là một ví dụ đơn giản
            Stage stage = (Stage) logoutBtn.getScene().getWindow();
            stage.close();

            // Mở màn hình đăng nhập
            // Trong thực tế, bạn có thể sử dụng một service hoặc event để chuyển đổi màn hình
            // LoginController.showLoginStage();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể đăng xuất: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}