package com.utc2.apartmentmanagement.Controller.User;

import com.utc2.apartmentmanagement.Controller.UserDashboardController;
import com.utc2.apartmentmanagement.DAO.ApartmentDAO;
import com.utc2.apartmentmanagement.DAO.UserDAO;
import com.utc2.apartmentmanagement.Model.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import lombok.Setter;
import lombok.SneakyThrows;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;
import java.util.ResourceBundle;

public class ComplaintsController implements Initializable {
    @FXML public AnchorPane ComplaintView;
    @Setter
    private UserDashboardController parentController;

    @FXML private TextField apartmentIdField;
    @FXML private TextField residentIdField;

    @FXML private DatePicker requestDatePicker;



    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set mặc định ngày gửi yêu cầu
        requestDatePicker.setValue(LocalDate.now());
        requestDatePicker.setDisable(true);

        fillResidentInfo();
    }


    private void fillResidentInfo() throws SQLException {
        // TODO: Get actual data from logged-in user session
        // For now, using dummy data
        String userName = Session.getUserName();
        int userId = new UserDAO().getIdByUserName(userName);
        Map<String, Object> apartmentInf = new ApartmentDAO().getInformation(userId);


        apartmentIdField.setText(String.valueOf(apartmentInf.get("apartment_id")));
        residentIdField.setText(String.valueOf((apartmentInf.get("resident_id"))));

        // These fields should be read-only
        apartmentIdField.setEditable(false);
        residentIdField.setEditable(false);
    }
    @FXML
    public void handleCancelButton(ActionEvent event) {
        // Xoá ComplaintView
        ((Pane) ComplaintView.getParent()).getChildren().clear();
        // Thêm lại dashboard nodes từ controller cha
        parentController.getContentArea().getChildren().setAll(parentController.getDashboardNodes());
    }


}
