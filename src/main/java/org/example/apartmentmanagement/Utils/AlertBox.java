package org.example.apartmentmanagement.Utils;

import javafx.scene.control.Alert;

public class AlertBox {
    public static void showAlertForUser(String title,String text){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(text);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
    public static void showAlertForExeptionRegister(String title,String text){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(text);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

}
