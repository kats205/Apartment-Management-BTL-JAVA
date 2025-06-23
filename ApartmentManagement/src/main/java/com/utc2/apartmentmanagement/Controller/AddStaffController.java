package com.utc2.apartmentmanagement.Controller;

import com.utc2.apartmentmanagement.DAO.User.ManagerDAO;
import com.utc2.apartmentmanagement.DAO.User.StaffDAO;
import com.utc2.apartmentmanagement.DAO.User.UserDAO;
import com.utc2.apartmentmanagement.Model.Session;
import com.utc2.apartmentmanagement.Model.User.Staff;
import com.utc2.apartmentmanagement.Model.User.User;
import com.utc2.apartmentmanagement.Utils.AlertBox;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;

public class AddStaffController implements Initializable {
    @FXML private ImageView avatarImageView;
    @FXML private Button btnChooseAvatar;
    @FXML private TextField txtFullName;
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhoneNumber;
    @FXML private ComboBox<String> cbDepartment;
    @FXML private ComboBox<String> cbPosition;
    @FXML private DatePicker dpHireDate;
    @FXML private ComboBox<String> cbManager;
    @FXML private TextField txtDegree;
    @FXML private TextField txtCertificate;
    @FXML private CheckBox cbActive;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;
    @FXML private Button btnReset;

