package com.utc2.apartmentmanagement.Views;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Test extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        VBox serviceCard = createServiceCard(
                "https://via.placeholder.com/280x150.png", // Địa chỉ ảnh mẫu
                "Dịch vụ dọn vệ sinh",
                "100,000 VND",
                "giờ",
                "Dịch vụ vệ sinh chuyên nghiệp cho căn hộ của bạn"
        );

        StackPane root = new StackPane(serviceCard);
        Scene scene = new Scene(root);

        primaryStage.setTitle("Service Card Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createServiceCard(String addressImage, String serviceName, String priceService, String unit, String description) {
        VBox card = new VBox(10); // spacing giữa các thành phần
        card.setPrefHeight(350);
        card.setPrefWidth(300);
        card.setStyle("-fx-padding: 10; -fx-background-color: #f8f8f8; -fx-border-color: #cccccc; -fx-border-radius: 8; -fx-background-radius: 8;");

        // Hình ảnh dịch vụ
        ImageView imageView;
        try {
            if (addressImage == null || addressImage.isBlank()) {
                throw new IllegalArgumentException("Image path is null or empty");
            }

            Image image = new Image(addressImage, true);
            imageView = new ImageView(image);
        } catch (Exception e) {
            Image placeholder = new Image(getClass().getResource("/images/no-image.png").toExternalForm());
            imageView = new ImageView(placeholder);
        }

        imageView.setFitHeight(150);
        imageView.setFitWidth(280);
        imageView.setPreserveRatio(true);
        card.getChildren().add(imageView);

        // Tên dịch vụ
        Label serviceNameLabel = new Label(serviceName);
        serviceNameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1a936f;");
        card.getChildren().add(serviceNameLabel);

        // Giá dịch vụ
        Label priceServiceLabel = new Label(priceService + " / " + unit);
        priceServiceLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333;");
        card.getChildren().add(priceServiceLabel);

        // Mô tả dịch vụ
        Label descriptionLabel = new Label(description);
        descriptionLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #333333;");
        descriptionLabel.setWrapText(true);
        card.getChildren().add(descriptionLabel);

        // Nút đăng ký dịch vụ
        Button register = new Button("Register Service");
        register.setPrefHeight(40);
        register.setPrefWidth(200);
        register.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-color: #1A936F; -fx-text-fill: white;" +
                "-fx-cursor: hand; -fx-background-radius: 5;");
        register.setOnAction(e -> {
            System.out.println("Đã nhấn đăng ký dịch vụ: " + serviceName);
            // thêm xử lý logic tại đây nếu cần
        });

        card.getChildren().add(register);
        card.setAlignment(Pos.CENTER);

        return card;
    }
}
