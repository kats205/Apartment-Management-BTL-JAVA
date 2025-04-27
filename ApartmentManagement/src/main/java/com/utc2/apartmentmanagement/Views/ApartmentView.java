package com.utc2.apartmentmanagement.Views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ApartmentView extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/utc2/apartmentmanagement/fxml/ApartmentView.fxml"));
        primaryStage.setTitle("Apartment Application");
        primaryStage.setScene(new Scene(root, 1500, 800));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
