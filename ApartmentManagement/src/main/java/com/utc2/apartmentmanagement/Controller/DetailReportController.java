package com.utc2.apartmentmanagement.Controller;

import com.utc2.apartmentmanagement.DAO.PaymentDAO;
import com.utc2.apartmentmanagement.Model.Apartment;
import com.utc2.apartmentmanagement.Model.Payment;
import com.utc2.apartmentmanagement.Model.Report;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lombok.Setter;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

public class DetailReportController {
    @FXML public Label totalInvoicesLabel;
    @FXML public Label paidInvoicesLabel;
    @FXML public Label paymentRateLabel;
    @FXML public Label totalCollectedLabel;
    @FXML public Label totalOutstandingLabel;
    @FXML public BarChart revenueChart;
    @FXML public Label reportTypeLabel;
    @FXML public Label fromDateLabel;
    @FXML public Label toDateLabel;
    @FXML public Button exportButton;

    @Setter
    private ReportViewController parentController = new ReportViewController();
    public void setApartment(Report report) throws IOException {
        if (report != null) {
            List<Payment> paymentList = new PaymentDAO().getAllPayment();
            Payment payment = new Payment();
            DecimalFormat formatter = new DecimalFormat("#,###");
            DecimalFormat df = new DecimalFormat("0.00");
            double rate = payment.paymentRateLabel(paymentList); // ví dụ trả về 83.3333

            reportTypeLabel.setText(report.getReportType());
            totalInvoicesLabel.setText(String.valueOf(paymentList.size()));
            paidInvoicesLabel.setText(String.valueOf(payment.completedPayment(paymentList)));
            totalCollectedLabel.setText(formatter.format(payment.totalAmountCollected(paymentList)) + " VND");
            totalOutstandingLabel.setText(formatter.format(payment.totalOutstandingLabel(paymentList))+ " VND");
            paymentRateLabel.setText(df.format(rate) + " %");

        }
    }
    public void exportReport(ActionEvent actionEvent) {

    }
}
