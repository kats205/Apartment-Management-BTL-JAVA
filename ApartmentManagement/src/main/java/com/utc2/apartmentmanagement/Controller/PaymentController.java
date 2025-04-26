package com.utc2.apartmentmanagement.Controller;

import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.web.WebView;
import com.utc2.apartmentmanagement.DAO.PaymentDAO;
import com.utc2.apartmentmanagement.Model.Payment;
import com.utc2.apartmentmanagement.Payment.vnpay.VnpayRedirectUrlBuilder;
import com.utc2.apartmentmanagement.Payment.vnpay.VnpayReturnServer;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.List;

public class PaymentController {
    @FXML
    private Button btnPayVnpay;
    @FXML
    private WebView webView;
    @FXML
    private void handlePayVnpay() throws Exception {
        try {
            // 1. Khởi động server mini

// Trong PaymentController hoặc nơi nào bạn gọi hàm thanh toán
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(() -> {
                try {
                    VnpayReturnServer.start(); // chạy trong background
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            // chạy song song nhận phản hồi
            // 2. Tạo URL thanh toán và mở trình duyệt
            JsonObject jb = VnpayRedirectUrlBuilder.createVnpayUrl(100000);
            for (Map.Entry<String, com.google.gson.JsonElement> entry : jb.entrySet()) {
                String key = entry.getKey(); // Lấy khóa
                com.google.gson.JsonElement value = entry.getValue(); // Lấy giá trị

                System.out.println("Key: " + key + ", Value: " + value);
            }
            String url = jb.get("data").getAsString();
            System.out.println("Mở trình duyệt tới: " + url);
            java.awt.Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
