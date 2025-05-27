package com.utc2.apartmentmanagement.Controller.User;

import javafx.fxml.FXML;
import javafx.scene.control.*;
        import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.StringConverter;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;

public class RegisterServicesViewController {
    @FXML
    private Label headerLabel;

    @FXML
    private ImageView serviceImage;

    @FXML
    private Label serviceNameLabel;

    @FXML
    private Label servicePriceLabel;

    @FXML
    private Label serviceDescriptionLabel;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private ComboBox<Integer> durationComboBox;

    @FXML
    private Label totalCostLabel;

    private double monthlyCost = 0;

    // Khởi tạo controller
    @FXML
    public void initialize() {
        setupDatePicker();
        setupDurationComboBox();
    }

    // Thiết lập DatePicker
    private void setupDatePicker() {
        // Mặc định là ngày hôm nay
        startDatePicker.setValue(LocalDate.now());

        // Không cho phép chọn ngày trong quá khứ
        startDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(LocalDate.now()) < 0);
            }
        });

        // Cập nhật tổng chi phí khi ngày bắt đầu thay đổi
        startDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateTotalCost();
        });
    }

    // Thiết lập ComboBox chọn thời hạn
    private void setupDurationComboBox() {
        // Thêm các tùy chọn thời hạn (số tháng)
        durationComboBox.getItems().addAll(1, 3, 6, 12);

        // Mặc định là 6 tháng
        durationComboBox.setValue(6);

        // Cập nhật tổng chi phí khi thời hạn thay đổi
        durationComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateTotalCost();
        });
    }

    // Thiết lập thông tin dịch vụ
    public void setServiceInfo(String name, String price, String description, Image image) {
        serviceNameLabel.setText(name);
        servicePriceLabel.setText(price);
        serviceDescriptionLabel.setText(description);

        if (image != null) {
            serviceImage.setImage(image);
        }

        // Lấy giá dịch vụ từ chuỗi (ví dụ: "250,000 VND / month" -> 250000)
        parseServicePrice(price);

        // Cập nhật tổng chi phí
        updateTotalCost();
    }

    // Phân tích chuỗi giá dịch vụ để lấy giá trị số
    private void parseServicePrice(String priceString) {
        try {
            // Tách phần số từ chuỗi giá
            String numberPart = priceString.split("VND")[0].trim();
            numberPart = numberPart.replace(",", "");

            monthlyCost = Double.parseDouble(numberPart);
        } catch (Exception e) {
            System.err.println("Không thể phân tích giá dịch vụ: " + priceString);
            monthlyCost = 0;
        }
    }

    // Cập nhật tổng chi phí dựa trên thời hạn đã chọn
    private void updateTotalCost() {
        int duration = durationComboBox.getValue() != null ? durationComboBox.getValue() : 1;
        double total = monthlyCost * duration;

        // Định dạng số với dấu phân cách hàng nghìn
        NumberFormat format = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedTotal = format.format(total) + " VND";

        totalCostLabel.setText(formattedTotal);
    }

    // Lấy ngày bắt đầu dịch vụ
    public LocalDate getStartDate() {
        return startDatePicker.getValue();
    }

    // Lấy thời hạn dịch vụ (số tháng)
    public int getDurationMonths() {
        return durationComboBox.getValue();
    }

    // Lấy tổng chi phí
    public double getTotalCost() {
        return monthlyCost * getDurationMonths();
    }

    // Tính ngày kết thúc dựa trên ngày bắt đầu và thời hạn
    public LocalDate getEndDate() {
        LocalDate startDate = getStartDate();
        return startDate.plusMonths(getDurationMonths());
    }
}