<<<<<<<< HEAD:src/main/java/org/example/apartmentmanagement/Views/login.java
package org.example.apartmentmanagement.Views;
========
package com.utc2.apartmentmanagement.Views;
>>>>>>>> a5f520b04b21230ce91e60cd577ab57a33bbf51d:ApartmentManagement/src/main/java/com/utc2/apartmentmanagement/Views/login.java

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class login extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
<<<<<<<< HEAD:src/main/java/org/example/apartmentmanagement/Views/login.java
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/login-view.fxml")));
========
        Parent root = FXMLLoader.load(getClass().getResource("/com/utc2/apartmentmanagement/fxml/login-view.fxml"));
>>>>>>>> a5f520b04b21230ce91e60cd577ab57a33bbf51d:ApartmentManagement/src/main/java/com/utc2/apartmentmanagement/Views/login.java
        primaryStage.setTitle("Apartment Application");
        primaryStage.setScene(new Scene(root, 1080, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

