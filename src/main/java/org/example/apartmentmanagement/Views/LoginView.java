package org.example.apartmentmanagement.Views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.Objects;

import static javafx.application.Application.launch;

public class LoginView extends Application  {
    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Assets/apartment.png"))));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/login.fxml"));
            AnchorPane root = loader.load();
            Scene scene = new Scene(root, 1280, 800);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Login Form");
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
