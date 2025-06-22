package com.utc2.apartmentmanagement.Controller.Report;

import com.utc2.apartmentmanagement.DAO.Billing.PaymentDAO;
import com.utc2.apartmentmanagement.Model.Billing.Payment;
import com.utc2.apartmentmanagement.Model.Report.Report;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
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
    @FXML public BarChart<String, Number> revenueChart;
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
            double rate = payment.paymentRateLabel(paymentList);

            // Cập nhật các label
            reportTypeLabel.setText(report.getReportType());
            totalInvoicesLabel.setText(String.valueOf(paymentList.size()));
            paidInvoicesLabel.setText(String.valueOf(payment.completedPayment(paymentList)));
            totalCollectedLabel.setText(formatter.format(payment.totalAmountCollected(paymentList)) + " VND");
            totalOutstandingLabel.setText(formatter.format(payment.totalOutstandingLabel(paymentList))+ " VND");
            paymentRateLabel.setText(df.format(rate) + " %");
            fromDateLabel.setText(payment.formatDate(payment.minDate(paymentList)));
            toDateLabel.setText(payment.formatDate(payment.maxDate(paymentList)));

            // Cập nhật biểu đồ
            setupSimpleBarChart(paymentList);
        }
    }

    // Biểu đồ so sánh đã thu vs chưa thu tổng quát
    private void setupSimpleBarChart(List<Payment> paymentList) {
        revenueChart.getData().clear();

        // Tạo 2 series riêng biệt để có thể set màu khác nhau
        XYChart.Series<String, Number> collectedSeries = new XYChart.Series<>();
        collectedSeries.setName("Đã thu");
        XYChart.Series<String, Number> outstandingSeries = new XYChart.Series<>();
        outstandingSeries.setName("Chưa thu");

        Payment payment = new Payment();
        double totalCollected = payment.totalAmountCollected(paymentList);
        double totalOutstanding = payment.totalOutstandingLabel(paymentList);

        // Thêm dữ liệu vào từng series
        collectedSeries.getData().add(new XYChart.Data<>("Đã thu", totalCollected));
        outstandingSeries.getData().add(new XYChart.Data<>("Chưa thu", totalOutstanding));

        // Thêm cả 2 series vào chart
        revenueChart.getData().addAll(collectedSeries, outstandingSeries);
        revenueChart.setTitle("Tổng quan thu chi");
        revenueChart.setCategoryGap(20);

    }

    public void exportReport(ActionEvent actionEvent) {
        // TODO: Implement export functionality
    }
}