package com.utc2.apartmentmanagement.Controller;

import com.utc2.apartmentmanagement.DAO.RoleDAO;
import com.utc2.apartmentmanagement.DAO.UserDAO;
import com.utc2.apartmentmanagement.Model.Role;
import com.utc2.apartmentmanagement.Model.Session;
import com.utc2.apartmentmanagement.Utils.AlertBox;
import com.utc2.apartmentmanagement.Views.Main;
import com.utc2.apartmentmanagement.Views.ResidentView;
import com.utc2.apartmentmanagement.Views.StaffView;
import com.utc2.apartmentmanagement.Views.register;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoginController {

    public ImageView lockView;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField visibleField;
    @FXML
    private Pane imagePane;

    @FXML
    private ImageView imageView;

    @FXML
    private ImageView revealView;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private ImageView Exit;

    @FXML
    private Button registerButton;

    @FXML
    public void initialize() {
        Exit.setOnMouseClicked(event -> {
            System.exit(0);
        });
        // Đảm bảo rằng phần ảnh được bo góc bên phải
        setupImagePane();

        // Nhấn Enter ở passwordField thì login
        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                loginButton.fire(); // Gọi lại handleLogin
            }
        });
        visibleField.textProperty().bindBidirectional(passwordField.textProperty());


    }

    private void setupImagePane() {
        // Sử dụng SVGPath để tạo hình dạng với góc bo tròn ở bên phải
        SVGPath path = new SVGPath();
        path.setContent("M4 0H284C377.888 0 454 76.1116 454 170V430C454 523.888 377.888 600 284 600H4V0Z");

        // Áp dụng clip vào imagePane để tạo hiệu ứng bo góc bên phải
        if (imagePane != null) {
            imagePane.setClip(path);

            // Đảm bảo ảnh nằm hoàn toàn trong vùng bo góc
            if (imageView != null) {
                imageView.setFitWidth(450);
                imageView.setFitHeight(600);
                imageView.setPreserveRatio(false);

                // Đặt sự kiện thay đổi kích thước
                imagePane.widthProperty().addListener((obs, oldVal, newVal) -> {
                    imageView.setFitWidth(newVal.doubleValue());
                });

                imagePane.heightProperty().addListener((obs, oldVal, newVal) -> {
                    imageView.setFitHeight(newVal.doubleValue());
                });
            }
        }
    }

    private boolean isPasswordVisible = false;

    public void handleLogin(ActionEvent event) {
        String userName = usernameField.getText();
        String password = passwordField.getText();

        if (userName.isEmpty() && password.isEmpty()) {
            AlertBox.showAlertForExeptionRegister("Thông báo!", "Vui lòng nhập tên đăng nhập và mật khẩu!");
        } else if (userName.isEmpty()) {
            AlertBox.showAlertForExeptionRegister("Thông báo!", "Vui lòng nhập tên đăng nhập!");
        } else if (password.isEmpty()) {
            AlertBox.showAlertForExeptionRegister("Thông báo!", "Vui lòng nhập mật khẩu!");
        }

        UserDAO userDAO = new UserDAO();
        int role_id = userDAO.login(userName, password);

        // lấy thông tin người dùng vừa đăng nhập và thời gian hiện tại
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formatted = now.format(formatter);
        Session.setLastLogin(formatted);
        Session.setUserName(userName);
        Session.setPassWord(password);
        Role role = new RoleDAO().getRoleById(role_id);
        if(role == null){
            AlertBox.showAlertForExeptionRegister("Thông báo!", "Tên đăng nhập hoặc mật khẩu không chính xác!");
            return;
        }
        String roleName = role.getRoleName();
        System.out.println("roleName " + roleName);
        if(roleName != null){
        Session.setRoleName(new RoleDAO().getRoleById(role_id).getRoleName());
        }
        else{
            AlertBox.showAlertForExeptionRegister("Thông báo!", "Tên đăng nhập hoặc mật khẩu không chính xác!");
            return;
        }
        switch (role_id) {
            case 1 -> {
                try {
                    // Đóng cửa sổ hiện tại
                    ((Stage) usernameField.getScene().getWindow()).close();
                    // Khởi chạy dashboard
                    Main main = new Main();
                    Stage stage = new Stage();
                    main.start(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            case 2 -> {

                try {
                    // Chuyển sang màn hình staff
                    ((Stage) usernameField.getScene().getWindow()).close();
                    StaffView staffView = new StaffView();
                    Stage stage = new Stage();
                    staffView.start(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            case 3 -> {
                try {
                    // Chuyển sang màn hình cư dân
                    ((Stage) usernameField.getScene().getWindow()).close();
                    ResidentView residentView = new ResidentView();
                    Stage stage = new Stage();
                    residentView.start(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

        @FXML
        public void goToSignUp (ActionEvent event){
            try {
                ((Stage) usernameField.getScene().getWindow()).close();
                // Khởi chạy dashboard
                register registerUser = new register();
                Stage stage = new Stage();
                registerUser.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // hàm hiển thị và ẩn mật khẩu
        @FXML
        private void handleTogglePasswordVisibility () {
            isPasswordVisible = !isPasswordVisible;
            if (isPasswordVisible) {
                visibleField.setVisible(true);
                visibleField.setManaged(true);
                passwordField.setVisible(false);
                passwordField.setManaged(false);
//            lockView.setImage(new Image(getClass().getResource("@.../assets/Login_register/eyev3.png").toString())); // icon mắt đóng
                lockView.setVisible(false);
                revealView.setVisible(true);
            } else {
                passwordField.setVisible(true);
                passwordField.setManaged(true);
                visibleField.setVisible(false);
                visibleField.setManaged(false);
//            lockView.setImage(new Image(getClass().getResource("@.../assets/Login_register/revealEye.png").toString())); // icon mắt mở
                lockView.setVisible(true);
                revealView.setVisible(false);
            }
        }
    }