package org.example.apartmentmanagement.Repository;

import org.example.apartmentmanagement.Model.BillItems;

import java.util.List;

public interface IBillItemDAO {
    List<BillItems> getAllBillItems();
    boolean addBillItem(BillItems billItems);
    boolean updateBillItemType(int billID, String newType);
    boolean updateBillItemDescription(int billID, String newDescription);
    boolean updateBillItemAmount(int billID, Float newAmount);
    boolean updateBillitemQuantity(int billID, int newQuantity);

}
