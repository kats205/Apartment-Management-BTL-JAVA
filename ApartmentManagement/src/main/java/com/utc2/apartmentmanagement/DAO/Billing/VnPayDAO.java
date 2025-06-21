package com.utc2.apartmentmanagement.DAO.Billing;

import com.google.gson.JsonObject;
import com.utc2.apartmentmanagement.DAO.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class VnPayDAO {
    public boolean addPaymentByVnpay(JsonObject object, int paymentID)throws SQLException{
        String sql = "INSERT INTO VnPayReturn (payment_id, vnp_txn_ref, vnp_card_type," +
                "vnp_order_info, vnp_pay_date, vnp_response_code, vnp_transaction_status ) VALUES ( ?, ?, ?, ?, ?, ?, ? , ? , ? , ? )";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, paymentID);
            stmt.setString(3, "VNPAY");
            for (Map.Entry<String, com.google.gson.JsonElement> entry : object.entrySet()) {
                String key = entry.getKey(); // Lấy khóa
                com.google.gson.JsonElement value = entry.getValue(); // Lấy giá trị
                if(key.equalsIgnoreCase("txnRef")){
                    stmt.setString(2, value.toString());
                }
                else if(key.equalsIgnoreCase("orderInfo")){
                    stmt.setString(4, value.toString());
                }
                else if(key.equalsIgnoreCase("code")){
                    stmt.setString(6, value.toString());
                }
                else if(key.equalsIgnoreCase("createDate")){
                    stmt.setString(5, value.toString());
                }
                else if(key.equalsIgnoreCase("message")){
                    stmt.setString(7, value.toString());
                }
                System.out.println("Key: " + key + ", Value: " + value);
            }
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new SQLException("Đã xảy ra lỗi trong quá trình thêm dữ liệu vào vnpay!", e);
        }
    }
}
