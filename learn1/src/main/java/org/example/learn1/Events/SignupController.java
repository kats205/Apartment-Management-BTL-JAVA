package org.example.learn1.Events;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.IOException;

public class SignupController {
    @FXML
    private TextField fullNameField, emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    public void handleSignup(ActionEvent event) {
        String fullName = fullNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Signup Failed", "All fields are required.");
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Signup Successful", "Account created successfully!");
        }
    }

    @FXML
    public void goToLogin(ActionEvent event) throws IOException {
        Stage stage = (Stage) emailField.getScene().getWindow();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/org/example/learn1/Views/login.fxml")));
        stage.setScene(scene);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
