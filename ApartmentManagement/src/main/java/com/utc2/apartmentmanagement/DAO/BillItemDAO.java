package com.utc2.apartmentmanagement.DAO;

import com.utc2.apartmentmanagement.Model.BillItems;
import com.utc2.apartmentmanagement.Repository.IBillItemDAO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class BillItemDAO implements IBillItemDAO {
    @Override
    public List<BillItems> getAllBillItems() {
        String sql = "SELECT * FROM BillItem";
        List<BillItems> billItemsList = new ArrayList<>();
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){
            while(rs.next()){
                billItemsList.add(new BillItems(rs.getInt("item_id"),
                        rs.getInt("bill_id"),
                        rs.getString("item_type"),
                        rs.getNString("description"),
                        rs.getFloat("amount"),
                        rs.getInt("quantity"),
                        rs.getDouble("total"),
                        rs.getDate("created_at"),
                        rs.getDate("updated_at")));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return billItemsList;
    }

    @Override
    public boolean addBillItem(BillItems billItems) {
        String sql = "INSERT INTO BillItem(item_id, bill_id, item_type, description, amount, quantity, total, created_at, updated_at)" +
                "VALUES ( ? , ? , ? , ? , ? , ? , ? , getdate(), getdate())";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1,billItems.getItemID());
            stmt.setInt(2, billItems.getBillID());
            stmt.setString(3, billItems.getItemType());
            stmt.setNString(4,billItems.getDescription());
            stmt.setDouble(5, billItems.getAmount());
            stmt.setDouble(6, billItems.getQuantity());
            stmt.setDouble(7, billItems.getTotal());
            return stmt.executeUpdate() > 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateBillItemType(int billID, String newType) {
        return false;
    }

    @Override
    public boolean updateBillItemDescription(int billID, String newDescription) {
        return false;
    }

    @Override
    public boolean updateBillItemAmount(int billID, Float newAmount) {
        return false;
    }

    @Override
    public boolean updateBillitemQuantity(int billID, int newQuantity) {
        return false;
    }

    public static boolean updateBillItemField(int billID, String field, Object newValue){
        String sql = "UPDATE BillItem SET " + field + " = ? , updated_at = ? WHERE billID = ?";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){
            if(newValue instanceof String){
                stmt.setNString(1, (String) newValue);
            }
            else if(newValue instanceof Double){
                stmt.setDouble(1, (Double)newValue);
            }
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
            stmt.setInt(3, billID);
            return stmt.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    public static void showBillItemList(){
        List<BillItems> BillItemsList = new BillItemDAO().getAllBillItems();
        if(BillItemsList.isEmpty()) return;
        System.out.println("===========================BillItem List===========================");
        for(BillItems billItems : BillItemsList){
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
}