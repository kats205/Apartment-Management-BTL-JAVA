package com.utc2.apartmentmanagement.Payment.Service;

import com.utc2.apartmentmanagement.DAO.Billing.PaymentDAO;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;

public class PaymentService {
    private PaymentDAO paymentDAO;

    public PaymentService() {
        this.paymentDAO = new PaymentDAO();
    }

    // Phương thức xử lý toàn bộ quá trình lưu thanh toán VNPay
    public void processVnpayPayment(String apartmentId, double totalAmount, int billedTo,
                                    String serviceName, Map<String, String> vnpayReturnParams) throws SQLException {

        try {
            // 1. Tạo bill trước
            int billId = paymentDAO.saveBill(apartmentId, LocalDate.now(), totalAmount, billedTo);

            // 2. Lấy thông tin từ VNPay return params
            String vnpResponseCode = vnpayReturnParams.get("vnp_ResponseCode");
            String transactionId = vnpayReturnParams.get("vnp_TxnRef");

            // Xác định trạng thái thanh toán
            String paymentStatus = "00".equals(vnpResponseCode) ? "completed" : "failed";

            // 3. Lưu thông tin payment
            int paymentId = paymentDAO.addPayment(billId, totalAmount, "VNPay", transactionId, paymentStatus);

            // 4. Lưu chi tiết VNPay return nếu thanh toán thành công
            if ("completed".equals(paymentStatus)) {
                paymentDAO.saveVnpayReturn(paymentId, vnpayReturnParams);
            }

            System.out.println("Đã lưu thành công thông tin thanh toán với Payment ID: " + paymentId);

        } catch (SQLException e) {
            System.err.println("Lỗi khi lưu thông tin thanh toán: " + e.getMessage());
            throw e;
        }
    }

    // Phương thức xử lý thanh toán chuyển khoản
    public void processTransferPayment(String apartmentId, double totalAmount, int billedTo, String serviceName) throws SQLException {
        try {
            // 1. Tạo bill
            int billId = paymentDAO.saveBill(apartmentId, LocalDate.now(), totalAmount, billedTo);

            // 2. Tạo payment với trạng thái pending (chờ xác nhận)
            String transactionId = "TRANSFER_" + System.currentTimeMillis();
            int paymentId = paymentDAO.addPayment(billId, totalAmount, "Transfer", transactionId, "pending");

            System.out.println("Đã tạo payment chờ xác nhận với ID: " + paymentId);

        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo payment chuyển khoản: " + e.getMessage());
            throw e;
        }
    }

    // Phương thức xác nhận thanh toán chuyển khoản
    public void confirmTransferPayment(int paymentId) throws SQLException {
        paymentDAO.updatePaymentStatus(paymentId, "completed");
    }
}
