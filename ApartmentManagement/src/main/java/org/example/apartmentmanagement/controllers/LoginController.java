package org.example.apartmentmanagement.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;

    public LoginController() {
    }

    @FXML
    private void handleLogin() {
        String username = this.usernameField.getText();
        String password = this.passwordField.getText();
        if (username.equals("admin") && password.equals("admin")) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Invalid username or password!");
        }

    }

    @FXML
    private void handleRegister() {
        System.out.println("Redirecting to registration...");
    }
}