    // Validation patterns
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );

    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^0[0-9]{9}$"
    );

    private static final Pattern USERNAME_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9.]{3,20}$"
    );

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d@$!%*?&#]{6,}$"
    );

    private static final Pattern NAME_PATTERN = Pattern.compile(
            "^[\\p{L}\\s'.-]{2,100}$", Pattern.UNICODE_CHARACTER_CLASS
    );

    // CSS Styles
    private static final String ERROR_STYLE =
            "-fx-background-color: #ffebee; -fx-border-color: #f44336; -fx-border-width: 2; -fx-border-radius: 8; -fx-padding: 10;";

    private static final String SUCCESS_STYLE =
            "-fx-background-color: #e8f5e8; -fx-border-color: #4caf50; -fx-border-width: 2; -fx-border-radius: 8; -fx-padding: 10;";

    private static final String NORMAL_STYLE =
            "-fx-background-color: #f8f9fa; -fx-border-color: #2c5aa0; -fx-border-radius: 8; -fx-padding: 10;";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupValidation();
        try {
            loadComboBoxData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        setupEventHandlers();
        defaultHireDate();
    }

    public void defaultHireDate(){
        dpHireDate.setValue(LocalDate.now());
    }

    private void setupValidation() {
        // Validate Full Name
        txtFullName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                validateFullName();
            }
        });

        // Validate Username
        txtUsername.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                validateUsername();
            }
        });

        // Validate Password
        txtPassword.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                validatePassword();
            }
        });

        // Validate Email
        txtEmail.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                validateEmail();
            }
        });

        // Validate Phone Number
        txtPhoneNumber.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                validatePhoneNumber();
            }
        });

        // Validate ComboBoxes
        cbDepartment.valueProperty().addListener((obs, oldVal, newVal) -> validateDepartment());
        cbPosition.valueProperty().addListener((obs, oldVal, newVal) -> validatePosition());
        cbManager.valueProperty().addListener((obs, oldVal, newVal) -> validateManager());

        // Validate Date
        dpHireDate.valueProperty().addListener((obs, oldVal, newVal) -> validateHireDate());
    }

    // Validation Methods
    private boolean validateFullName() {
        String fullName = txtFullName.getText().trim();

        if (fullName.isEmpty()) {
            setFieldError(txtFullName, "Họ tên không được để trống!");
            return false;
        }

        if (!NAME_PATTERN.matcher(fullName).matches()) {
            setFieldError(txtFullName, "Họ tên chỉ chứa chữ cái, khoảng trắng và ký tự đặc biệt như '.', '-', '''!");
            return false;
        }

        if (fullName.length() < 2 || fullName.length() > 100) {
            setFieldError(txtFullName, "Họ tên phải từ 2-100 ký tự!");
            return false;
        }

        setFieldSuccess(txtFullName, "Họ tên hợp lệ ✓");
        return true;
    }

    private boolean validateUsername() {
        String username = txtUsername.getText().trim();

        if (username.isEmpty()) {
            setFieldError(txtUsername, "Tên đăng nhập không được để trống!");
            return false;
        }

        if (!USERNAME_PATTERN.matcher(username).matches()) {
            setFieldError(txtUsername, "Tên đăng nhập chỉ chứa chữ, số và. (3-20 ký tự)!");
            return false;
        }

        // TODO: Check username exists in database
        setFieldSuccess(txtUsername, "Tên đăng nhập hợp lệ ✓");
        return true;
    }

    private boolean validatePassword() {
        String password = txtPassword.getText();

        if (password.isEmpty()) {
            setFieldError(txtPassword, "Mật khẩu không được để trống!");
            return false;
        }

        if (password.length() < 6) {
            setFieldError(txtPassword, "Mật khẩu phải ít nhất 6 ký tự!");
            return false;
        }

        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            setFieldError(txtPassword, "Mật khẩu phải có ít nhất 1 chữ hoa, 1 chữ thường, 1 số!");
            return false;
        }

        setFieldSuccess(txtPassword, "Mật khẩu mạnh ✓");
        return true;
    }

    private boolean validateEmail() {
        String email = txtEmail.getText().trim();

        if (email.isEmpty()) {
            setFieldError(txtEmail, "Email không được để trống!");
            return false;
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            setFieldError(txtEmail, "Email không đúng định dạng!");
            return false;
        }

        // TODO: Check email exists in database
        setFieldSuccess(txtEmail, "Email hợp lệ ✓");
        return true;
    }

    private boolean validatePhoneNumber() {
        String phone = txtPhoneNumber.getText().trim();

        if (phone.isEmpty()) {
            setFieldError(txtPhoneNumber, "Số điện thoại không được để trống!");
            return false;
        }

        if (!PHONE_PATTERN.matcher(phone).matches()) {
            setFieldError(txtPhoneNumber, "Số điện thoại phải bắt đầu bằng 0 và có 10 chữ số!");
            return false;
        }

        // TODO: Check phone exists in database
        setFieldSuccess(txtPhoneNumber, "Số điện thoại hợp lệ ✓");
        return true;
    }

    private boolean validateDepartment() {
        if (cbDepartment.getValue() == null || cbDepartment.getValue().isEmpty()) {
            setComboBoxError(cbDepartment, "Vui lòng chọn phòng ban!");
            return false;
        }
        setComboBoxSuccess(cbDepartment);
        return true;
    }

    private boolean validatePosition() {
        if (cbPosition.getValue() == null || cbPosition.getValue().isEmpty()) {
            setComboBoxError(cbPosition, "Vui lòng chọn chức vụ!");
            return false;
        }
        setComboBoxSuccess(cbPosition);
        return true;
    }

    private boolean validateManager() {
        if (cbManager.getValue() == null || cbManager.getValue().isEmpty()) {
            setComboBoxError(cbManager, "Vui lòng chọn quản lý!");
            return false;
        }
        setComboBoxSuccess(cbManager);
        return true;
    }

    private boolean validateHireDate() {
        LocalDate hireDate = dpHireDate.getValue();

        if (hireDate == null) {
            setDatePickerError(dpHireDate, "Vui lòng chọn ngày tuyển dụng!");
            return false;
        }

        if (hireDate.isAfter(LocalDate.now())) {
            setDatePickerError(dpHireDate, "Ngày tuyển dụng không được trong tương lai!");
            return false;
        }

        if (hireDate.isBefore(LocalDate.of(1900, 1, 1))) {
            setDatePickerError(dpHireDate, "Ngày tuyển dụng không hợp lệ!");
            return false;
        }

        setDatePickerSuccess(dpHireDate);
        return true;
    }

    // Helper Methods for UI feedback
    private void setFieldError(TextField field, String message) {
        field.setStyle(ERROR_STYLE);
        field.setTooltip(new Tooltip(message));

        // Animation effect
        Platform.runLater(field::requestFocus);
    }

    private void setFieldSuccess(TextField field, String message) {
        field.setStyle(SUCCESS_STYLE);
        field.setTooltip(new Tooltip(message));
    }

    private void setFieldError(PasswordField field, String message) {
        field.setStyle(ERROR_STYLE);
        field.setTooltip(new Tooltip(message));
    }

    private void setFieldSuccess(PasswordField field, String message) {
        field.setStyle(SUCCESS_STYLE);
        field.setTooltip(new Tooltip(message));
    }

    private void setComboBoxError(ComboBox<String> comboBox, String message) {
        comboBox.setStyle(ERROR_STYLE);
        comboBox.setTooltip(new Tooltip(message));
    }

    private void setComboBoxSuccess(ComboBox<String> comboBox) {
        comboBox.setStyle(SUCCESS_STYLE);
        comboBox.setTooltip(new Tooltip("Hợp lệ ✓"));
    }

    private void setDatePickerError(DatePicker datePicker, String message) {
        datePicker.setStyle(ERROR_STYLE);
        datePicker.setTooltip(new Tooltip(message));
    }

    private void setDatePickerSuccess(DatePicker datePicker) {
        datePicker.setStyle(SUCCESS_STYLE);
        datePicker.setTooltip(new Tooltip("Hợp lệ ✓"));
    }

    // Load ComboBox Data
    private void loadComboBoxData() throws SQLException {
        List<Map<String, String>> list = new StaffDAO().getAllDepartmentsAndPositions();
        // Load departments
        Set<String> departments = new HashSet<>();
        Set<String> positions = new HashSet<>();

        for (Map<String, String> item : list) {
            System.out.println(item);
            departments.add(item.get("department"));
            positions.add(item.get("position"));
        }

        // Load vào combobox
        cbDepartment.getItems().addAll(departments);
        cbPosition.getItems().addAll(positions);

        // Load managers (example data)
        List<Map<String, String>> list1 = new ManagerDAO().getAllOfficeAndFullNameManager();
        Set<String> officesAndFullName = new HashSet<>();
        for (Map<String, String> item : list1) {
            System.out.println(item);
            officesAndFullName.add(item.get("full_name") + "-" + item.get("office"));
        }

        cbManager.getItems().addAll(officesAndFullName);
    }

    // Event Handlers
    private void setupEventHandlers() {
        btnSave.setOnAction(e -> handleSave());
        btnCancel.setOnAction(e -> handleCancel());
        btnReset.setOnAction(e -> handleReset());
        btnChooseAvatar.setOnAction(e -> handleChooseAvatar());
    }

    // Validate entire form
    public boolean validateForm() {
        boolean isValid = true;

        isValid &= validateFullName();
        isValid &= validateUsername();
        isValid &= validatePassword();
        isValid &= validateEmail();
        isValid &= validatePhoneNumber();
        isValid &= validateDepartment();
        isValid &= validatePosition();
        isValid &= validateManager();
        isValid &= validateHireDate();

        return isValid;
    }

    public int getInfAndSaveUser(){
        String userName = txtUsername.getText();
        String passWord = txtPassword.getText();
        String fullName = txtFullName.getText();
        String email = txtEmail.getText();
        String phoneNumber = txtPhoneNumber.getText();
        UserDAO user = new UserDAO();
        boolean isActive = cbActive.isSelected();
        boolean saveUser = user.addUser(new User(0,userName, passWord, fullName,email, phoneNumber, 2, isActive));
        if(saveUser){
            System.out.println("Lưu thông tin user thành công!");
        }
        return user.getUserIdBvEmail(email);
    }

    public void getInfAndSaveStaff(){
        String department = cbDepartment.getValue();
        String position = cbPosition.getValue();
        Date hireDate = Date.valueOf(dpHireDate.getValue());
        String managerValue = cbManager.getValue();

        String[] parts = managerValue.split("-", 2); // Tách 1 lần tại dấu gạch ngang
        String office = "";
        String fullName = "";

        if (parts.length == 2) {
            office = parts[0].trim();
            fullName = parts[1].trim();
        } else {
            // Có thể thêm thông báo lỗi nếu cần
            System.out.println("Giá trị không đúng định dạng: " + managerValue);
        }

        // Truy vấn ID từ DAO
        int managerId = new ManagerDAO().getManagerIdByFullNameAndOffice(fullName, office);
        System.out.println("Manager ID: " + managerId);
        String degree = txtDegree.getText();
        String certificate = txtCertificate.getText();
        // lưu thông tin user đồng thời lâyus user id
        int userId = getInfAndSaveUser();
        boolean saveStaff = new StaffDAO().addStaff(new Staff(userId,department, position, hireDate, managerId, degree, certificate));
        if(saveStaff){
            System.out.println("Lưu thông tin nhân viên thành công!");
        }
    }
    private void handleSave() {
        if (validateForm()) {
            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thành công");
            alert.setHeaderText("Thêm nhân viên thành công!");
            alert.setContentText("Thông tin nhân viên đã được lưu vào hệ thống.");
            alert.showAndWait();

            // TODO: Save to database
            // lưu thông tin staff
            getInfAndSaveStaff();
            System.out.println("Saving staff information...");
        } else {
            // Show error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi validation");
            alert.setHeaderText("Vui lòng kiểm tra lại thông tin!");
            alert.setContentText("Một số trường thông tin chưa đúng định dạng hoặc bị thiếu.");
            alert.showAndWait();
        }
    }

    private void handleCancel() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận");
        alert.setHeaderText("Bạn có chắc muốn hủy?");
        alert.setContentText("Tất cả thông tin đã nhập sẽ bị mất.");

        if (alert.showAndWait().get() == ButtonType.OK) {
            // Close window or navigate back
            btnCancel.getScene().getWindow().hide();
        }
    }

    private void handleReset() {
        // Reset all fields
        txtFullName.clear();
        txtUsername.clear();
        txtPassword.clear();
        txtEmail.clear();
        txtPhoneNumber.clear();
        txtDegree.clear();
        txtCertificate.clear();
        cbDepartment.setValue(null);
        cbPosition.setValue(null);
        cbManager.setValue(null);
        dpHireDate.setValue(null);
        cbActive.setSelected(true);
        loadDefaultAvatar();
        // Reset all styles to normal
        resetFieldStyles();
    }

    private void resetFieldStyles() {
        txtFullName.setStyle(NORMAL_STYLE);
        txtUsername.setStyle(NORMAL_STYLE);
        txtPassword.setStyle(NORMAL_STYLE);
        txtEmail.setStyle(NORMAL_STYLE);
        txtPhoneNumber.setStyle(NORMAL_STYLE);
        txtDegree.setStyle(NORMAL_STYLE);
        txtCertificate.setStyle(NORMAL_STYLE);
        cbDepartment.setStyle(NORMAL_STYLE);
        cbPosition.setStyle(NORMAL_STYLE);
        cbManager.setStyle(NORMAL_STYLE);
        dpHireDate.setStyle(NORMAL_STYLE);

        // Clear tooltips
        txtFullName.setTooltip(null);
        txtUsername.setTooltip(null);
        txtPassword.setTooltip(null);
        txtEmail.setTooltip(null);
        txtPhoneNumber.setTooltip(null);
        cbDepartment.setTooltip(null);
        cbPosition.setTooltip(null);
        cbManager.setTooltip(null);
        dpHireDate.setTooltip(null);
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
                    avatarImageView.setImage(image);
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
            String defaultImagePath = "/com/utc2/apartmentmanagement/assets/Profile/Admin/IMG_0466.JPG";
            Image defaultImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(defaultImagePath)));
            avatarImageView.setImage(defaultImage);
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



    public void handleChooseAvatar() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh đại diện");

        // Lọc file chỉ cho phép chọn ảnh
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );

        // Mở cửa sổ chọn file ảnh
        File selectedFile = fileChooser.showOpenDialog(btnChooseAvatar.getScene().getWindow());

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
                    avatarImageView.setImage(newImage);

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

}
