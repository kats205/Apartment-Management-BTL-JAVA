package org.example.apartmentmanagement.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;

public class LoginController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Pane imagePane;

    @FXML
    private ImageView imageView;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    public void initialize() {
        // Đảm bảo rằng phần ảnh được bo góc bên phải
        setupImagePane();
    }

    private void setupImagePane() {
        // Sử dụng SVGPath để tạo hình dạng với góc bo tròn ở bên phải
        SVGPath path = new SVGPath();
        path.setContent("M4 0H284C377.888 0 454 76.1116 454 170V430C454 523.888 377.888 600 284 600H4V0Z");

        // Áp dụng clip vào imagePane để tạo hiệu ứng bo góc bên phải
        if (imagePane != null) {
            imagePane.setClip(path);

            // Đảm bảo ảnh nằm hoàn toàn trong vùng bo góc
            if (imageView != null) {
                imageView.setFitWidth(450);
                imageView.setFitHeight(600);
                imageView.setPreserveRatio(false);

                // Đặt sự kiện thay đổi kích thước
                imagePane.widthProperty().addListener((obs, oldVal, newVal) -> {
                    imageView.setFitWidth(newVal.doubleValue());
                });

                imagePane.heightProperty().addListener((obs, oldVal, newVal) -> {
                    imageView.setFitHeight(newVal.doubleValue());
                });
            }
        }
    }

    @FXML
    public void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.equals("admin") && password.equals("admin")) {
            
            System.out.println("Login successful!");
        } else {
            System.out.println("Invalid username or password!");
        }

        // Thực hiện xác thực đăng nhập tại đây
        System.out.println("Đang đăng nhập với tên người dùng: " + username);

        // Thêm logic chuyển đến màn hình chính sau khi đăng nhập thành công
    }

    @FXML
    public void goToSignUp(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/learn1/Views/signup.fxml"));
            AnchorPane signupPane = loader.load();

            Scene scene = new Scene(signupPane);
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Lỗi khi chuyển sang màn hình đăng ký: " + e.getMessage());
            e.printStackTrace();
        }
    }
}