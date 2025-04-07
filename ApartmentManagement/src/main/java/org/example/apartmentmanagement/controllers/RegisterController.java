package org.example.apartmentmanagement.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Pane imagePane;

    @FXML
    private ImageView imageView;

    @FXML
    public void initialize() {
        // Đảm bảo rằng phần ảnh được bo góc bên phải
        setupImagePane();
    }

    private void setupImagePane() {
        // Sử dụng SVGPath để tạo hình dạng với góc bo tròn ở bên phải
        SVGPath path = new SVGPath();
        path.setContent("M454 0H174C80.1116 0 4 76.1116 4 170V430C4 523.888 80.1116 600 174 600H454V0Z");

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
    public void goToLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("@../fxml/login-view.fxml"));
            AnchorPane signupPane = loader.load();

            Scene scene = new Scene(signupPane);
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Lỗi khi chuyển sang màn hình đăng nhập: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
