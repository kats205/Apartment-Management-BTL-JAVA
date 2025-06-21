package com.utc2.apartmentmanagement.Controller.User;

import com.utc2.apartmentmanagement.DAO.User.UserDAO;
import com.utc2.apartmentmanagement.Model.User.User;
import com.utc2.apartmentmanagement.Utils.StringUtils;
import com.utc2.apartmentmanagement.Utils.TryCatchUtil;
import com.utc2.apartmentmanagement.Views.login;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;

import java.util.List;

import static com.utc2.apartmentmanagement.Utils.AlertBox.showAlertForExeptionRegister;

public class RegisterController {

    @FXML
    public TextField registerUserName;
    @FXML
    public TextField registerFullname;
    @FXML
    public TextField registerEmail;
    @FXML
    public TextField registerPhoneNumber;
    @FXML
    public PasswordField registerPassWord;
    @FXML
    public PasswordField registerReEnterPW;
    // label warning cho từng field
    @FXML
    public Label warningUsername;
    @FXML
    public Label warningFullname;
    @FXML
    public Label warningEmail;
    @FXML
    public Label warningPhoneNumber;
    @FXML
    public Label warningPassword;
    @FXML
    public Label warningReEnterPW;
    @FXML
    public Button registerButton;
    @FXML
    private Pane imagePane;
    @FXML
    private ImageView imageView;
    @FXML
    private ImageView Exit;


    @FXML
    public void initialize() {
        Exit.setOnMouseClicked(event -> {
            System.exit(0);
        });
        // Đảm bảo rằng phần ảnh được bo góc bên phải
        setupImagePane();

        // Gán sự kiện click nút login
        registerButton.setOnAction(this::handleRegister);
        // Cho phép nhấn Enter ở bất kỳ input nào
        registerButton.setDefaultButton(true);
        // Nhấn Enter ở passwordField thì login
        registerReEnterPW.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                registerButton.fire(); // Gọi lại handleLogin
            }
        });
    }

    private void setupImagePane() {
        // Sử dụng SVGPath để tạo hình dạng với góc bo tròn ở bên phải
        SVGPath path = new SVGPath();
        path.setContent("M454 0H174C80.1116 0 4 76.1116 4 170V430C4 523.888 80.1116 600 174 600H454V0Z");

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
    public void BackLogin(ActionEvent actionEvent) throws Exception {
        try {
            ((Stage) registerReEnterPW.getScene().getWindow()).close();
            // Khởi chạy dashboard
            login loginUser = new login();
            Stage stage = new Stage();
            loginUser.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // hàm reset các label warning
    private void resetAllWarnings() {
        warningPassword.setVisible(false);
        warningEmail.setVisible(false);
        warningPhoneNumber.setVisible(false);
        warningUsername.setVisible(false);
        warningReEnterPW.setVisible(false);
        warningFullname.setVisible(false);

        registerPassWord.setStyle(null);
        registerEmail.setStyle(null);
        registerPhoneNumber.setStyle(null);
        registerUserName.setStyle(null);
        registerReEnterPW.setStyle(null);
        registerFullname.setStyle(null);
    }

    //hàm show warning
    private void showWarning(Label label, TextField field, String message) {
        label.setText(message);
        label.setStyle("-fx-text-fill: red;");
        label.setVisible(true);
        field.setStyle("-fx-border-color: red;");
    }

    private boolean validateForm(){
        boolean hasError = false;
        String user_name = registerUserName.getText();
        String pass_word = registerPassWord.getText();
        String re_password = registerReEnterPW.getText();
        String full_name = StringUtils.capitalizeName(registerFullname.getText());
        String phone_number = registerPhoneNumber.getText();
        String email = registerEmail.getText();
        // lấy ra mảng username để check tính unique
        List<String> userNameList = UserDAO.getAllValuesofColumn("username");
        //Lấy ra mảng email để check tính unique
        List<String> emailList = UserDAO.getAllValuesofColumn("email");
        // Bắt ngoại lệ cho từng field
        if (userNameList.contains(user_name)) {
            showWarning(warningUsername, registerUserName, "Tên người dùng này đã tồn tại! Vui lòng nhập lại!");
            hasError = true;
        }
        if(user_name.isEmpty()){
            showWarning(warningUsername, registerUserName, "Tên người dùng không được trống!");
            hasError = true;
        }
        if(!TryCatchUtil.validateFullName(full_name)){
            showWarning(warningFullname, registerFullname, "Họ tên không đúng định dạng!");
            hasError = true;
        }
        if (!TryCatchUtil.validatePassword(pass_word)) {
            showWarning(warningPassword, registerPassWord, "Mật khẩu phải từ 6 đến 12 ký tự và có ít nhất 1 chữ hoa!");
            hasError = true;
        }

        if (!TryCatchUtil.validateEmail(email)) {
            showWarning(warningEmail, registerEmail, "Email không đúng định dạng!");
            hasError = true;
        }

        if (!TryCatchUtil.validatePhone(phone_number)) {
            showWarning(warningPhoneNumber, registerPhoneNumber, "Số điện thoại không đúng định dạng!");
            hasError = true;
        }

        if (emailList.contains(email)) {
            showWarning(warningEmail, registerEmail, "Email này đã tồn tại! Vui lòng nhập lại!");
            hasError = true;
        }
        if(re_password.isEmpty()){
            showWarning(warningReEnterPW, registerReEnterPW, "Vui lòng nhập lại mật khẩu!");
            hasError = true;
        }
        if (!pass_word.equals(re_password)) {
            showWarning(warningReEnterPW, registerReEnterPW, "Mật khẩu không khớp!");
            hasError = true;
        }
        return hasError;
    }
    @FXML
    public void handleRegister(ActionEvent actionEvent) {
        String user_name = registerUserName.getText();
        String pass_word = registerPassWord.getText();
        String re_password = registerReEnterPW.getText();
        String full_name = StringUtils.capitalizeName(registerFullname.getText());
        String phone_number = registerPhoneNumber.getText();
        String email = registerEmail.getText();
        resetAllWarnings();
        try {
            // Nếu không có lỗi thì mới tiếp tục
            boolean hasError = validateForm();
            if (!hasError) {
                if (new UserDAO().addUser(new User(user_name, pass_word, full_name, email, phone_number, 2, true))) {
                    showAlertForExeptionRegister("Thông báo!", "Đăng ký tài khoản thành công!");
                    // Đóng form hiện tại
                    ((Stage) warningEmail.getScene().getWindow()).close();
                    // Mở lại trang đăng nhập
                    login loginView = new login();
                    Stage stage = new Stage();
                    loginView.start(stage);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlertForExeptionRegister("Cảnh báo!", "Đã xảy ra lỗi! Vui lòng thử lại");
        }
    }
}