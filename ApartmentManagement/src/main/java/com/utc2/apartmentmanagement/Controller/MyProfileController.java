package com.utc2.apartmentmanagement.Controller;

import com.utc2.apartmentmanagement.DAO.ManagerDAO;
import com.utc2.apartmentmanagement.DAO.StaffDAO;
import com.utc2.apartmentmanagement.DAO.UserDAO;
import com.utc2.apartmentmanagement.Model.Session;
import com.utc2.apartmentmanagement.Model.Staff;
import com.utc2.apartmentmanagement.Model.User;
import com.utc2.apartmentmanagement.Utils.AlertBox;
import com.utc2.apartmentmanagement.Utils.passwordEncryption;
import com.utc2.apartmentmanagement.Views.login;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;


public class MyProfileController implements Initializable {
    @FXML public Button themeButton;
    @FXML public Button saveSettingsButton;
    @FXML public Button resetButton;
    @FXML public Button closeButton;
    @FXML public AnchorPane SettingView;
    @FXML public Button changeAvatarBtn;
    @FXML public ImageView userAvatar;
    @FXML public Label usernameLabel1;
    @FXML public Label roleLabel1;
    @FXML public Label lastLoginLabel1;
    @FXML public Label lastLogin;
    @FXML public Label userName;
    @FXML public TextField fullNameField;
    @FXML public TextField emailField;
    @FXML public TextField phoneField;
    @FXML public TextField officeField;
    @FXML public Button saveProfileBtn;
    @FXML public PasswordField currentPasswordField;
    @FXML public PasswordField confirmPasswordField;
    @FXML public PasswordField newPasswordField1;
    @FXML public Button changePasswordBtn1;
    @FXML public Button logoutBtn;
    @FXML public Label roleName;
    @FXML public AnchorPane rootPane;
    @Setter
    private DashboardController parentDashBoardController;
    @Setter
    private UserDashboardController parentUserDashBoardController;
    @Setter
    private StaffDashboardController parentStaffDashBoard;
    @Setter
    private UserDashboardController parentUserDashBoard;
    @Setter
    private Stage dashboardStage; // để tắt stage chính

