package com.utc2.apartmentmanagement.DAO.Billing;

import com.utc2.apartmentmanagement.DAO.DatabaseConnection;
import com.utc2.apartmentmanagement.Model.Billing.Payment;
import com.utc2.apartmentmanagement.Repository.Billing.IPaymentDAO;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.sql.Date;
import java.util.Map;

public class PaymentDAO implements IPaymentDAO {
    @Override
    public List<Payment> getAllPayment(){
        List<Payment> paymentList = new ArrayList<>();
        String sql = "SELECT * FROM payment";
        try{
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                paymentList.add(new Payment(rs.getInt("payment_id"), rs.getInt("bill_id"), rs.getFloat("amount"),
                        rs.getDate("payment_date"), rs.getString("payment_method"), rs.getString("transaction_id"),
                        rs.getString("status"), rs.getDate("created_at"), rs.getDate("updated_at")));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return paymentList;
    }

    public boolean deletePaymentByID(int paymentID){
        String sql = "DELETE FROM Payment WHERE payment_id = ?";
        try{
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, paymentID);
            return stmt.executeUpdate() > 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

//    public boolean addPayment(Payment payment) {
//        String sql = "INSERT INTO Payment(payment_id, bill_id, amount, payment_date, payment_method, transaction_id, status, created_at, updated_at" +
//                " VALUES( ? , ? , ? , ? , ? , ? , ? , ? , ?";
//        try(Connection connection = DatabaseConnection.getConnection();
//        PreparedStatement stmt = connection.prepareStatement(sql)){
//            stmt.setInt(1, payment.getPaymentID());
//            stmt.setInt(2, payment.getBillID());
//            stmt.setDouble(3, payment.getAmount());
//            stmt.setDate(4, payment.getPaymentDate());
//            stmt.setNString(5, payment.getPaymentMedthod());
//            stmt.setString(6, payment.getTransactionID());
//            stmt.setNString(7, payment.getStatus());
//            stmt.setDate(8, Date.valueOf(LocalDate.now()));
//            stmt.setDate(9, Date.valueOf(LocalDate.now()));
//            return stmt.executeUpdate() > 0;
//        }catch (SQLException e){
//            e.printStackTrace();
//        }
//        return false;
//    }


    // Phương thức lưu thông tin bill trước
    public int saveBill(String apartmentId, LocalDate billingDate, double totalAmount, int billedTo) throws SQLException {
        String sql = "INSERT INTO Bill (apartment_id, billing_date, total_amount, billed_to, status) " +
                "OUTPUT INSERTED.bill_id " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, apartmentId);
            stmt.setDate(2, Date.valueOf(billingDate));
            stmt.setDouble(3, totalAmount);
            stmt.setInt(4, billedTo);
            stmt.setString(5, "paid");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new SQLException("Failed to create bill");
        }
    }

