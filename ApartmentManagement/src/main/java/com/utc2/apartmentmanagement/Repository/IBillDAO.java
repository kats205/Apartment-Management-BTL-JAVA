package com.utc2.apartmentmanagement.Repository;

import com.utc2.apartmentmanagement.Model.Bills;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IBillDAO{
    public List<Bills> getAllbills();
    public boolean addBill(Bills bill);
    public Bills getBillByID(int billID);
    public boolean updateApartmentID(int billID, String newAparmentID);
    public boolean updateBillingDate(int billID, Date newBillDate);
    public boolean updateDueDateBill(int billID, Date newDueDate);
    public boolean updateStatusBill(int billID, String newStatus);
    public boolean deleteBillByID(int billID) throws SQLException;
    List<Map<String, Object>> getBillByApartmentId(String apartmentId) throws SQLException;
}
