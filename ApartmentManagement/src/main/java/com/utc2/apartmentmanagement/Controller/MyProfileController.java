package com.utc2.apartmentmanagement.Controller;

import com.utc2.apartmentmanagement.Model.Session;
import com.utc2.apartmentmanagement.Views.login;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;


public class MyProfileController implements Initializable {
    @FXML public Button themeButton;
    @FXML public Button saveSettingsButton;
    @FXML public Button resetButton;
    @FXML public Button closeButton;
    @FXML public AnchorPane SettingView;
    @FXML public Button changeAvatarBtn;
    @FXML public ImageView userAvatar;
    @FXML public Label usernameLabel1;
    @FXML public Label roleLabel1;
    @FXML public Label lastLoginLabel1;
    @FXML public Label lastLogin;
    @FXML public Label userName;
    @FXML public TextField fullNameField;
    @FXML public TextField emailField;
    @FXML public TextField phoneField;
    @FXML public TextField officeField;
    @FXML public Button saveProfileBtn;
    @FXML public PasswordField currentPasswordField;
    @FXML public PasswordField confirmPasswordField;
    @FXML public PasswordField newPasswordField1;
    @FXML public Button changePasswordBtn1;
    @FXML public Button logoutBtn;
    @Setter
    private DashboardController parentDashBoardController;
    @Setter
    private StaffDashboardController parentStaffDashBoard;
    @Setter
    private UserDashboardController parentUserDashBoard;
    @Setter
    private Stage dashboardStage; // để tắt stage chính

    @FXML
    public void handleCloseButton(ActionEvent event) {
        // Xoá myProfile view
        ((Stage) changeAvatarBtn.getScene().getWindow()).close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String username = Session.getUserName();
        String lastlogin = Session.getLastLogin();
        System.out.println("Last login: " + lastLogin);
        System.out.println("User name: " + userName);
        userName.setText(username);
        lastLogin.setText(lastlogin);

    }
    @FXML
    public void handleLogout() throws Exception {
        if (dashboardStage != null) {
            dashboardStage.close();
            ((Stage)fullNameField.getScene().getWindow()).close();
        }

        // Mở lại login
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/utc2/apartmentmanagement/fxml/login-view.fxml"));
//        Parent root = loader.load();
//
//        Stage stage = new Stage();
//        stage.setScene(new Scene(root));
//        stage.setTitle("Login");
//        stage.show();
        Stage stage = new Stage();
        login loginView = new login();
        loginView.start(stage);
    }

    @FXML
    public void handleChangeAvatar(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh đại diện");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(changeAvatarBtn.getScene().getWindow());

        if (selectedFile != null) {
            try {
                // Tạo thư mục lưu ảnh (ví dụ: C:/Users/YourName/apartment_app/avatars)
                Path destinationDir = Paths.get(System.getProperty("user.home"), "apartment_app", "avatars");
                Files.createDirectories(destinationDir);

                // Đặt tên file mới (giữ tên cũ hoặc đặt lại tuỳ ý)
                Path destinationFile = destinationDir.resolve(selectedFile.getName());

                // Copy file ảnh vào thư mục đích
                Files.copy(selectedFile.toPath(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

                // Hiển thị ảnh lên ImageView
                Image image = new Image(destinationFile.toUri().toString());
                userAvatar.setImage(image);

                // Ghi lại đường dẫn vào Session (hoặc sau này lưu vào file txt/db nếu muốn giữ lâu dài)
                Session.setAvatarPath(destinationFile.toString());

                System.out.println("Đã thay đổi ảnh đại diện");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
