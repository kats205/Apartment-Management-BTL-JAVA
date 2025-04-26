package org.example.apartmentmanagement.Service;

import jakarta.servlet.annotation.WebServlet;
import javafx.concurrent.Task;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/payment-endpoint")
public class VNPayService {

    public static void callPaymentServlet(double amount, String bankCode, String language) {
        Task<String> paymentTask = new Task<>() {
            @Override
            protected String call() throws Exception {
                // Tạo các tham số
                Map<Object, Object> params = new HashMap<>();
                params.put("amount", String.valueOf(amount));
                params.put("bankCode", bankCode);
                params.put("language", language);

                // Tạo URL với các tham số
                String formData = params.entrySet().stream()
                        .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue().toString(), StandardCharsets.UTF_8))
                        .reduce((p1, p2) -> p1 + "&" + p2)
                        .orElse("");

                // Tạo HTTP request
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/your-app-context/your-servlet-path"))
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .POST(HttpRequest.BodyPublishers.ofString(formData))
                        .build();

                // Gửi request và nhận response
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                return response.body();
            }
        };

        // Xử lý khi hoàn thành
        paymentTask.setOnSucceeded(e -> {
            String paymentUrl = paymentTask.getValue();
            try {
                // Mở trình duyệt với URL thanh toán
                java.awt.Desktop.getDesktop().browse(new URI(paymentUrl));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Xử lý lỗi
        paymentTask.setOnFailed(e -> {
            System.err.println("Payment failed: " + paymentTask.getException().getMessage());
        });

        // Chạy task trong thread riêng
        new Thread(paymentTask).start();
    }
}
