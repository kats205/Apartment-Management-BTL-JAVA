package com.utc2.apartmentmanagement.Controller;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StaffDashboardController implements Initializable {

    @FXML public ImageView Exit;
    @FXML public AnchorPane rootPane;
    double x =0, y = 0;
    public void loadMyProfile(ActionEvent actionEvent) {
        try {
            URL url = getClass().getResource("/com/utc2/apartmentmanagement/fxml/MyProfileView.fxml");
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();

            // Gán controller
            MyProfileController controller = loader.getController();
            controller.setParentStaffDashBoard(this);
            controller.setDashboardStage((Stage) rootPane.getScene().getWindow()); // Stage của dashboard!

            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);

            // Kéo cửa sổ
            root.setOnMousePressed(event -> {
                x = event.getSceneX();
                y = event.getSceneY();
            });

            root.setOnMouseDragged(event -> {
                stage.setX(event.getScreenX() - x);
                stage.setY(event.getScreenY() - y);
            });
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), root);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Exit.setOnMouseClicked(e ->
                System.exit(0));
    }
}
