package com.utc2.apartmentmanagement.Payment.Gateway.VnPay;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.utc2.apartmentmanagement.Payment.Service.PaymentService;
import javafx.application.Platform;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static com.utc2.apartmentmanagement.Utils.AlertBox.showAlert;

public class VnpayReturnServer {
    private static boolean paymentHandled = false;
    private static HttpServer server;
    private static PaymentService paymentService = new PaymentService();

    // Thêm các thông tin cần thiết để lưu vào DB
    private static String currentApartmentId;
    private static double currentTotalAmount;
    private static int currentBilledTo;
    private static String currentServiceName;

    // Phương thức để set thông tin trước khi thanh toán
    public static void setPaymentInfo(String apartmentId, double totalAmount, int billedTo, String serviceName) {
        currentApartmentId = apartmentId;
        currentTotalAmount = totalAmount;
        currentBilledTo = billedTo;
        currentServiceName = serviceName;
    }

    public static void start() throws IOException {
        if (server != null) {
            return; // Tránh khởi động nhiều server
        }

        server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/payment_returnUrl", new VnpayHandler());
        server.setExecutor(null); // default executor
        server.start();
        System.out.println("Mini server đã khởi động tại http://localhost:8080/payment_returnUrl");
    }

    public static void stop() {
        if (server != null) {
            server.stop(0);
            server = null;
            System.out.println("Mini server đã dừng");
        }
    }

    static class VnpayHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("Đã nhận yêu cầu từ VNPAY.");

            // Lấy các tham số từ URL
            Map<String, String> params = queryToMap(exchange.getRequestURI().getQuery());
            System.out.println("Các tham số nhận được từ VNPAY: " + params);

            // Kiểm tra mã phản hồi
            String code = params.get("vnp_ResponseCode");
            boolean isSuccess = "00".equals(code);

            // Lưu thông tin thanh toán vào database
            try {
                if (currentApartmentId != null) {
                    paymentService.processVnpayPayment(
                            currentApartmentId,
                            currentTotalAmount,
                            currentBilledTo,
                            currentServiceName,
                            params
                    );
                    System.out.println("Đã lưu thông tin thanh toán vào database thành công!");
                }
            } catch (SQLException e) {
                System.err.println("Lỗi khi lưu thông tin thanh toán: " + e.getMessage());
                e.printStackTrace();
            }

            // Tạo nội dung HTML tùy chỉnh dựa trên kết quả thanh toán
            String htmlContent = createCustomHtml(isSuccess, params);

