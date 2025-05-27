package com.utc2.apartmentmanagement.Controller;

import com.utc2.apartmentmanagement.DAO.UserDAO;
import com.utc2.apartmentmanagement.Model.User;
import com.utc2.apartmentmanagement.Utils.StringUtils;
import com.utc2.apartmentmanagement.Utils.TryCatchUtil;
import com.utc2.apartmentmanagement.Views.login;
import javafx.application.Platform;
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
import java.util.concurrent.CompletableFuture;

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

        setupRealTimeValidation();
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

    private void setupRealTimeValidation() {
        // Username validation
        registerUserName.textProperty().addListener((observable, oldValue, newValue) -> {
            validateUsernameRealTime(newValue);
        });

        // Password validation
        registerPassWord.textProperty().addListener((observable, oldValue, newValue) -> {
            validatePasswordRealTime(newValue);
            // Cũng validate confirm password khi password thay đổi
            if (!registerReEnterPW.getText().isEmpty()) {
                validateConfirmPasswordRealTime(registerReEnterPW.getText());
            }
        });

        // Confirm password validation
        registerReEnterPW.textProperty().addListener((observable, oldValue, newValue) -> {
            validateConfirmPasswordRealTime(newValue);
        });

        // Full name validation
        registerFullname.textProperty().addListener((observable, oldValue, newValue) -> {
            validateFullNameRealTime(newValue);
        });

        // Email validation
        registerEmail.textProperty().addListener((observable, oldValue, newValue) -> {
            validateEmailRealTime(newValue);
        });

        // Phone validation
        registerPhoneNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            validatePhoneRealTime(newValue);
        });
    }

    // 2. REAL-TIME USERNAME VALIDATION
    private void validateUsernameRealTime(String username) {
        // Reset style trước
        clearWarning(warningUsername, registerUserName);

        if (username == null || username.trim().isEmpty()) {
            showWarning(warningUsername, registerUserName, "Tên người dùng không được trống!");
            return;
        }

        if (username.length() <= 6) {
            showWarning(warningUsername, registerUserName, "Tên người dùng phải có ít nhất 7 ký tự!");
            return;
        }

        // Kiểm tra định dạng (chỉ chữ, số, underscore)
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            showWarning(warningUsername, registerUserName, "Tên người dùng chỉ được chứa chữ cái, số và dấu gạch dưới!");
            return;
        }

        // Kiểm tra trùng lặp (async để không block UI)
        checkUsernameExists(username);
    }

    // 3. KIỂM TRA USERNAME TỒN TẠI (ASYNC)
    private void checkUsernameExists(String username) {
        CompletableFuture.supplyAsync(() -> {
            try {
                List<String> userNameList = UserDAO.getAllValuesofColumn("username");
                return userNameList.contains(username);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }).thenAccept(exists -> {
            Platform.runLater(() -> {
                if (exists) {
                    showWarning(warningUsername, registerUserName, "Tên người dùng này đã tồn tại! Vui lòng nhập lại!");
                } else {
                    showSuccess(warningUsername, registerUserName, "Tên người dùng khả dụng!");
                }
            });
        });
    }

    // 4. REAL-TIME PASSWORD VALIDATION
    private void validatePasswordRealTime(String password) {
        clearWarning(warningPassword, registerPassWord);

        if (password == null || password.isEmpty()) {
            showWarning(warningPassword, registerPassWord, "Mật khẩu không được trống!");
            return;
        }

        if (!TryCatchUtil.validatePassword(password)) {
            showWarning(warningPassword, registerPassWord, "Mật khẩu phải từ 6 đến 12 ký tự và có ít nhất 1 chữ hoa!");
            return;
        }

        showSuccess(warningPassword, registerPassWord, "Mật khẩu hợp lệ!");
    }

    // 5. REAL-TIME CONFIRM PASSWORD VALIDATION
    private void validateConfirmPasswordRealTime(String confirmPassword) {
        clearWarning(warningReEnterPW, registerReEnterPW);

        if (confirmPassword == null || confirmPassword.isEmpty()) {
            showWarning(warningReEnterPW, registerReEnterPW, "Vui lòng nhập lại mật khẩu!");
            return;
        }

        String password = registerPassWord.getText();
        if (!password.equals(confirmPassword)) {
            showWarning(warningReEnterPW, registerReEnterPW, "Mật khẩu không khớp!");
            return;
        }

        showSuccess(warningReEnterPW, registerReEnterPW, "Mật khẩu khớp!");
    }

    // 6. REAL-TIME FULLNAME VALIDATION
    private void validateFullNameRealTime(String fullName) {
        clearWarning(warningFullname, registerFullname);

        if (fullName == null || fullName.trim().isEmpty()) {
            showWarning(warningFullname, registerFullname, "Họ tên không được trống!");
            return;
        }

        String capitalizedName = StringUtils.capitalizeName(fullName);
        if (!TryCatchUtil.validateFullName(capitalizedName)) {
            showWarning(warningFullname, registerFullname, "Họ tên không đúng định dạng!");
            return;
        }

        // Tự động format tên
        if (!fullName.equals(capitalizedName)) {
            Platform.runLater(() -> registerFullname.setText(capitalizedName));
        }

        showSuccess(warningFullname, registerFullname, "Họ tên hợp lệ!");
    }

    // 7. REAL-TIME EMAIL VALIDATION
    private void validateEmailRealTime(String email) {
        clearWarning(warningEmail, registerEmail);

        if (email == null || email.trim().isEmpty()) {
            showWarning(warningEmail, registerEmail, "Email không được trống!");
            return;
        }

        if (!TryCatchUtil.validateEmail(email)) {
            showWarning(warningEmail, registerEmail, "Email không đúng định dạng!");
            return;
        }

        // Kiểm tra trùng lặp email (async)
        checkEmailExists(email);
    }

    // 8. KIỂM TRA EMAIL TỒN TẠI (ASYNC)
    private void checkEmailExists(String email) {
        CompletableFuture.supplyAsync(() -> {
            try {
                List<String> emailList = UserDAO.getAllValuesofColumn("email");
                return emailList.contains(email);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }).thenAccept(exists -> {
            Platform.runLater(() -> {
                if (exists) {
                    showWarning(warningEmail, registerEmail, "Email này đã tồn tại! Vui lòng nhập lại!");
                } else {
                    showSuccess(warningEmail, registerEmail, "Email khả dụng!");
                }
            });
        });
    }

    // 9. REAL-TIME PHONE VALIDATION
    private void validatePhoneRealTime(String phoneNumber) {
        clearWarning(warningPhoneNumber, registerPhoneNumber);

        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            showWarning(warningPhoneNumber, registerPhoneNumber, "Số điện thoại không được trống!");
            return;
        }

        if (!TryCatchUtil.validatePhone(phoneNumber)) {
            showWarning(warningPhoneNumber, registerPhoneNumber, "Số điện thoại không đúng định dạng!");
            return;
        }

        showSuccess(warningPhoneNumber, registerPhoneNumber, "Số điện thoại hợp lệ!");
    }

    // 10. HELPER METHODS
    private void showWarning(Label warningLabel, TextField textField, String message) {
        warningLabel.setText(message);
        warningLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        warningLabel.setVisible(true);
        textField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
    }

    private void showSuccess(Label warningLabel, TextField textField, String message) {
        warningLabel.setText(message);
        warningLabel.setStyle("-fx-text-fill: green; -fx-font-size: 12px;");
        warningLabel.setVisible(true);
        textField.setStyle("-fx-border-color: green; -fx-border-width: 2px;");
    }

    private void clearWarning(Label warningLabel, TextField textField) {
        warningLabel.setVisible(false);
        textField.setStyle("-fx-border-color: transparent;");
    }

    // 11. FINAL VALIDATION TRƯỚC KHI SUBMIT (CẢI TIẾN)
    private boolean validateForm() {
        boolean hasError = false;

        // Validate tất cả fields một lần nữa
        String username = registerUserName.getText();
        String password = registerPassWord.getText();
        String confirmPassword = registerReEnterPW.getText();
        String fullName = StringUtils.capitalizeName(registerFullname.getText());
        String phoneNumber = registerPhoneNumber.getText();
        String email = registerEmail.getText();

        // Kiểm tra từng field và tổng hợp lỗi
        if (username.isEmpty() || username.length() <= 6) {
            hasError = true;
        }

        if (!TryCatchUtil.validatePassword(password)) {
            hasError = true;
        }

        if (confirmPassword.isEmpty() || !password.equals(confirmPassword)) {
            hasError = true;
        }

        if (!TryCatchUtil.validateFullName(fullName)) {
            hasError = true;
        }

        if (!TryCatchUtil.validateEmail(email)) {
            hasError = true;
        }

        if (!TryCatchUtil.validatePhone(phoneNumber)) {
            hasError = true;
        }

        // Kiểm tra trùng lặp (synchronous cho final check)
        try {
            List<String> userNameList = UserDAO.getAllValuesofColumn("username");
            List<String> emailList = UserDAO.getAllValuesofColumn("email");

            if (userNameList.contains(username) || emailList.contains(email)) {
                hasError = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
        }

        return !hasError; // Return true if no error
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
                if (new UserDAO().addUser(new User(user_name, pass_word, full_name, email, phone_number, 3, true))) {
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