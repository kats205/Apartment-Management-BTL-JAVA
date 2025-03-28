package org.example.apartmentmanagement.DAO;

import org.example.apartmentmanagement.Model.BillItems;
import org.example.apartmentmanagement.Model.Staff;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class BillItemDAO {
    private static List<BillItems> BillItemList = new ArrayList<BillItems>();

    public static void getAllBillItems(){
        BillItemList.clear();
        String sql = "select * from BillItem";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                BillItemList.add(new BillItems(rs.getInt("item_id"), rs.getInt("bill_id"), rs.getNString("item_type"),
                            rs.getNString("description"), rs.getFloat("amount"), rs.getInt("quantity"),
                        rs.getDouble("total"), rs.getDate("created_at"), rs.getDate("updated_at")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void showStaffList(){
        if(BillItemList.isEmpty()) getAllBillItems();
        System.out.println("===========================BillItem List===========================");
        for(BillItems billItems : BillItemList){
            System.out.println("-------------------------------------------------");
            System.out.println("Item ID: " + billItems.getItemID());
            System.out.println("Bill ID: " + billItems.getBillID());
            System.out.println("Item Type: " + billItems.getItemType());
            System.out.println("Description: " + billItems.getDescription());
            System.out.println("Amount: " + billItems.getAmount());
            System.out.println("Quantity: " + billItems.getQuantity());
            System.out.println("Total: " + billItems.getTotal());
            System.out.println("Created At: " + billItems.getCreated_at());
            System.out.println("Updated_at: " + billItems.getUpdated_at());
            System.out.println("-------------------------------------------------");
        }
    }

    //thêm 1 staff vào DB
    public static void addBillItem( int itemID, int billID, String itemType, String description,
                                    float amount, int quantity, double total, Date created_at, Date updated_at){
        String sql = "insert into BillItem( item_id, bill_id, item_type, description, amount, quantity, total, created_at, updated_at)" +
                "values ( ? , ? , ? , ? , ? , ? , ? , getdate(), getdate()";
        try{
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, itemID);
            stmt.setInt(2, billID);
            stmt.setNString(3, itemType);
            stmt.setNString(4, description);
            stmt.setFloat(5, amount);
            stmt.setInt(6, quantity);
            stmt.setDouble(7, total);
            int excuted = stmt.executeUpdate();
            if(excuted > 0) System.out.println("Add a Bill Item successfully!");
            BillItemList.add(new BillItems(itemID,billID,itemType,description,amount, quantity,total ,Date.valueOf(LocalDate.now()),Date.valueOf(LocalDate.now())));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // xóa staff bằng ID
    public static void deleteBillItemById(int itemID) throws SQLException {
        String deleteStaffSQL = "DELETE FROM BillItem WHERE item_id  = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt1 = connection.prepareStatement(deleteStaffSQL);
             Scanner sc = new Scanner(System.in)) {
            connection.setAutoCommit(false);
            System.out.println("Do you want to delete staff with ID " + itemID + "? (Yes/No)");
            String check = sc.nextLine().trim();
            if (!check.equalsIgnoreCase("Yes")) {
                System.out.println("Deletion cancelled.");
                return;
            }
            stmt1.setInt(1, itemID);
            int excuted = stmt1.executeUpdate();
            if (excuted > 0) {
                connection.commit(); // Chỉ commit nếu không có lỗi
                System.out.println("Delete A Bill Item Successfully!");
            } else {
                connection.rollback(); // Rollback nếu không có dòng nào bị xóa
                System.out.println("Staff Delete Failed!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (!DatabaseConnection.getConnection().getAutoCommit()) {
                    DatabaseConnection.getConnection().rollback(); // Rollback nếu có lỗi
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        }

    }
    // tìm staff bằng ID
    public static BillItems findStaffByID(int itemID) throws SQLException {
        if(BillItemList.isEmpty()) getAllBillItems();
        for(BillItems billItems  : BillItemList){
            if(billItems.getItemID() == itemID) return billItems;
        }
        return null;
    }
    // hàm này dùng để cập nhật một staff thông qua id, khi chưa biết trước nên cập nhật thông tin gì
    // -> dùng Object để kiểm tra đối tượng cần update
    public static void updateBillItems (int itemID, String field, Object newValue) throws SQLException {
        // các column có sẵn trong Resident để tránh sql injection
        List<String> allowColumn = Arrays.asList("item_id", "bill_id", "item_type","description", "amount", "quantity", "total");
        if(!allowColumn.contains(field.toLowerCase())){
            System.out.println("Field need update isn't invalid!");
            return;
        }
        String sql = "Update BillItem set " + field + " = ? where item_id = ?";
        try{
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            connection.setAutoCommit(false);
            if(newValue instanceof String){
                stmt.setString(1, (String)newValue);
            }
            else if(newValue instanceof Integer){
                stmt.setInt(1, (Integer) newValue);
            }
            else if(newValue instanceof Double){
                stmt.setDouble(1, (Double) newValue);
            }
            else{
                System.out.println("Field is invalid!");
            }
            stmt.setInt(2, itemID);
            int excuted = stmt.executeUpdate();
            if(excuted > 0) {
                System.out.println("Update successfully!");
                connection.commit();
            }
            else System.out.println("Update fail!");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
