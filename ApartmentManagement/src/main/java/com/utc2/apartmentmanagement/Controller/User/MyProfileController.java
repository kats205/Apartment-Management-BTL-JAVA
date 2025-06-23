package com.utc2.apartmentmanagement.Controller.User;

import com.utc2.apartmentmanagement.Controller.DashboardController;
import com.utc2.apartmentmanagement.Controller.Staff.StaffDashboardController;
import com.utc2.apartmentmanagement.DAO.User.ManagerDAO;
import com.utc2.apartmentmanagement.DAO.User.StaffDAO;
import com.utc2.apartmentmanagement.DAO.User.UserDAO;
import com.utc2.apartmentmanagement.Model.Session;
import com.utc2.apartmentmanagement.Model.User.Staff;
import com.utc2.apartmentmanagement.Model.User.User;
import com.utc2.apartmentmanagement.Utils.AlertBox;
import com.utc2.apartmentmanagement.Utils.passwordEncryption;
import com.utc2.apartmentmanagement.Views.login;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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
import java.util.Objects;
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
        loadCurrentAvatar();

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

    // 1. Tải ảnh avatar hiện tại
    private void loadCurrentAvatar() {
        try {
            String fileName = new UserDAO().getAvatarPathByUserId(Session.getUserName());
            if (fileName != null && !fileName.isEmpty()) {
                // Tạo đường dẫn đến file ảnh
                Path imagePath = Paths.get("uploads", "avatars", fileName);

                if (Files.exists(imagePath)) {
                    // Nếu file tồn tại, load ảnh
                    Image image = new Image(imagePath.toUri().toString());
                    userAvatar.setImage(image);
                } else {
                    // Nếu file không tồn tại, dùng ảnh mặc định
                    loadDefaultAvatar();
                }
            } else {
                // Nếu chưa có ảnh trong DB, dùng ảnh mặc định
                loadDefaultAvatar();
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tải ảnh avatar: " + e.getMessage());
            loadDefaultAvatar();
        }
    }

    // 2. Load ảnh mặc định
    private void loadDefaultAvatar() {
        try {
            // Đường dẫn ảnh mặc định trong resources
            String defaultImagePath = "com/utc2/apartmentmanagement/assets/Profile/Admin/IMG_0466.JPG";
            Image defaultImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(defaultImagePath)));
            userAvatar.setImage(defaultImage);
        } catch (Exception e) {
            System.err.println("Không thể load ảnh mặc định: " + e.getMessage());
        }
    }

    // 4. Xóa ảnh cũ
    private void deleteOldAvatar(int userId) {
        try {
            String oldFileName = new UserDAO().getAvatarPathByUserId(Session.getUserName());
            if (oldFileName != null && !oldFileName.isEmpty()) {
                Path oldFilePath = Paths.get("uploads", "avatars", oldFileName);
                if (Files.exists(oldFilePath)) {
                    Files.delete(oldFilePath);
                    System.out.println("Đã xóa ảnh cũ: " + oldFileName);
                }
            }
        } catch (Exception e) {
            System.err.println("Không thể xóa ảnh cũ: " + e.getMessage());
            // Không throw exception vì đây không phải lỗi nghiêm trọng
        }
    }


    // 5. Lấy extension của file
    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex).toLowerCase();
        }
        return ".jpg"; // default extension
    }

    // 8. Validation kích thước file (optional)
    private boolean isValidImageSize(File file) {
        long maxSize = 5 * 1024 * 1024; // 5MB
        return file.length() <= maxSize;
    }

    // 9. Validation định dạng ảnh (optional)
    private boolean isValidImageFormat(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") ||
                fileName.endsWith(".png") || fileName.endsWith(".gif") ||
                fileName.endsWith(".bmp");
    }

    @FXML
    public void handleChangeAvatar(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh đại diện");

        // Lọc file chỉ cho phép chọn ảnh
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );

        // Mở cửa sổ chọn file ảnh
        File selectedFile = fileChooser.showOpenDialog(changeAvatarBtn.getScene().getWindow());

        if (selectedFile != null) {
            try {
                // 1. Tạo thư mục uploads/avatars nếu chưa tồn tại
                Path destinationDir = Paths.get("uploads", "avatars");
                if (!Files.exists(destinationDir)) {
                    Files.createDirectories(destinationDir);
                    System.out.println("Đã tạo thư mục: " + destinationDir.toAbsolutePath());
                }

                // 2. Lấy thông tin user
                int userId = new UserDAO().getIdByUserName(Session.getUserName());
                System.out.println("User ID(Lấy thông tin user): " + userId);
                // 3. Xóa ảnh cũ (nếu có)
                deleteOldAvatar(userId);

                // 4. Tạo tên file mới (unique)
                String fileExtension = getFileExtension(selectedFile.getName());
                String newFileName = "user_" + userId + "_" + System.currentTimeMillis() + fileExtension;
                Path destinationFile = destinationDir.resolve(newFileName);

                // 5. Copy ảnh vào thư mục
                Files.copy(selectedFile.toPath(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Đã copy ảnh đến: " + destinationFile.toAbsolutePath());

                // 6. Cập nhật database
                UserDAO userDAO = new UserDAO();
                if (userDAO.updateAvatar(userId, newFileName)) {
                    System.out.println("Đã cập nhật ảnh đại diện vào database!");

                    // 7. Hiển thị ảnh mới lên ImageView
                    Image newImage = new Image(destinationFile.toUri().toString());
                    userAvatar.setImage(newImage);

                    // 8. Cập nhật Session
                    Session.setAvatarPath(newFileName);

                    // 9. Thông báo thành công
                    AlertBox.showAlert("Thành công", "Đã thay đổi ảnh đại diện thành công!");

                } else {
                    // Nếu cập nhật DB thất bại, xóa file đã copy
                    Files.deleteIfExists(destinationFile);
                    AlertBox.showAlert("Lỗi", "Không thể cập nhật ảnh đại diện vào database!");
                }

            } catch (IOException e) {
                System.err.println("Lỗi I/O: " + e.getMessage());
                AlertBox.showAlert("Lỗi", "Không thể lưu file ảnh: " + e.getMessage());
            } catch (SQLException e) {
                System.err.println("Lỗi SQL: " + e.getMessage());
                AlertBox.showAlert("Lỗi", "Lỗi database: " + e.getMessage());
            }
        }
    }
    public void initInformation() throws SQLException {
        int userId = new UserDAO().getIdByUserName(Session.getUserName());
        User user = new UserDAO().getUserByID(userId);

        switch (user.getRoleID()) {
            case 1 -> loadManagerInfo(userId);
            case 2 -> loadStaffInfo(user);
            case 3 -> loadResidentInfo(user);
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

    // Hàm xử lý resident(role_id = 3)
    private void loadResidentInfo(User user) throws SQLException {
        Staff staff = new StaffDAO().getStaffByUserId(user.getUserID());
        if (staff == null) {
            System.out.println("Không có thông tin staff.");
            return;
        }

        fullNameField.setText(user.getFullName());
        emailField.setText(user.getEmail());
        phoneField.setText(user.getPhoneNumber());
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
        StaffDAO staffDAO = new StaffDAO();
        boolean phoneUpdated = update.updatePhoneNumber(user_id, phone);
        boolean emailUpdated = update.updateEmail(user_id, email);
        boolean nameUpdated = update.updateFullName(user_id, FullName);
        boolean deptUpdated = staffDAO.updateDepartment(user_id, office);

        if (phoneUpdated && emailUpdated && nameUpdated && deptUpdated) {
            AlertBox.showAlertForExeptionRegister("Thông báo!", "Cập nhật thông tin thành công!");
        } else {
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
