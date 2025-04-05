package org.example.apartmentmanagement.DAOLayer;

import org.example.apartmentmanagement.Repository.iBillItem;
import org.example.apartmentmanagement.models.BillItems;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BIllItemDAO implements iBillItem {


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
        return billItemsList;    }

    @Override
    public boolean addBillItem(BillItems billItems) {
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
}
