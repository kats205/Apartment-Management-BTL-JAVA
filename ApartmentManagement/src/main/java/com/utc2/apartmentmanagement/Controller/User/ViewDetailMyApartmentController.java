package com.utc2.apartmentmanagement.Controller.User;

import com.utc2.apartmentmanagement.DAO.ApartmentDAO;
import com.utc2.apartmentmanagement.DAO.UserDAO;
import com.utc2.apartmentmanagement.Model.Session;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

public class ViewDetailMyApartmentController {
    @FXML public Label address;
    @FXML public Label price;
    @FXML public Label moveInDate;
    @FXML public Label fullNameResident;
    @FXML public Label floor;
    @FXML public Label area;
    @FXML public Label bedrooms;
    @FXML public Label maintenanceFee;
    @FXML
    private Button closeButton;

    private NumberFormat currencyFormat;

    /**
     * Khởi tạo controller
     */
    @FXML
    public void initialize() throws SQLException {
        // Khởi tạo định dạng tiền tệ
        currencyFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

        // Thiết lập sự kiện đóng cửa sổ
        closeButton.setOnAction(event -> closeWindow());
        getInformationResident();
    }

    private void getInformationResident() throws SQLException {
        String userName = Session.getUserName();
        int userId = new UserDAO().getIdByUserName(userName);
        Map<String, Object> apartmentInf = new ApartmentDAO().getInformation(userId);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.'); // Dùng dấu chấm thay vì dấu phẩy

        DecimalFormat df = new DecimalFormat("#,###", symbols);
        df.setMaximumFractionDigits(0);
        fullNameResident.setText(apartmentInf.get("full_name").toString());
        moveInDate.setText(apartmentInf.get("move_in_date").toString());
        price.setText(df.format(Double.parseDouble(apartmentInf.get("price_apartment").toString())));
        address.setText(apartmentInf.get("address").toString());
        floor.setText(apartmentInf.get("floor").toString());
        area.setText(apartmentInf.get("area").toString());
        bedrooms.setText(apartmentInf.get("bedrooms").toString());
        maintenanceFee.setText(df.format(Double.parseDouble(apartmentInf.get("maintenance_fee").toString())));
    }
    /**
     * Thiết lập dữ liệu căn hộ để hiển thị
     * @param apartment Dữ liệu căn hộ cần hiển thị
     */
//    public void setApartmentData(MyApartmentController.ApartmentData apartment) {
        // Thiết lập thông tin căn hộ


        // Thử tải ảnh căn hộ


        // Thiết lập các thông tin chi tiết


    /**
     * Đóng cửa sổ chi tiết
     */
    private void closeWindow() {
        // Lấy cửa sổ chứa controller này
        Stage stage = (Stage) closeButton.getScene().getWindow();
        // Đóng cửa sổ
        stage.close();
    }
}