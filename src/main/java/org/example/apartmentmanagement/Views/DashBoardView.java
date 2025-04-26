<<<<<<<< HEAD:src/main/java/org/example/apartmentmanagement/Views/DashBoardView.java
package org.example.apartmentmanagement.Views;
========
package com.utc2.apartmentmanagement.Views;
>>>>>>>> a5f520b04b21230ce91e60cd577ab57a33bbf51d:ApartmentManagement/src/main/java/com/utc2/apartmentmanagement/Views/Main.java

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class DashBoardView extends Application {
    double x,y = 0;
    @Override
    public void start(Stage primaryStage) throws Exception{
<<<<<<<< HEAD:src/main/java/org/example/apartmentmanagement/Views/DashBoardView.java
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/DashBoard-view.fxml")));
========
        Parent root = FXMLLoader.load(getClass().getResource("/com/utc2/apartmentmanagement/fxml/DashBoard-view.fxml"));
>>>>>>>> a5f520b04b21230ce91e60cd577ab57a33bbf51d:ApartmentManagement/src/main/java/com/utc2/apartmentmanagement/Views/Main.java
        primaryStage.initStyle(StageStyle.UNDECORATED);

        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - x);
            primaryStage.setY(event.getScreenY() - y);
        });

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

}
