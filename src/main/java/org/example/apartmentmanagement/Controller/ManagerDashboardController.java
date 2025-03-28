package org.example.apartmentmanagement.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class ManagerDashboardController {
    @FXML
    private StackPane contentPane;

    public void showApartments() {
        contentPane.getChildren().setAll(new Label("🏠 Quản lý Căn hộ"));
    }

    public void showResidents() {
        contentPane.getChildren().setAll(new Label("👥 Quản lý Cư dân"));
    }

    public void showStaff() {
        contentPane.getChildren().setAll(new Label("👔 Quản lý Nhân viên"));
    }

    public void showServices() {
        contentPane.getChildren().setAll(new Label("🛠 Quản lý Dịch vụ"));
    }

    public void showMaintenanceFees() {
        contentPane.getChildren().setAll(new Label("💰 Quản lý Phí bảo trì"));
    }

    public void showContracts() {
        contentPane.getChildren().setAll(new Label("📜 Quản lý Hợp đồng"));
    }

    public void showReports() {
        contentPane.getChildren().setAll(new Label("📊 Báo cáo"));
    }

    public void logout() {
        System.out.println("Đăng xuất...");
        // Thêm code để quay lại màn hình đăng nhập
    }
}
