package org.example.apartmentmanagement.Payment.vnpay;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jakarta.servlet.annotation.WebServlet;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
public class VnpayReturnServer {
    public static void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/payment_return", new VnpayHandler());
        server.setExecutor(null); // default executor
        server.start();
        System.out.println("Mini server đã khởi động tại http://localhost:8080/payment_return");
    }

    static class VnpayHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "<h1>Đã nhận kết quả thanh toán!</h1>";
            Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery());
            String code = params.get("vnp_ResponseCode");

            if ("00".equals(code)) {
                response += "<p style='color:green;'>Thanh toán thành công!</p>";
                // Gọi hàm JavaFX để hiển thị thông báo thành công
                Platform.runLater(() -> showAlert("Thanh toán thành công!"));
            } else {
                response += "<p style='color:red;'>Thanh toán thất bại hoặc bị huỷ.</p>";
                // Gọi hàm JavaFX để hiển thị thông báo thất bại
                Platform.runLater(() -> showAlert("Thanh toán không thành công."));
            }


            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();

            // Log ra kết quả (hoặc gọi lại JavaFX nếu bạn dùng giao diện)
            System.out.println("Mã phản hồi từ VNPAY: " + code);

        }

        private Map<String, String> queryToMap(String query) {
            Map<String, String> result = new HashMap<>();
            if (query == null) return result;
            for (String param : query.split("&")) {
                String[] entry = param.split("=");
                if (entry.length > 1) {
                    result.put(entry[0], URLDecoder.decode(entry[1]));
                }
            }
            return result;
        }
    }
    private static void showAlert(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.show();
        });
    }

}
