package org.example.learn1;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.plaf.nimbus.State;

public class HelloApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        //Stage stage = new Stage();
        Group root = new Group();
        Scene scene = new Scene(root, 600, 600, Color.LIGHTSKYBLUE);
        Stage stage = new Stage();

//        Image icon = new Image(getClass().getResourceAsStream("/IMG_0042.JPG")); // ảnh phải nằm trong resources
//        state.getIcons().add(icon);
//        state.setTitle("Test");
//        state.setResizable(true);

        Text text = new Text();
        text.setText("WHOOOOOH");
        text.setX(100);
        text.setY(100);
        text.setFont(Font.font("Lato", 50));
        text.setFill(Color.BLACK);

        root.getChildren().add(text); // add text to group root
        stage.setScene(scene);
        stage.show();

    }
}