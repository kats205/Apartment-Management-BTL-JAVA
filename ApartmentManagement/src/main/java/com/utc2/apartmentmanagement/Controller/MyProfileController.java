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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import lombok.Setter;

import java.net.URL;
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
    private DashboardController parentController;
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/utc2/apartmentmanagement/fxml/login-view.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Login");
        stage.show();
    }
}