    // Phương thức lưu thông tin payment
    @Override
    public int addPayment(int billId, double amount, String paymentMethod, String transactionId, String status) throws SQLException {
        String sql = "INSERT INTO Payment (bill_id, amount, payment_date, payment_method, transaction_id, status) " +
                "OUTPUT INSERTED.payment_id " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, billId);
            stmt.setDouble(2, amount);
            stmt.setDate(3, Date.valueOf(LocalDate.now()));
            stmt.setString(4, paymentMethod);
            stmt.setString(5, transactionId);
            stmt.setString(6, status);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new SQLException("Failed to create payment");
        }
    }

    // Phương thức lưu thông tin VNPay return
    public void saveVnpayReturn(int paymentId, Map<String, String> vnpayParams) throws SQLException {
        String sql = "INSERT INTO VnpayReturn (payment_id, vnp_bank_code, vnp_pay_date, vnp_transaction_no, " +
                "vnp_TmnCode, vnp_SecureHash, vnp_order_info, vnp_txn_ref, vnp_Amount, vnp_card_type, " +
                "vnp_transaction_status, vnp_bank_tran_no, vnp_response_code) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, paymentId);
            stmt.setString(2, vnpayParams.get("vnp_BankCode"));
            stmt.setString(3, vnpayParams.get("vnp_PayDate"));
            stmt.setString(4, vnpayParams.get("vnp_TransactionNo"));
            stmt.setString(5, vnpayParams.get("vnp_TmnCode"));
            stmt.setString(6, vnpayParams.get("vnp_SecureHash"));
            stmt.setString(7, vnpayParams.get("vnp_OrderInfo"));
            stmt.setString(8, vnpayParams.get("vnp_TxnRef"));
            stmt.setString(9, vnpayParams.get("vnp_Amount"));
            stmt.setString(10, vnpayParams.get("vnp_CardType"));
            stmt.setString(11, vnpayParams.get("vnp_TransactionStatus"));
            stmt.setString(12, vnpayParams.get("vnp_BankTranNo"));
            stmt.setString(13, vnpayParams.get("vnp_ResponseCode"));

            stmt.executeUpdate();
        }
    }

    // Phương thức cập nhật trạng thái payment
    public void updatePaymentStatus(int paymentId, String status) throws SQLException {
        String sql = "UPDATE Payment SET status = ?, updated_at = GETDATE() WHERE payment_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, paymentId);
            stmt.executeUpdate();
        }
    }

    @Override
    public boolean updatebillID(int paymentID, int newbBillId) {
        return updatePaymentField(paymentID, "bill_id", newbBillId);
    }

    @Override
    public boolean updateAmount(int paymentID, double newAmount) {
        return updatePaymentField(paymentID, "amount", newAmount);
    }

    @Override
    public boolean updatePaymentDate(int paymentID, Date newPaymentDate) {
        return updatePaymentField(paymentID, "payment_date", newPaymentDate);
    }

    @Override
    public boolean updatePaymentMethod(int paymentID, String newPaymentMethod) {
        return updatePaymentField(paymentID, "payment_method", newPaymentMethod);
    }

    @Override
    public boolean updateTransactionID(int paymentID, String newTransactionID) {
        return updatePaymentField(paymentID, "transaction_id", newTransactionID);
    }

    @Override
    public boolean updateStatusPayment(int paymentID, String newStatus) {
        return updatePaymentField(paymentID, "status", newStatus);
    }

    @Override
    public List<Payment> findPaymentByDate(LocalDate fromDate, LocalDate toDate) {
        String sql = "SELECT * FROM Payment WHERE payment_date BETWEEN ? AND ?";
        List<Payment> paymentList = new ArrayList<>();
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setDate(1, Date.valueOf(fromDate));
            stmt.setDate(2, Date.valueOf(toDate));
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                paymentList.add(new Payment(rs.getInt("payment_id"), rs.getInt("bill_id"), rs.getFloat("amount"),
                        rs.getDate("payment_date"), rs.getString("payment_method"), rs.getString("transaction_id"),
                        rs.getString("status"), rs.getDate("created_at"), rs.getDate("updated_at")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi lấy dữ liệu",e);
        }
        return paymentList;
    }

    @Override
    public List<Map<String, Object>> listPaymentDetail() throws SQLException {
        String sql = "SELECT \n" +
                "    p.payment_id, \n" +
                "    b.bill_id, \n" +
                "    bi.apartment_id, \n" +
                "    p.amount, \n" +
                "    p.payment_method, \n" +
                "    p.status, \n" +
                "    b.item_type, \n" +
                "    b.description\n" +
                "FROM Payment p\n" +
                "JOIN BillItem b ON p.bill_id = b.bill_id\n" +
                "JOIN Bill bi ON p.bill_id = bi.bill_id";
        List<Map<String, Object>> listPaymentDetail = new ArrayList<>();
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)){
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Map<String, Object> rows = new HashMap<>();
                rows.put("payment_id", rs.getInt("payment_id"));
                rows.put("bill_id",rs.getInt("bill_id"));
                rows.put("apartment_id", rs.getString("apartment_id"));
                rows.put("amount", rs.getBigDecimal("amount")); // dùng getBigDecimal để chính xác hơn cho DECIMAL
                rows.put("payment_method", rs.getString("payment_method"));
                rows.put("status", rs.getString("status"));
                rows.put("item_type", rs.getString("item_type"));
                rows.put("description", rs.getString("description"));
                listPaymentDetail.add(rows);
            }
        }
    return listPaymentDetail;
    }
  
    public double totalPaymentFromStatus(String status) throws SQLException {
        String sql = "SELECT SUM(amount) FROM Payment WHERE status = ?";
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1,status);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }
        }catch (SQLException e){
            throw new SQLException("Lỗi khi tính tổng tiền thanh toán", e);
        }
        return 0;
    }

    public boolean updatePaymentField(int paymentID, String field, Object newValue){
        String sql = "UPDATE payment SET " + field + " = ? , updated_at = ? WHERE payment_id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);){
            if(newValue instanceof String){
                stmt.setNString(1, (String)newValue);
            }
            else if(newValue instanceof Float){
                stmt.setFloat(1,(Float)newValue);
            }
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
            stmt.setInt(3, paymentID);
            return stmt.executeUpdate() > 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    public Payment findPaymentByApartmentId(String apartmentID) throws SQLException{
        String sql = "SELECT p.* FROM Payment p" +
                " JOIN Bill b ON b.bill_id = p.bill_id" +
                " JOIN Apartment a ON a.apartment_id = b.apartment_id" +
                " WHERE a.apartment_id =  ? ";
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, apartmentID);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return new Payment(rs.getInt("payment_id"), rs.getInt("bill_id"), rs.getFloat("amount"),
                        rs.getDate("payment_date"), rs.getString("payment_method"), rs.getString("transaction_id"),
                        rs.getString("status"), rs.getDate("created_at"), rs.getDate("updated_at"));
            }
        }catch (SQLException e){
            throw new SQLException("Lỗi khi tìm kiếm thông tin thanh toán", e);
        }

        return null;
    }

}
