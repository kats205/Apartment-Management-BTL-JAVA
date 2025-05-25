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
    @FXML
    private Label apartmentIdLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Label buildingNameLabel;

    @FXML
    private ImageView apartmentImage;

    @FXML
    private Label addressLabel;

    @FXML
    private Label floorLabel;

    @FXML
    private Label areaLabel;

    @FXML
    private Label bedroomsLabel;

    @FXML
    private Label maintenanceFeeLabel;

    @FXML
    private Label propertyValueLabel;

    @FXML
    private Label primaryResidentLabel;

    @FXML
    private Label moveInDateLabel;

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
        if(apartmentInf.isEmpty()){
            System.out.println("Không có thông tin căn hộ nào");
        }else{
            primaryResidentLabel.setText(apartmentInf.get("full_name").toString());
            moveInDateLabel.setText(apartmentInf.get("move_in_date").toString());
            propertyValueLabel.setText(df.format(Double.parseDouble(apartmentInf.get("price_apartment").toString())) + " VNĐ");
            addressLabel.setText(apartmentInf.get("address").toString());
            apartmentIdLabel.setText(apartmentInf.get("apartment_id").toString());
            statusLabel.setText(apartmentInf.get("status").toString());
            floorLabel.setText(apartmentInf.get("floor").toString());
            areaLabel.setText(apartmentInf.get("area").toString() + " m2");
            bedroomsLabel.setText(apartmentInf.get("bedrooms").toString());
            maintenanceFeeLabel.setText(df.format(Double.parseDouble(apartmentInf.get("maintenance_fee").toString())) + " VNĐ");


        }
    }


    private void closeWindow() {
        // Lấy cửa sổ chứa controller này
        Stage stage = (Stage) closeButton.getScene().getWindow();
        // Đóng cửa sổ
        stage.close();
    }
}