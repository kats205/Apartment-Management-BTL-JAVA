package com.utc2.apartmentmanagement.Controller;

import com.utc2.apartmentmanagement.DAO.ApartmentDAO;
import com.utc2.apartmentmanagement.DAO.ResidentDAO;
import com.utc2.apartmentmanagement.DAO.UserDAO;
import com.utc2.apartmentmanagement.Model.Session;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.awt.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.control.*;
import javafx.scene.control.Label;

public class UserDashboardController implements Initializable {
    @FXML public Label apartmentIdTf;
    @FXML public Label buildingTF;
    @FXML public Label areaTF;
    @FXML public Label floorTF;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            int user_id = new UserDAO().getIdByUserName(Session.getUserName());
            String apartment_id = new ResidentDAO().getApartmentIdByUserID(user_id);
            List<Object> objectList = new ApartmentDAO().getApartmentInfoByApartmentID(apartment_id);
            apartmentIdTf.setText(apartment_id);
            buildingTF.setText(String.valueOf(objectList.get(0)));
            floorTF.setText(String.valueOf(objectList.get(1)));
            areaTF.setText(String.valueOf(objectList.get(2)));

            System.out.println(apartment_id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