            // Gửi phản hồi HTML tùy chỉnh
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, htmlContent.getBytes(StandardCharsets.UTF_8).length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(htmlContent.getBytes(StandardCharsets.UTF_8));
            }

            // Xử lý logic trong ứng dụng JavaFX
            if (!paymentHandled) {
                paymentHandled = true;
                if (isSuccess) {
                    Platform.runLater(() -> {
                        showAlert("Thông báo","Thanh toán thành công! Thông tin đã được lưu vào hệ thống.");
                        // Reset thông tin sau khi xử lý xong
                        resetPaymentInfo();
                    });
                } else {
                    Platform.runLater(() -> {
                        showAlert("Thông báo","Thanh toán không thành công.");
                        resetPaymentInfo();
                    });
                }
            }
        }

        private String createCustomHtml(boolean isSuccess, Map<String, String> params) {
            // Bạn có thể tạo một file HTML template hoặc tạo chuỗi HTML tùy chỉnh
            StringBuilder html = new StringBuilder();
            html.append("<!DOCTYPE html>");
            html.append("<html lang=\"vi\">");
            html.append("<head>");
            html.append("    <meta charset=\"UTF-8\">");
            html.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
            html.append("    <title>Kết quả thanh toán</title>");
            html.append("    <style>");
            html.append("        body { font-family: Arial, sans-serif; margin: 0; padding: 0; display: flex; justify-content: center; align-items: center; height: 100vh; background-color: #f5f5f5; }");
            html.append("        .payment-result { max-width: 800px; padding: 30px; border-radius: 10px; box-shadow: 0 0 20px rgba(0,0,0,0.1); background-color: white; text-align: center; }");
            html.append("        .success { color: #28a745; }");
            html.append("        .failure { color: #dc3545; }");
            html.append("        .details { margin-top: 20px; text-align: left; border-top: 1px solid #eee; padding-top: 20px; }");
            html.append("        table { width: 100%; border-collapse: collapse; }");
            html.append("        table td { padding: 8px; border-bottom: 1px solid #eee; }");
            html.append("    </style>");
            html.append("    <link rel=\"icon\" href=\"src/main/resources/com/utc2/apartmentmanagement/Images/logo.png\" type=\"image/png\">\n");
            html.append("</head>");
            html.append("<body>");
            html.append("    <div class=\"payment-result\">");

            if (isSuccess) {
                html.append("        <h1 class=\"success\">Thanh toán thành công!</h1>");
                html.append("        <p>Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi. Thông tin thanh toán đã được lưu vào hệ thống.</p>");
            } else {
                html.append("        <h1 class=\"failure\">Thanh toán không thành công</h1>");
                html.append("        <p>Rất tiếc, giao dịch của bạn không thể hoàn tất. Vui lòng thử lại sau.</p>");
            }

            html.append("        <div class=\"details\">");
            html.append("            <h3>Chi tiết giao dịch:</h3>");
            html.append("            <table>");

            // Hiển thị các thông tin giao dịch
            if (params.containsKey("vnp_TxnRef")) {
                html.append("                <tr><td>Mã giao dịch:</td><td>").append(params.get("vnp_TxnRef")).append("</td></tr>");
            }
            if (params.containsKey("vnp_Amount")) {
                // VNPAY trả về số tiền đã nhân 100
                long amount = Long.parseLong(params.get("vnp_Amount")) / 100;
                html.append("                <tr><td>Số tiền:</td><td>").append(formatCurrency(amount)).append(" VND</td></tr>");
            }
            if (params.containsKey("vnp_PayDate")) {
                String payDate = params.get("vnp_PayDate");
                // Format lại định dạng ngày nếu cần
                html.append("                <tr><td>Ngày thanh toán:</td><td>").append(formatDate(payDate)).append("</td></tr>");
            }
            if (params.containsKey("vnp_OrderInfo")) {
                html.append("                <tr><td>Nội dung thanh toán:</td><td>").append(params.get("vnp_OrderInfo")).append("</td></tr>");
            }
            if (params.containsKey("vnp_BankCode")) {
                html.append("                <tr><td>Ngân hàng:</td><td>").append(params.get("vnp_BankCode")).append("</td></tr>");
            }

            html.append("            </table>");
            html.append("        </div>");
            html.append("    </div>");
            html.append("</body>");
            html.append("</html>");

            return html.toString();
        }

        // Hàm định dạng tiền tệ
        private String formatCurrency(long amount) {
            return String.format("%,d", amount).replace(",", ".");
        }

        // Hàm định dạng ngày
        private String formatDate(String vnpayDate) {
            // Format yyyyMMddHHmmss -> dd/MM/yyyy HH:mm:ss
            if (vnpayDate != null && vnpayDate.length() == 14) {
                return vnpayDate.substring(6, 8) + "/" + vnpayDate.substring(4, 6) + "/" + vnpayDate.substring(0, 4) +
                        " " + vnpayDate.substring(8, 10) + ":" + vnpayDate.substring(10, 12) + ":" + vnpayDate.substring(12, 14);
            }
            return vnpayDate;
        }

        private Map<String, String> queryToMap(String query) {
            Map<String, String> result = new HashMap<>();
            if (query == null) return result;
            System.out.println("Câu truy vấn từ VNPAY: " + query);
            for (String param : query.split("&")) {
                String[] entry = param.split("=");
                if (entry.length > 1) {
                    result.put(entry[0], URLDecoder.decode(entry[1], StandardCharsets.UTF_8));
                } else if (entry.length == 1) {
                    result.put(entry[0], "");
                }
            }
            return result;
        }
    }

    // Phương thức reset thông tin sau khi xử lý
    private static void resetPaymentInfo() {
        currentApartmentId = null;
        currentTotalAmount = 0;
        currentBilledTo = 0;
        currentServiceName = null;
        paymentHandled = false;
    }
}