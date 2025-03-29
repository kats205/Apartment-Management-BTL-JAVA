package org.example.apartmentmanagement.views;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class login extends Application {
    public login() {
    }

    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/org/example/apartmentmanagement/fxml/login-view.fxml"));
        Scene scene = new Scene((Parent)fxmlLoader.load());
        primaryStage.setTitle("Apartment Management - Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}