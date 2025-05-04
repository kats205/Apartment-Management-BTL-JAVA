package com.utc2.apartmentmanagement.Controller;

import com.utc2.apartmentmanagement.DAO.ApartmentDAO;
import com.utc2.apartmentmanagement.DAO.ResidentDAO;
import com.utc2.apartmentmanagement.DAO.UserDAO;
import com.utc2.apartmentmanagement.Model.Session;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import lombok.Setter;

public class UserDashboardController implements Initializable {
    @FXML public Label apartmentIdTf;
    @FXML public Label buildingTF;
    @FXML public Label areaTF;
    @FXML public Label floorTF;
    @FXML private AnchorPane rootPane;
    @FXML private ImageView Exit;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Exit.setOnMouseClicked(event -> {
            System.exit(0);
        });
    }
    double x =0, y = 0;
    @FXML
    public void loadMyProfile(ActionEvent actionEvent) {

        try {
            URL url = getClass().getResource("/com/utc2/apartmentmanagement/fxml/MyProfileView.fxml");
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();

            // Gán controller
            MyProfileController controller = loader.getController();
            controller.setParentUserDashBoard(this);
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
}
