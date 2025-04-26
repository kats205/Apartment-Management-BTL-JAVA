package org.example.apartmentmanagement.Views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class PaymentForManager extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/PaymentForManager.fxml")));
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Assets/apartment.png"))));
        primaryStage.setTitle("Apartment Application");
        primaryStage.setScene(new Scene(root, 1080, 600));
        primaryStage.setResizable(true);
        primaryStage.show();

    }
//    public BorderPane getView(){
//        launch();
//    }
//    public static void main(String[] args) {
//        launch(args);
//    }
}
