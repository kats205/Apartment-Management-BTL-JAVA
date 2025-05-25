package com.utc2.apartmentmanagement.Controller;

import com.google.gson.JsonObject;
import com.utc2.apartmentmanagement.Payment.vnpay.VnpayRedirectUrlBuilder;
import com.utc2.apartmentmanagement.Payment.vnpay.VnpayReturnServer;

import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class PaymentController {
    private static final AtomicBoolean serverStarted = new AtomicBoolean(false);
    private ExecutorService executorService;

    @FXML
    private void handlePayVnpay() {
        try {
            // 1. Khởi động server mini (chỉ khởi động 1 lần)
            if (!serverStarted.getAndSet(true)) {
                executorService = Executors.newSingleThreadExecutor();
                executorService.submit(() -> {
                    try {
                        VnpayReturnServer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Platform.runLater(() -> showAlert("Lỗi: " + e.getMessage()));
                    }
                });
            }

            // Đặt lại cờ trạng thái xử lý thanh toán
            // VnpayReturnServer.resetPaymentHandled();  // (thêm phương thức này vào VnpayReturnServer nếu cần)

            // 2. Tạo URL thanh toán và mở trình duyệt
            JsonObject jb = VnpayRedirectUrlBuilder.createVnpayUrl(100000);
            String paymentUrl = jb.get("data").getAsString();

            System.out.println("Mở trình duyệt tới: " + paymentUrl);
            Desktop.getDesktop().browse(new URI(paymentUrl));

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    // Thêm phương thức để dừng server khi đóng ứng dụng
    public void shutdown() {
        VnpayReturnServer.stop();

        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}