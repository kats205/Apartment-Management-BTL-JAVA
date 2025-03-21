package org.example.apartmentmanagement.Model.BillingManagement;

public class BillItems {
    private int itemID;
    private int billID;
    private String itemType;
    private String description;
    private double amount;
    private double quantity;
    private double total;

    public BillItems() {
        itemID = billID = 0;
        itemType = description = "";
        amount = quantity = total = 0;
    }

    public BillItems(int itemID, int billID, String itemType, String description, double amount, double quantity, double total) {
        this.itemID = itemID;
        this.billID = billID;
        this.itemType = itemType;
        this.description = description;
        this.amount = amount;
        this.quantity = quantity;
        this.total = total;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public int getBillID() {
        return billID;
    }

    public void setBillID(int billID) {
        this.billID = billID;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
    public double calculateTotal(){

    }
}
