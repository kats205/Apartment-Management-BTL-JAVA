package org.example.apartmentmanagement.Controller;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
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
    private AnchorPane loginPane;

    @FXML
    private AnchorPane registerPane;

    @FXML
    void handleLogin(ActionEvent event) {
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.millis(500));
        slide.setNode(registerPane);

        slide.setToX(-550); // Di chuyển form Register vào vị trí của Login
        slide.play();
        loginPane.setVisible(false); // Ẩn Login Pane sau khi chuyển
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
        System.out.println("Checkbox đã được chọn! Mở form đăng ký...");
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.millis(500));
        slide.setNode(registerPane);

        slide.setToX(-550); // Đưa Register Pane ra ngoài
        slide.play();

        slide.setOnFinished((e) -> loginPane.setVisible(true)); // Hiện Login Pane lại
    }

    @FXML
    public void backLogin(){
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.millis(500));
        slide.setNode(registerPane);

        slide.setToX(600); // Đẩy registerPane ra ngoài màn hình
        slide.play();

        // Khi hiệu ứng hoàn thành, hiện lại loginPane
        slide.setOnFinished(event -> loginPane.setVisible(true));
    }
    public void handleForgotPassword(MouseEvent mouseEvent) {
        System.out.println("Redirecting to Forgot Password page...");
    }
}
