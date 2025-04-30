package com.utc2.apartmentmanagement.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class DetailReportController implements Initializable {

    @FXML
    private Label reportTypeLabel;

    @FXML
    private Label fromDateLabel;

    @FXML
    private Label toDateLabel;

    @FXML
    private Button exportButton;

    @FXML
    private Label totalInvoicesLabel;

    @FXML
    private Label paidInvoicesLabel;

    @FXML
    private Label paymentRateLabel;

    @FXML
    private Label totalCollectedLabel;

    @FXML
    private Label totalOutstandingLabel;

    @FXML
    private BarChart<String, Number> revenueChart;

    // Định dạng số tiền VNĐ
    private final NumberFormat currencyFormatter = NumberFormat.getInstance(new Locale("vi", "VN"));

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Các giá trị mặc định sẽ được thiết lập từ backend

        // Định dạng số tiền
        currencyFormatter.setMaximumFractionDigits(0);
        currencyFormatter.setGroupingUsed(true);
    }

    /**
     * Cập nhật thông tin báo cáo
     * Method này sẽ được gọi từ backend để hiển thị dữ liệu
     */
    public void updateReportInfo(String reportType, String fromDate, String toDate,
                                 int totalInvoices, int paidInvoices, double paymentRate,
                                 double totalCollected, double totalOutstanding) {

        reportTypeLabel.setText(reportType);
        fromDateLabel.setText(fromDate);
        toDateLabel.setText(toDate);

        totalInvoicesLabel.setText(String.format("%,d", totalInvoices));
        paidInvoicesLabel.setText(String.format("%,d", paidInvoices));
        paymentRateLabel.setText(String.format("%.2f%%", paymentRate));
        totalCollectedLabel.setText(currencyFormatter.format(totalCollected));
        totalOutstandingLabel.setText(currencyFormatter.format(totalOutstanding));
    }

    /**
     * Cập nhật biểu đồ doanh thu
     * Method này sẽ được gọi từ backend để hiển thị dữ liệu
     */
    public void updateRevenueChart(String[] timeLabels, double[] collectedAmounts, double[] outstandingAmounts) {
        revenueChart.getData().clear();

        XYChart.Series<String, Number> collectedSeries = new XYChart.Series<>();
        collectedSeries.setName("Đã thu");

        XYChart.Series<String, Number> outstandingSeries = new XYChart.Series<>();
        outstandingSeries.setName("Chưa thu");

        for (int i = 0; i < timeLabels.length; i++) {
            collectedSeries.getData().add(new XYChart.Data<>(timeLabels[i], collectedAmounts[i]));
            outstandingSeries.getData().add(new XYChart.Data<>(timeLabels[i], outstandingAmounts[i]));
        }

        revenueChart.getData().addAll(collectedSeries, outstandingSeries);
    }

    @FXML
    private void exportReport(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Xuất báo cáo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"),
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );

        File file = fileChooser.showSaveDialog(exportButton.getScene().getWindow());

        if (file != null) {
            // Thực hiện xuất báo cáo theo định dạng file đã chọn
            // Backend sẽ xử lý phần này

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Đã xuất báo cáo thành công!");
            alert.showAndWait();
        }
    }
}
