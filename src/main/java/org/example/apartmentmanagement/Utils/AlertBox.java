package org.example.apartmentmanagement.Utils;

import javafx.scene.control.Alert;

public class AlertBox {
    public static void showAlertForResident(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setContentText("Chào mừng cư dân!");
        alert.setHeaderText(null);
        alert.showAndWait();
    }
    public static void showAlertForManager(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setContentText("Chào mừng quản lý!");
        alert.setHeaderText(null);
        alert.showAndWait();
    }
    public static void showAlertForStaff(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setContentText("Chào mừng nhân viên!");
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
