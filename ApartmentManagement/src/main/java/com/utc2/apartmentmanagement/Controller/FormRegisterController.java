package com.utc2.apartmentmanagement.Controller;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.utc2.apartmentmanagement.DAO.UserDAO;
import com.utc2.apartmentmanagement.Model.User;
import com.utc2.apartmentmanagement.Utils.StringUtils;
import com.utc2.apartmentmanagement.Utils.TryCatchUtil;

import java.util.List;

import static com.utc2.apartmentmanagement.Utils.AlertBox.showAlertForExeptionRegister;

public class FormRegisterController {
    public Button registerBtn;
    @FXML
    private TextField userName;

    @FXML
    private PasswordField passWord;

    @FXML
    private TextField fullName;

    @FXML
    private TextField PhoneNumber;

    @FXML
    private PasswordField reEnterPassWord;

    @FXML
    private TextField Email;

    @FXML
    public void handleRegister() {
        String full_name = null;
        try {
            String user_name = userName.getText();
            String pass_word = passWord.getText();
            String re_password = reEnterPassWord.getText();
            full_name = StringUtils.capitalizeName(fullName.getText());
            String phone_number = PhoneNumber.getText();
            String email = Email.getText();
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
                if(new UserDAO().addUser(new User(user_name, pass_word, full_name, email, phone_number, 3, true))){
                    showAlertForExeptionRegister("Thông báo!", "Đăng ký tài khoản thành công!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlertForExeptionRegister("Cảnh báo!", "Đã xảy ra lỗi! Vui lòng thử lại");
        }
    }
}
