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

public class LoginController {
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    public void handleLogin(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if ("admin@example.com".equals(email) && "1234".equals(password)) {
            showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome back!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid email or password.");
        }
    }

    @FXML
    public void goToSignUp(ActionEvent event) throws IOException {
        Stage stage = (Stage) emailField.getScene().getWindow();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/org/example/learn1/Views/signup.fxml")));
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
