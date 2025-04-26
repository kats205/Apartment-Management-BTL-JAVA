package org.example.apartmentmanagement.Views;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import java.io.File;

public class qrcode extends Application {
    private ImageView qrImageView;

//    @Override
//    public void start(Stage stage) {
//        // Label
//        Label label = new Label("Nhấn để thanh toán:");
//
//        // Nút thanh toán
//        Button payButton = new Button("Thanh toán");
//
//        // ImageView để hiện QR code
//        qrImageView = new ImageView();
//        qrImageView.setFitHeight(300);
//        qrImageView.setFitWidth(300);
//
//        // Sự kiện khi nhấn nút
//        payButton.setOnAction(e -> {
//            try {
//                String userId = "123";
//                int amount = 100000; // Có thể lấy từ DB
//
//                String paymentUrl = "https://www.facebook.com/";
//                File qrFile = generateQRCode(paymentUrl);
//
//                Image qrImage = new Image(qrFile.toURI().toString());
//                qrImageView.setImage(qrImage);
//
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        });
//
//        VBox root = new VBox(20, label, payButton, qrImageView);
//        root.setAlignment(Pos.CENTER);
//        root.setPadding(new Insets(20));
//
//        Scene scene = new Scene(root, 400, 500);
//        stage.setScene(scene);
//        stage.setTitle("Thanh toán QR");
//        stage.show();
//    }
    @Override
    public void start(Stage primaryStage) {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        // Load file HTML trong resources
        String htmlPath = getClass().getResource("/qrcode.html").toExternalForm();
        webEngine.load(htmlPath);

        VBox root = new VBox(webView);
        Scene scene = new Scene(root, 400, 300);

        primaryStage.setTitle("WebView QR Code Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Hàm sinh mã QR và lưu thành file tạm
    public File generateQRCode(String content) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 300, 300);

        File tempFile = File.createTempFile("qrcode_", ".png");
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", tempFile.toPath());

        return tempFile;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
