package org.example.apartmentmanagement.Controller;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.apartmentmanagement.DAO.UserDAO;
import org.example.apartmentmanagement.Utils.passwordEncryption;

import java.sql.Date;
import java.time.LocalDate;

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
    public void handleRegister(){
        try{
            String user_name = userName.getText();
            String pass_word = passWord.getText();
            String full_name = fullName.getText();
            String phone_number = PhoneNumber.getText();
            String RE_password = reEnterPassWord.getText();
            String email = Email.getText();
            if(pass_word.equals(RE_password)){

                UserDAO.addUser(user_name, pass_word, full_name,
                        email, phone_number, 3, true);
            }
            else{
                System.out.println("Lỗi đăng ký");
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }

}
