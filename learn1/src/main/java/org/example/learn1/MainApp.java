package org.example.learn1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/org/example/learn1/Views/login.fxml"))));
        primaryStage.setTitle("Sign In");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
