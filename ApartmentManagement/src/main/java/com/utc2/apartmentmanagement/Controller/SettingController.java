package com.utc2.apartmentmanagement.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

import java.net.URL;
import java.util.ResourceBundle;


public class SettingController implements Initializable {
    @FXML
    public Button themeButton;
    public Button saveSettingsButton;
    public Button resetButton;
    public Button closeButton;
    public Button notificationSettingsButton;
    public Button restoreButton;
    public Button backupButton;
    public TableColumn userActionsColumn;
    public TableColumn statusColumn;
    public TableColumn roleColumn;
    public TableColumn phoneColumn;
    public TableColumn emailColumn;
    public TableColumn nameColumn;
    public TableColumn usernameColumn;
    public TableColumn userIdColumn;
    public TableView userTable;
    public Button addUserButton;
    public Button searchUserButton;
    public TextField searchUserField;
    @FXML
    public AnchorPane SettingView;
    private boolean darkMode = false;


    public void handleThemeButton() {
        Region root = (Region) themeButton.getScene().getRoot();

        if (darkMode) {
            // Chuyển về Light Mode
            root.setStyle("-fx-background-color: white; -fx-text-fill: black;");

        } else {
            // Chuyển sang Dark Mode
            root.setStyle("-fx-background-color: #2b2b2b; -fx-text-fill: white;");
//            searchUserField.setStyle("-fx-text-fill: white;");
        }

        darkMode = !darkMode;
    }

    public void handleButtonClose(ActionEvent actionEvent) {
        SettingView.setVisible(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
