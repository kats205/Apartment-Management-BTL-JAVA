package org.example.apartmentmanagement.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.apartmentmanagement.DAO.UserDAO;

import java.io.IOException;
import java.util.prefs.Preferences;

public class LoginController {

    public Button registerBtn;
    @FXML
    private Button btnLogin;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUsername;

    @FXML
    private CheckBox rememberPassword;

    @FXML
    void handleLogin(ActionEvent event) {
        String userName =  txtUsername.getText();
        String password = txtPassword.getText();
        UserDAO userDAO = new UserDAO();
        int role_id = userDAO.login(userName, password);
        if(role_id == 1){
            System.out.println("Hello manager!");
        }
        else if (role_id == 2){
            System.out.println("Hello staff!");
        }
        else if(role_id == 3){
            System.out.println("hello resident!");
        }
        else{
            System.out.println("Error");
        }

        Preferences prefs = Preferences.userNodeForPackage(getClass());

        if(rememberPassword.isSelected()){
            prefs.put("username", userName);
            prefs.put("password", password);
            prefs.putBoolean("rememberMe", true);
        } else {
            prefs.remove("username");
            prefs.remove("password");
            prefs.putBoolean("rememberMe", false);
        }
    }
    private void showRegisterForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FormRegister.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Form Đăng Ký");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void registerAccountBtn() throws IOException {
        registerBtn.setOnAction(event -> {

                System.out.println("Checkbox đã được chọn! Mở form đăng ký...");
                showRegisterForm();

        });
//        if (registerCheckBox != null && registerCheckBox.isSelected()) {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FormRegister.fxml"));
//            Parent root = loader.load();
//            FormRegisterController controller = loader.getController();
//            controller.handleRegister();
//        }
    }
}
