package com.utc2.apartmentmanagement.Repository.Billing;

import com.utc2.apartmentmanagement.Model.Billing.BillItems;

import java.util.List;

public interface IBillItemDAO {
    List<BillItems> getAllBillItems();
    boolean addBillItem(BillItems billItems);
    boolean updateBillItemType(int billID, String newType);
    boolean updateBillItemDescription(int billID, String newDescription);
    boolean updateBillItemAmount(int billID, Float newAmount);
    boolean updateBillitemQuantity(int billID, int newQuantity);

}
