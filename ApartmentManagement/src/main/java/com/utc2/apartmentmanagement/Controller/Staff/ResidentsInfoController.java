package com.utc2.apartmentmanagement.Controller.Staff;

import com.utc2.apartmentmanagement.Controller.DashboardController;
import com.utc2.apartmentmanagement.DAO.User.ResidentDAO;
import com.utc2.apartmentmanagement.DAO.User.UserDAO;
import com.utc2.apartmentmanagement.Model.User.Resident;
import com.utc2.apartmentmanagement.Utils.AlertBox;
import com.utc2.apartmentmanagement.Utils.PaginationUtils;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import lombok.Setter;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class ResidentsInfoController implements Initializable {
    public ComboBox<String> genderFilter;
    public TextField searchField;
    public Button exportBtn;
    public Button clearBtn;
    public TableView<Resident> residentsTable;
    public TableColumn<Resident, Integer> residentIdCol;
    public TableColumn<Resident, String> fullNameCol;
    public TableColumn<Resident, String> identityCardCol;
    public TableColumn<Resident, String> apartmentIdCol;
    public TableColumn<Resident, Date> dateOfBirthCol;
    public TableColumn<Resident, String> genderCol;
    public TableColumn<Resident, Date> moveInDateCol;
    public Label summaryLabel;
    public Button cancelBtn;
    public Pagination pagination;
    public AnchorPane residentView;
    public ProgressIndicator searchLoading;
    @Setter
    private DashboardController parentController;
    private static final List<Resident> residentsInit = new ResidentDAO().getAllResident();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpTable(residentsInit);
        setUpComboBox();
        setUpSearchOnChange();
        setUpActionForCB();
        searchLoading.setVisible(false);
    }

    public void setUpActionForCB(){
        genderFilter.setOnAction(event -> {
            genderFilter(genderFilter.getValue());
        });
    }


    public void genderFilter(String gender){
        List<Resident> residentFilter = new ArrayList<>();
        for(Resident r : residentsInit){
            if(r.getGender().equals(gender)){
                residentFilter.add(r);
            }
        }
        setUpTable(residentFilter);
    }


    public void setUpSearchOnChange(){
        PauseTransition pause = new PauseTransition(Duration.millis(500));

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchLoading.setVisible(true); // hiện spinner khi bắt đầu gõ

            pause.setOnFinished(event -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    setUpTable(new ResidentDAO().getAllResident());
                } else {
                    String type = detectType(newValue);
                    List<Resident> residents = null;

                    if ("apartment_id".equalsIgnoreCase(type)) {
                        residents = new ResidentDAO().searchOnChange(newValue, "apartment_id");
                    } else if ("full_name".equalsIgnoreCase(type)) {
                        residents = new ResidentDAO().searchOnChange(newValue, "full_name");
                    }

                    setUpTable(residents);
                }

                searchLoading.setVisible(false); // ẩn spinner sau khi xử lý
            });

            pause.playFromStart();
        });

    }

    public void setUpComboBox(){
        genderFilter.getItems().addAll("Nam", "Nu");
    }

    public void setUpColForResidentTable(){
        residentIdCol.setCellValueFactory(new PropertyValueFactory<>("residentID"));
        residentIdCol.setStyle("-fx-alignment: CENTER;");

        fullNameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        fullNameCol.setStyle("-fx-alignment: CENTER;");

        identityCardCol.setCellValueFactory(new PropertyValueFactory<>("identityCard"));
        identityCardCol.setStyle("-fx-alignment: CENTER;");

        apartmentIdCol.setCellValueFactory(new PropertyValueFactory<>("apartmentID"));
        apartmentIdCol.setStyle("-fx-alignment: CENTER;");

        dateOfBirthCol.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        dateOfBirthCol.setStyle("-fx-alignment: CENTER;");

        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        genderCol.setStyle("-fx-alignment: CENTER;");

        moveInDateCol.setCellValueFactory(new PropertyValueFactory<>("moveInDate"));
        moveInDateCol.setStyle("-fx-alignment: CENTER;");

    }

    public void setUpTable(List<Resident> residents){
        setUpColForResidentTable();
        ObservableList<Resident> observableResidents = FXCollections.observableList(residents);
        residentsTable.setItems(observableResidents);
        PaginationUtils.setupPagination(
                residents,
                residentsTable,
                pagination
        );
        summaryLabel.setText("Showing " + residents.size() + " of " + residentsInit.size() + " residents");
    }


    @FXML
    public void handleExport(ActionEvent actionEvent) {

    }
    @FXML
    public void handleClear(ActionEvent actionEvent) {
        setUpTable(residentsInit);
    }
    @FXML
    public void handleCancel(ActionEvent actionEvent) {
        ((Pane) residentView.getParent()).getChildren().clear();
        // Thêm lại dashboard nodes từ controller cha
        parentController.getContentArea().getChildren().setAll(parentController.getDashboardNodes());
    }

    public String detectType(String input) {
        if (input.matches("[A-Z]\\d+[A-Z]\\d+")) {
            return "apartment_id";
        } else if (input.matches("[\\p{L} ]+")) {
            return "full_name";
        } else {
            return "unknown";
        }
    }

}
