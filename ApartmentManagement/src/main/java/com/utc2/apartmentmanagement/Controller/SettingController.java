package com.utc2.apartmentmanagement.Controller;

import com.utc2.apartmentmanagement.Model.Session;
import com.utc2.apartmentmanagement.Views.login;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import lombok.Setter;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;


public class SettingController implements Initializable {
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
    private boolean darkMode = false;




    public void handleThemeButton() {
        Region root = (Region) themeButton.getScene().getRoot();

        if (darkMode) {
            // Chuyển về Light Mode
            root.setStyle("-fx-background-color: white; -fx-text-fill: black;");

        } else {
            // Chuyển sang Dark Mode
            root.setStyle("-fx-background-color: #2b2b2b; -fx-text-fill: white;");
//            searchUserField.setStyle("-fx-text-fill: white;");
        }

        darkMode = !darkMode;
    }

    @Setter
    private DashboardController parentController;
    @FXML
    public void handleCloseButton(ActionEvent event) {
        // Xoá apartment view
        ((Pane)SettingView.getParent()).getChildren().clear();
        // Thêm lại dashboard nodes từ controller cha
        parentController.getContentArea().getChildren().setAll(parentController.getDashboardNodes());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        String userNameLbel = Session.getUserName();
//        LocalDate lastLoginLabel = Session.getLastLogin();
//        userName.setText(userNameLbel);
//        lastLogin.setText(String.valueOf(lastLoginLabel));
//        System.out.println("Last login: " + lastLogin);
//        System.out.println("User name: " + userName);


        String username = Session.getUserName();
        String lastlogin = Session.getLastLogin();
        System.out.println("Last login: " + lastLogin);
        System.out.println("User name: " + userName);
        userName.setText(username);
        lastLogin.setText(lastlogin);

    }
    @FXML
    public void handleLogout() throws Exception {
        ((Stage) logoutBtn.getScene().getWindow()).close();
        login login = new login();
        Stage stage = new Stage();
        login.start(stage);
    }
}
