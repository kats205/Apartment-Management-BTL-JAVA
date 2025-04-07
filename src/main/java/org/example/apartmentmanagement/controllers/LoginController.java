package org.example.apartmentmanagement.controllers;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.SVGPath;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.apartmentmanagement.DAO.UserDAO;
import org.example.apartmentmanagement.Model.User;
import org.example.apartmentmanagement.Utils.AlertBox;
import org.example.apartmentmanagement.Utils.StringUtils;
import org.example.apartmentmanagement.Utils.TryCatchUtil;
import org.example.apartmentmanagement.Views.DashBoardView;

import java.util.List;

import static javafx.application.Application.launch;
import static org.example.apartmentmanagement.Utils.AlertBox.showAlertForExeptionRegister;

public class LoginController {
    @FXML
    public ImageView userView;
    @FXML
    public CheckBox checkBoxTerm;
    @FXML
    public PasswordField registerReEnterPW;
    @FXML
    public PasswordField registerPassword;
    @FXML
    public TextField registerEmail;
    @FXML
    public TextField registerFullName;
    @FXML
    public TextField registerPhoneNumber;
    @FXML
    public TextField registerUsername;
    @FXML
    public Button backToLogin;
    @FXML
    public AnchorPane registerPane;
    @FXML
    public ImageView lockView;
    public TextField showPasswordField;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Pane imagePane;
    @FXML
    private ImageView imageView;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;
    @FXML
    public void initialize() {
        // Đảm bảo rằng phần ảnh được bo góc bên phải
        setupImagePane();
    }
    @FXML
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

    @FXML
    public void handleLogin(ActionEvent event) {
        String userName = usernameField.getText();
        String password = passwordField.getText();
        UserDAO userDAO = new UserDAO();
        int role_id = userDAO.login(userName, password);
        switch (role_id) {
            case 1 -> {
                try {
                    // Đóng cửa sổ hiện tại
                    ((Stage) usernameField.getScene().getWindow()).close();
                    // Khởi chạy dashboard
                    DashBoardView dashBoard = new DashBoardView();
                    Stage stage = new Stage();
                    dashBoard.start(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            case 2 -> AlertBox.showAlertForUser("Thông báo","Chào mừng nhân viên!");
            case 3 -> AlertBox.showAlertForUser("Thông báo","Chào mừng cư dân!");
            default -> System.out.println("Invalid username or password!");
        }
    }

    @FXML
    public void goToSignUp(ActionEvent event) {
        System.out.println("Checkbox đã được chọn! Mở form đăng ký...");
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.millis(500));
        slide.setNode(registerPane);

        slide.setToX(-550); // Đưa Register Pane ra ngoài
        slide.play();

//        slide.setOnFinished((e) -> loginPane.setVisible(true)); // Hiện Login Pane lại

        // Khi hiệu ứng hoàn thành, hiện lại loginPane
//        slide.setOnFinished(event -> loginPane.setVisible(true));
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/signup.fxml"));
//            AnchorPane signupPane = loader.load();
//            Scene scene = new Scene(signupPane);
//            Stage stage = (Stage) rootPane.getScene().getWindow();
//            stage.setScene(scene);
//            stage.show();
//        } catch (IOException e) {
//            System.err.println("Lỗi khi chuyển sang màn hình đăng ký: " + e.getMessage());
//            e.printStackTrace();
//        }
    }

    public void backToLogin(ActionEvent actionEvent) {
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.millis(500));
        slide.setNode(registerPane);

        slide.setToX(600); // Đẩy registerPane ra ngoài màn hình
        slide.play();

        // Khi hiệu ứng hoàn thành, hiện lại loginPane
        slide.setOnFinished(event -> rootPane.setVisible(true));
        registerUsername.clear();
        registerPassword.clear();
        registerReEnterPW.clear();
        registerPhoneNumber.clear();
        registerFullName.clear();
        registerEmail.clear();
        checkBoxTerm.setSelected(false);
    }
    @FXML
    public void handleRegister(ActionEvent actionEvent) {
        if(checkBoxTerm.isSelected()){
            String full_name = null;
            try {
                String user_name = registerUsername.getText();
                String pass_word = registerPassword.getText();
                String re_password = registerReEnterPW.getText();
                full_name = StringUtils.capitalizeName(registerFullName.getText());
                String phone_number = registerPhoneNumber.getText();
                String email = registerEmail.getText();
                // lấy ra mảng username để check tính unique
                List<String> userNameList = UserDAO.getAllValuesofColumn("username");
                //Lấy ra mảng email để check tính unique
                List<String> user = UserDAO.getAllValuesofColumn("email");
                if (!TryCatchUtil.validatePassword(pass_word)) {
                    showAlertForExeptionRegister("Thông báo!", "Mật khẩu phải từ 6-12 ký tự và có ít nhất một chữ hoa!");
                } else if (!TryCatchUtil.validateEmail(email)) {
                    showAlertForExeptionRegister("Thông báo!", "Không đúng định dạng email!");
                } else if (!TryCatchUtil.validatePhone(phone_number)) {
                    showAlertForExeptionRegister("Thông báo!", "Không đúng định dạng số điện thoại");
                } else if (userNameList.contains(user_name)) {
                    showAlertForExeptionRegister("Thông báo!", "Tên người dùng này đã tồn tại! Vui lòng nhập lại!");
                } else if (!pass_word.equals(re_password)) {
                    showAlertForExeptionRegister("Thông báo!", "Mật khẩu không khớp!");
                } else {
                    // mặc định là đăng ký tài khoản cho cư dan nên để role id = 3
                    if(new UserDAO().addUser(new User(user_name, pass_word, full_name, email, phone_number, 1, true))){
                        showAlertForExeptionRegister("Thông báo!", "Đăng ký tài khoản thành công!");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                showAlertForExeptionRegister("Cảnh báo!", "Đã xảy ra lỗi! Vui lòng thử lại");
            }
        }
        else{
            showAlertForExeptionRegister("Thông báo!", "Vui lòng đồng ý với các điều khoản và điều kiện trước khi đăng ký!");
        }
    }

    @FXML
    public void ShowPassWord(MouseEvent mouseEvent) {

    }
}