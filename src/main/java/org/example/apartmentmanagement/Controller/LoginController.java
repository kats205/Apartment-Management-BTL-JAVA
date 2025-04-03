package org.example.apartmentmanagement.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.apartmentmanagement.DAO.UserDAO;
import org.example.apartmentmanagement.Utils.AlertBox;
import org.example.apartmentmanagement.Utils.TryCatchUtil;

import java.io.IOException;

public class LoginController {

    public Button registerBtn;
    @FXML
    public Label forgotPassword;
    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUsername;

    @FXML
    void handleLogin(ActionEvent event) {
        String userName = txtUsername.getText();
        String password = txtPassword.getText();
        UserDAO userDAO = new UserDAO();
        int role_id = userDAO.login(userName, password);
        switch (role_id) {
            case 1 -> AlertBox.showAlertForUser("Thông báo","Chào mừng quản lý!");
            case 2 -> AlertBox.showAlertForUser("Thông báo","Chào mừng nhân viên!");
            case 3 -> AlertBox.showAlertForUser("Thông báo","Chào mừng cư dân!");
            default -> System.out.println("Invalid username or password!");
        }
    }
    private void showRegisterForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FormRegister.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Form Đăng Ký");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void registerAccountBtn() throws IOException {
        registerBtn.setOnAction(event -> {

                System.out.println("Checkbox đã được chọn! Mở form đăng ký...");
                showRegisterForm();

        });
    }
    public void handleForgotPassword(MouseEvent mouseEvent) {
        System.out.println("Redirecting to Forgot Password page...");
    }

}