    @FXML
    public void handleCloseButton(ActionEvent event) {
        // Xoá myProfile view
        ((Stage) changeAvatarBtn.getScene().getWindow()).close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // set tên và thời gian đăng nhập của người dùng
        setInformationUser();
        // set lại avatar cho người dùng
        try {
            String filePath = new UserDAO().getAvatarPathByUserId(Session.getUserName());
            if(filePath!=null){
                Path pathImage = Paths.get(System.getProperty("user.home"), "apartment_app", "avatars", filePath);
                Image image = new Image(pathImage.toUri().toString());
                userAvatar.setImage(image);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            initInformation();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setInformationUser(){
        String username = Session.getUserName();
        String lastlogin = Session.getLastLogin();
        System.out.println("Last login: " + lastLogin);
        System.out.println("User name: " + userName);
        userName.setText(username);
        lastLogin.setText(lastlogin);
        roleName.setText(Session.getRoleName());
    }
    @FXML
    public void handleLogout() throws Exception {
        if (dashboardStage != null) {
            dashboardStage.close();
            ((Stage)fullNameField.getScene().getWindow()).close();
        }

        Stage stage = new Stage();
        login loginView = new login();
        loginView.start(stage);
    }

    @FXML
    public void handleChangeAvatar(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh đại diện");

// Lọc file chỉ cho phép chọn ảnh
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

// Mở cửa sổ chọn file ảnh
        File selectedFile = fileChooser.showOpenDialog(changeAvatarBtn.getScene().getWindow());

        if (selectedFile != null) {
            try {
                // Thư mục đích: C:/Users/<Tên người dùng>/apartment_app/avatars
                Path destinationDir = Paths.get(System.getProperty("user.home"), "apartment_app", "avatars");
                Files.createDirectories(destinationDir); // Tạo thư mục nếu chưa có

                // Tên file mới (giữ nguyên hoặc có thể đổi tên nếu muốn)
                int user_id = new UserDAO().getIdByUserName(Session.getUserName());
                String newFileName = "user_" + user_id + "_" + selectedFile.getName(); // Gợi ý đặt tên duy nhất
                Path destinationFile = destinationDir.resolve(newFileName);
                Session.setAvatarPath(newFileName);
                // Copy ảnh vào thư mục
                Files.copy(selectedFile.toPath(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
                //Lưu tên ảnh vào database tương ứng với user_id
                if(new UserDAO().updateAvatar(user_id, newFileName)){
                    System.out.println("Đã cập nhật ảnh đại diện vào database!");
                }
                // Hiển thị ảnh mới lên ImageView
                Image image = new Image(destinationFile.toUri().toString());
                userAvatar.setImage(image);

                // Lưu đường dẫn vào Session (hoặc DB nếu bạn muốn lưu lâu dài)
                Session.setAvatarPath(destinationFile.toString());

                System.out.println("Đã thay đổi ảnh đại diện thành công!");

            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void initInformation() throws SQLException {
        int userId = new UserDAO().getIdByUserName(Session.getUserName());
        User user = new UserDAO().getUserByID(userId);

        switch (user.getRoleID()) {
            case 1 -> loadManagerInfo(userId);
            case 2 -> loadStaffInfo(user);
            default -> System.out.println("Không xác định được vai trò người dùng.");
        }
    }

    // Hàm xử lý Staff (role_id = 2)
    private void loadStaffInfo(User user) throws SQLException {
        Staff staff = new StaffDAO().getStaffByUserId(user.getUserID());
        if (staff == null) {
            System.out.println("Không có thông tin staff.");
            return;
        }

        fullNameField.setText(user.getFullName());
        emailField.setText(user.getEmail());
        phoneField.setText(user.getPhoneNumber());
        officeField.setText(staff.getDepartment());
    }

    // Hàm xử lý Manager (role_id = 1)
    private void loadManagerInfo(int userId) throws SQLException {
        Map<String, Object> infoMap = new ManagerDAO().getManagerByUserId(userId);
        if (infoMap == null || infoMap.isEmpty()) {
            System.out.println("Không có thông tin manager.");
            return;
        }

        fullNameField.setText(String.valueOf(infoMap.get("full_name")));
        emailField.setText(String.valueOf(infoMap.get("email")));
        phoneField.setText(String.valueOf(infoMap.get("phone_number")));
        officeField.setText(String.valueOf(infoMap.get("office")));
    }

    public void handleSaveProfileBtn(ActionEvent actionEvent) throws SQLException {
        int user_id = new UserDAO().getIdByUserName(Session.getUserName());
        String FullName = fullNameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String office = officeField.getText();
        UserDAO update = new UserDAO();
        if(update.updatePhoneNumber(user_id, phone) && update.updateEmail(user_id, email) && update.updateFullName(user_id, FullName) && new StaffDAO().updateDepartment(user_id, office)){
            AlertBox.showAlertForExeptionRegister("Thông báo!", "Cập nhật thông tin thành công!");
        }
        else{
            AlertBox.showAlertForExeptionRegister("Thông báo!", "Cập nhật thông tin không thành công!");
        }
    }

    public void handleChangePassword(ActionEvent actionEvent) throws Exception {
        // password vừa nhập
        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField1.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            AlertBox.showAlertForExeptionRegister("Thông báo!", "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            AlertBox.showAlertForExeptionRegister("Thông báo!", "Mật khẩu mới không khớp!");
            return;
        }

        UserDAO userDAO = new UserDAO();
        int user_id = userDAO.getIdByUserName(Session.getUserName());
        // password dưới database
        String currentPasswordInDB = userDAO.getPasswordByUserId(user_id);

        // vì password được hash bằng bcrypt nên ở đây cần check lại
        // Nếu bạn lưu mật khẩu đã mã hóa (băm), bạn cần so sánh đã hash lại
        if (!passwordEncryption.checkPassword(currentPassword, currentPasswordInDB)) {
            AlertBox.showAlertForExeptionRegister("Thông báo!", "Mật khẩu hiện tại không đúng!");
            return;
        }
        // Thay đổi mật khẩu
        boolean updated = userDAO.updatePassword(user_id, newPassword);
        if (updated) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Đổi mật khẩu thành công");
            alert.setHeaderText("Bạn đã đổi mật khẩu thành công.");
            alert.setContentText("Bạn có muốn đăng xuất và đăng nhập lại không?");
            ButtonType buttonYes = new ButtonType("Đăng xuất");
            ButtonType buttonNo = new ButtonType("Tiếp tục sử dụng");
            alert.getButtonTypes().setAll(buttonYes, buttonNo);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == buttonYes) {
                handleLogout(); // giả định có phương thức chuyển scene
            } else {
                currentPasswordField.clear();
                newPasswordField1.clear();
                confirmPasswordField.clear();
            }
        } else {
            AlertBox.showAlertForExeptionRegister("Lỗi", "Đổi mật khẩu thất bại!");
        }

    }
}
