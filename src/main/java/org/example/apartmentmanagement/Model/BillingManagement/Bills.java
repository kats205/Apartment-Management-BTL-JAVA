package org.example.apartmentmanagement.Model.BillingManagement;

import java.sql.Date;
import java.util.ArrayList;

public class Bills {
    private int billID;
    private String apartmentID;
    private Date billingDate;
    private Date dueDate;
    private double totalAmount;
    private String status;
    private List<BillItem> items;

    public Bills() {
        billID = 0;
        apartmentID = status = "";
        billingDate = dueDate = null;
        totalAmount = 0;
        items = new ArrayList<BillItem>();

    }

    public Bills(int billID, String apartmentID, Date billingDate, Date dueDate, double totalAmount, String status, List<BillItem> items) {
        this.billID = billID;
        this.apartmentID = apartmentID;
        this.billingDate = billingDate;
        this.dueDate = dueDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.items = items;
    }

    public int getBillID() {
        return billID;
    }

    public void setBillID(int billID) {
        this.billID = billID;
    }

    public String getApartmentID() {
        return apartmentID;
    }

    public void setApartmentID(String apartmentID) {
        this.apartmentID = apartmentID;
    }

    public Date getBillingDate() {
        return billingDate;
    }

    public void setBillingDate(Date billingDate) {
        this.billingDate = billingDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<BillItem> getItems() {
        return items;
    }

    public void setItems(List<BillItem> items) {
        this.items = items;
    }

    public double calculateTotalAmount(){

    }
    public File generatePDF(){

    }
    public void addItem(BillItem item){

    }
    public boolean isPaid(){

    }
}
