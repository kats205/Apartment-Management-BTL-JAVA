package com.utc2.apartmentmanagement.Model;

public class VnPay {
    private int id;
    private int payment_id;
    private String vnp_txn_ref;
    private String vnp_transaction_no;
    private String vnp_bank_tran_no;
    private String vnp_bank_code;
    private String vnp_card_type;
    private String vnp_order_info;
    private String vnp_pay_date;
    private String vnp_response_code;
    private String vnp_transaction_status;

    public VnPay() {}

    public VnPay(int id, int payment_id, String vnp_txn_ref, String vnp_transaction_no, String vnp_bank_tran_no, String vnp_bank_code, String vnp_card_type, String vnp_order_info, String vnp_pay_date, String vnp_response_code, String vnp_transaction_status) {
        this.id = id;
        this.payment_id = payment_id;
        this.vnp_txn_ref = vnp_txn_ref;
        this.vnp_transaction_no = vnp_transaction_no;
        this.vnp_bank_tran_no = vnp_bank_tran_no;
        this.vnp_bank_code = vnp_bank_code;
        this.vnp_card_type = vnp_card_type;
        this.vnp_order_info = vnp_order_info;
        this.vnp_pay_date = vnp_pay_date;
        this.vnp_response_code = vnp_response_code;
        this.vnp_transaction_status = vnp_transaction_status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(int payment_id) {
        this.payment_id = payment_id;
    }

    public String getVnp_txn_ref() {
        return vnp_txn_ref;
    }

    public void setVnp_txn_ref(String vnp_txn_ref) {
        this.vnp_txn_ref = vnp_txn_ref;
    }

    public String getVnp_transaction_no() {
        return vnp_transaction_no;
    }

    public void setVnp_transaction_no(String vnp_transaction_no) {
        this.vnp_transaction_no = vnp_transaction_no;
    }

    public String getVnp_bank_tran_no() {
        return vnp_bank_tran_no;
    }

    public void setVnp_bank_tran_no(String vnp_bank_tran_no) {
        this.vnp_bank_tran_no = vnp_bank_tran_no;
    }

    public String getVnp_bank_code() {
        return vnp_bank_code;
    }

    public void setVnp_bank_code(String vnp_bank_code) {
        this.vnp_bank_code = vnp_bank_code;
    }

    public String getVnp_card_type() {
        return vnp_card_type;
    }

    public void setVnp_card_type(String vnp_card_type) {
        this.vnp_card_type = vnp_card_type;
    }

    public String getVnp_order_info() {
        return vnp_order_info;
    }

    public void setVnp_order_info(String vnp_order_info) {
        this.vnp_order_info = vnp_order_info;
    }

    public String getVnp_pay_date() {
        return vnp_pay_date;
    }

    public void setVnp_pay_date(String vnp_pay_date) {
        this.vnp_pay_date = vnp_pay_date;
    }

    public String getVnp_response_code() {
        return vnp_response_code;
    }

    public void setVnp_response_code(String vnp_response_code) {
        this.vnp_response_code = vnp_response_code;
    }

    public String getVnp_transaction_status() {
        return vnp_transaction_status;
    }

    public void setVnp_transaction_status(String vnp_transaction_status) {
        this.vnp_transaction_status = vnp_transaction_status;
    }
}
