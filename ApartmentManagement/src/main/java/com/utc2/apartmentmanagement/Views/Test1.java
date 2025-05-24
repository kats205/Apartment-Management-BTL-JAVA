package com.utc2.apartmentmanagement.Views;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Test1 extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        TextField emailField = new TextField();
        Label emailError = new Label();
        emailError.setStyle("-fx-text-fill: red;");

        emailField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) {
                emailError.setText("Email không hợp lệ!");
                emailField.setStyle("-fx-border-color: red;");
            } else {
                emailError.setText("");
                emailField.setStyle("");
            }
        });

        VBox root = new VBox(10, emailField, emailError);
        root.setStyle("-fx-padding: 20;");
        Scene scene = new Scene(root, 300, 150);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
