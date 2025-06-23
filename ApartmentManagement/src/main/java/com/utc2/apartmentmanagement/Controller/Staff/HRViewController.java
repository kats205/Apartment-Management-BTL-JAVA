package com.utc2.apartmentmanagement.Controller.Staff;

import com.utc2.apartmentmanagement.Controller.DashboardController;
import com.utc2.apartmentmanagement.DAO.User.RoleDAO;
import com.utc2.apartmentmanagement.DAO.User.StaffDAO;
import com.utc2.apartmentmanagement.DAO.User.UserDAO;
import com.utc2.apartmentmanagement.Utils.PaginationUtils;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.Setter;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class HRViewController implements Initializable {
    @FXML public AnchorPane hrView;
    @FXML public TextField tfUserId;
    @FXML public TextField tfUsername;
    @FXML public PasswordField pfPassword;
    @FXML public TextField tfFullName;
    @FXML public TextField tfEmail;
    @FXML public TextField tfPhone;
    @FXML public TextField tfRole;
    @FXML public CheckBox cbActive;
    @FXML public Button btnAdd;
    @FXML public Button btnUpdate;
    @FXML public Button btnDelete;
    @FXML public Button btnClear;
    @FXML public Button btnSave;
    @FXML public ComboBox<String> cbFilterPosition;
    @FXML public Button btnSearch;
    @FXML public Button btnRefresh;
    @FXML public TableColumn<Map<String, Object>, Integer> colUserId;
    @FXML public TableColumn<Map<String, Object>, String> colRole;
    @FXML public TableColumn<Map<String, Object>, String> colFullName;
    @FXML public TableColumn<Map<String, Object>, String> colUsername;
    @FXML public TableColumn<Map<String, Object>, String> colEmail;
    @FXML public TableColumn<Map<String, Object>, String> colPhone;
    @FXML public TableColumn<Map<String, Object>, String> colDepartment;
    @FXML public TableColumn<Map<String, Object>, String> colPosition;
    @FXML public TableColumn<Map<String, Object>, Integer> colActive;
    @FXML public Label lblTotal;
    @FXML public Button btnClose;
    @FXML public TableView<Map<String, Object>> tvUsers;
    @FXML public TextField textFullName;
    @FXML public Pagination pagination;
    @Setter
    private DashboardController parentController;

    private static Stage addStaffStage;

    @FXML
    public void handleCloseButton(ActionEvent event) {
        // Xoá apartment view
        ((Pane)hrView.getParent()).getChildren().clear();
        // Thêm lại dashboard nodes từ controller cha
        parentController.getContentArea().getChildren().setAll(parentController.getDashboardNodes());
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // load thông tin của staff từ database
        loadDataStaff();

        // gán các giá trị cho combobox
        try {
            loadComboboxPosition();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // tìm kiếm onchange
        handleBtnSearchOnChanged();

        btnAdd.setOnAction(event -> {
            try{
                loadFormAddStaff();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void loadComboboxPosition() throws SQLException {
        List<String> positions = new StaffDAO().listPosition();
        cbFilterPosition.getItems().addAll(positions);
    }

    private void setValueCol(){
        colUserId.setCellValueFactory(data -> new SimpleObjectProperty<>((Integer) data.getValue().get("ID")));
        colUserId.setStyle("-fx-alignment: CENTER;");

        colUsername.setCellValueFactory(data -> new SimpleStringProperty((String) data.getValue().get("Tendangnhap")));
        colUsername.setStyle("-fx-alignment: CENTER;");

        colRole.setCellValueFactory(data -> new SimpleStringProperty((String) data.getValue().get("Vaitro")));
        colRole.setStyle("-fx-alignment: CENTER;");

        colFullName.setCellValueFactory(data -> new SimpleStringProperty((String) data.getValue().get("Hoten")));
        colFullName.setStyle("-fx-alignment: CENTER;");

        colEmail.setCellValueFactory(data -> new SimpleStringProperty((String) data.getValue().get("Email")));
        colEmail.setStyle("-fx-alignment: CENTER;");

        colPhone.setCellValueFactory(data -> new SimpleStringProperty((String) data.getValue().get("Sodienthoai")));
        colPhone.setStyle("-fx-alignment: CENTER;");

        colDepartment.setCellValueFactory(data -> new SimpleStringProperty((String) data.getValue().get("Phongban")));
        colDepartment.setStyle("-fx-alignment: CENTER;");

        colPosition.setCellValueFactory(data -> new SimpleStringProperty((String) data.getValue().get("Chucvu")));
        colPosition.setStyle("-fx-alignment: CENTER;");

        colActive.setCellValueFactory(data -> new SimpleObjectProperty<>((Boolean) data.getValue().get("Trangthai") ? 1 : 0));
        colActive.setStyle("-fx-alignment: CENTER;");

    }

    private void loadDataStaff(){
        try {
            setValueCol();
            List<Map<String, Object>> listStaff = new StaffDAO().getAllStaffInfo();
            setUpTableView(listStaff);
            PaginationUtils.setupPagination(
                    listStaff,
                    tvUsers,
                    pagination,
                    lblTotal

            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private void setUpTableView(List<Map<String, Object>> list){
        ObservableList<Map<String, Object>> observableList = FXCollections.observableArrayList(list);
        tvUsers.setItems(observableList);
        lblTotal.setText(String.valueOf(list.size()));
        PaginationUtils.setupPagination(
                list,
                tvUsers,
                pagination,
                lblTotal
        );
    }
    public void handleBtnSearchOnChanged() {
        textFullName.textProperty().addListener((observable, oldValue, newValue) -> {
            List<Map<String, Object>> listStaff = null;
            try {
                listStaff = new UserDAO().searchOnChange(newValue);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            ObservableList<Map<String, Object>> observableList = FXCollections.observableArrayList(listStaff);
            PaginationUtils.setupPagination(
                    listStaff,
                    tvUsers,
                    pagination,
                    lblTotal
            );
            tvUsers.setItems(observableList);
        });
    }
    @FXML
    public void handleBtnSearch(ActionEvent actionEvent) throws SQLException {
        setValueCol();
        String searchText = cbFilterPosition.getValue();
        List<Map<String, Object>> list = new StaffDAO().filterStaffByRoleName(searchText);
        PaginationUtils.setupPagination(
                list,
                tvUsers,
                pagination,
                lblTotal
        );
        setUpTableView(list);
    }

    public void handleBtnRefresh(ActionEvent actionEvent) throws SQLException {
        setValueCol();
        cbFilterPosition.setValue("Lọc theo chức vụ");
        List<Map<String, Object>> list = new StaffDAO().getAllStaffInfo();
        setUpTableView(list);
    }

    private void setValueText(Map<String, Object> StaffSelected){
        tfUserId.setText(StaffSelected.get("ID").toString());
        tfUsername.setText(StaffSelected.get("Tendangnhap").toString());
        tfFullName.setText(StaffSelected.get("Hoten").toString());
        tfEmail.setText(StaffSelected.get("Email").toString());
        tfPhone.setText(StaffSelected.get("Sodienthoai").toString());
    }

    public Map<String, Object> handleClickTVUsers() {
        Map<String, Object> selected = tvUsers.getSelectionModel().getSelectedItem();
        if(selected!=null){
            setValueText(selected);
        }
        return selected;
    }

    @FXML
    public void handleBtnUpdate(ActionEvent actionEvent) {
        UserDAO user = new UserDAO();
        RoleDAO role = new RoleDAO();

        int userId = Integer.parseInt(tfUserId.getText());
        // Kiểm tra các trường bắt buộc
        if (tfUsername.getText().isEmpty() || tfFullName.getText().isEmpty() || tfEmail.getText().isEmpty() ||
                tfPhone.getText().isEmpty() || tfRole.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Vui lòng điền đầy đủ tất cả các trường!");
            return;
        }

        // Kiểm tra định dạng email
        if (!tfEmail.getText().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Email không hợp lệ!");
            return;
        }

        // Kiểm tra định dạng số điện thoại
        if (!tfPhone.getText().matches("^\\d{9,11}$")) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Số điện thoại phải là số và có độ dài từ 9 đến 11 chữ số!");
            return;
        }

        // Nếu mọi kiểm tra đều hợp lệ, tiến hành cập nhật
        user.updateUserName(userId, tfUsername.getText());
        user.updateFullName(userId, tfFullName.getText());
        user.updateEmail(userId, tfEmail.getText());
        user.updatePhoneNumber(userId, tfPhone.getText());
        role.updateRoleName(userId, tfRole.getText());
        user.updateActive(userId, cbActive.isSelected());

        showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật người dùng thành công!");
        loadDataStaff(); // Tải lại dữ liệu
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    public void handleBtnClear(ActionEvent actionEvent) {
        tfUserId.setText("");
        tfUsername.setText("");
        tfFullName.setText("");
        tfEmail.setText("");
        tfPhone.setText("");
    }


    public void loadFormAddStaff() throws IOException {
        if (addStaffStage == null || !addStaffStage.isShowing()) {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/utc2/apartmentmanagement/fxml/FormAddStaff.fxml")));
            addStaffStage = new Stage();
            addStaffStage.setTitle("Apartment Application");
            addStaffStage.setScene(new Scene(root, 1200, 800));
            addStaffStage.show();

            // Khi cửa sổ bị đóng, reset lại biến
            addStaffStage.setOnHidden(e -> addStaffStage = null);
        } else {
            // Nếu cửa sổ đã mở, đưa nó lên trước (optional)
            addStaffStage.toFront();
        }
    }
}
