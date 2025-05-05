package com.utc2.apartmentmanagement.Controller;

import com.utc2.apartmentmanagement.DAO.UserDAO;
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
import java.sql.SQLException;
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
    @FXML public AnchorPane rootPane;
    @Setter
    private DashboardController parentDashBoardController;
    @Setter
    private UserDashboardController parentUserDashBoardController;
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
        // set tên và thời gian đăng nhập của người dùng
        setInformationUser();
        // set lại avatar cho người dùng
        try {
            String filePath = new UserDAO().getAvatarPathByUserId(Session.getUserName());
            if(filePath!=null){
                Path pathImage = Paths.get(System.getProperty("user.home"), "apartment_app", "avatars", filePath);
                Image image = new Image(pathImage.toUri().toString());
                userAvatar.setImage(image);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    private void setInformationUser(){
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

// Lọc file chỉ cho phép chọn ảnh
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

// Mở cửa sổ chọn file ảnh
        File selectedFile = fileChooser.showOpenDialog(changeAvatarBtn.getScene().getWindow());

        if (selectedFile != null) {
            try {
                // Thư mục đích: C:/Users/<Tên người dùng>/apartment_app/avatars
                Path destinationDir = Paths.get(System.getProperty("user.home"), "apartment_app", "avatars");
                Files.createDirectories(destinationDir); // Tạo thư mục nếu chưa có

                // Tên file mới (giữ nguyên hoặc có thể đổi tên nếu muốn)
                int user_id = new UserDAO().getIdByUserName(Session.getUserName());
                String newFileName = "user_" + user_id + "_" + selectedFile.getName(); // Gợi ý đặt tên duy nhất
                Path destinationFile = destinationDir.resolve(newFileName);
                Session.setAvatarPath(newFileName);
                // Copy ảnh vào thư mục
                Files.copy(selectedFile.toPath(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
                //Lưu tên ảnh vào database tương ứng với user_id
                if(new UserDAO().updateAvatar(user_id, newFileName)){
                    System.out.println("Đã cập nhật ảnh đại diện vào database!");
                }
                // Hiển thị ảnh mới lên ImageView
                Image image = new Image(destinationFile.toUri().toString());
                userAvatar.setImage(image);

                // Lưu đường dẫn vào Session (hoặc DB nếu bạn muốn lưu lâu dài)
                Session.setAvatarPath(destinationFile.toString());

                System.out.println("Đã thay đổi ảnh đại diện thành công!");

            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
