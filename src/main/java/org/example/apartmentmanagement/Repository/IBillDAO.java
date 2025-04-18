package org.example.apartmentmanagement.Repository;

import org.example.apartmentmanagement.Model.Bills;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface IBillDAO{
    public List<Bills> getAllbills();
    public boolean addBill(Bills bill);
    public Bills getBillByID(int billID);
    public boolean updateApartmentID(int billID, String newAparmentID);
    public boolean updateBillingDate(int billID, Date newBillDate);
    public boolean updateDueDateBill(int billID, Date newDueDate);
    public boolean updateStatusBill(int billID, String newStatus);
    public boolean deleteBillByID(int billID) throws SQLException;
}
