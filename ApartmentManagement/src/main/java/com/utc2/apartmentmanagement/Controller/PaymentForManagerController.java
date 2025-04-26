package com.utc2.apartmentmanagement.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import com.utc2.apartmentmanagement.DAO.ApartmentDAO;
import com.utc2.apartmentmanagement.DAO.BillsDAO;
import com.utc2.apartmentmanagement.Model.Bills;
import com.utc2.apartmentmanagement.Views.BillItemsView;
import com.utc2.apartmentmanagement.Views.DashBoardView;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class PaymentForManagerController implements Initializable {
    @FXML
    public TableColumn<Bills, LocalDate> createDateCol;
    @FXML
    public TableColumn<Bills, LocalDate> updateDateCol;
    @FXML
    private ComboBox<String> apartmentComboBox;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private TableView<Bills> billsTableView;
    @FXML private TableColumn<Bills, Integer> billIdColumn;
    @FXML private TableColumn<Bills, String> apartmentIdColumn;
    @FXML private TableColumn<Bills, LocalDate> billingDateColumn;
    @FXML private TableColumn<Bills, LocalDate> dueDateColumn;
    @FXML private TableColumn<Bills, Double> totalAmountColumn;
    @FXML private TableColumn<Bills, Double> lateFeeColumn;
    @FXML private TableColumn<Bills, String> statusColumn;
    @FXML private TableColumn<Bills, Void> actionColumn;
    @FXML private Label totalBillsLabel;
    @FXML private Label statusLabel;


    public void searchBills(ActionEvent actionEvent) {
        // có 3 trường hợp:
        // th1: chỉ tìm kiếm bằng apartment id
        //th2: chỉ tìm kiếm bằng status
        // th3: tìm kiếm bằng cả 2 giá trị trên

        //th1
        String id = apartmentComboBox.getValue();
        String status = statusComboBox.getValue();
        if(id == null && status!=null){
            List<Bills> Bills = new BillsDAO().getAllBillByStatus(status);
            getBillList(Bills);
        }
        else if(id!=null & status == null){
            Bills bill = new BillsDAO().getBillByApartmentID(id);
            ObservableList<Bills> BillList = FXCollections.observableArrayList();
            Bills Bills = bill;
            BillList.addAll(Bills);
            billsTableView.setItems(BillList);
        }
        else{
            Bills bill = new BillsDAO().getBillByApartmentIdAndStatus(id, status);
            ObservableList<Bills> BillList = FXCollections.observableArrayList();
            Bills Bills = bill;
            BillList.addAll(Bills);
            billsTableView.setItems(BillList);
        }
    }

    public void refreshBills(ActionEvent actionEvent) {
    }

    public void createNewBill(ActionEvent actionEvent) {
    }

    public void viewSelectedBillDetails(ActionEvent actionEvent) {
    }

    public void paySelectedBill(ActionEvent actionEvent) {
    }

    public void exportBillsReport(ActionEvent actionEvent) {
    }

    public void closeWindow(ActionEvent actionEvent) {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getView();
        List<String> apartments = new ApartmentDAO().getAllIdApartment();
        apartmentComboBox.getItems().addAll(apartments);
        statusComboBox.getItems().add("pending");
        statusComboBox.getItems().add("paid");
        statusComboBox.getItems().add("overdue");
        statusComboBox.getItems().add("cancelled");

    }
    public void getView(){
        billIdColumn.setCellValueFactory(new PropertyValueFactory<>("billID"));
        billIdColumn.setStyle("-fx-font-size: 14px; -fx-alignment: CENTER;");
        billIdColumn.setPrefWidth(100);

        apartmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("apartmentID"));
        apartmentIdColumn.setStyle("-fx-font-size: 14px; -fx-alignment: CENTER;");
        apartmentIdColumn.setPrefWidth(50);

        billingDateColumn.setCellValueFactory(new PropertyValueFactory<>("billingDate"));
        billingDateColumn.setStyle("-fx-font-size: 14px; -fx-alignment: CENTER;");
        apartmentIdColumn.setPrefWidth(100);

        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        dueDateColumn.setStyle("-fx-font-size: 14px; -fx-alignment: CENTER;");
        dueDateColumn.setPrefWidth(100);

        totalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        totalAmountColumn.setStyle("-fx-font-size: 14px; -fx-alignment: CENTER;");
        totalAmountColumn.setPrefWidth(100);

        lateFeeColumn.setCellValueFactory(new PropertyValueFactory<>("late_fee"));
        lateFeeColumn.setStyle("-fx-font-size: 14px; -fx-alignment: CENTER;");
        lateFeeColumn.setPrefWidth(100);

        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setStyle("-fx-font-size: 14px; -fx-alignment: CENTER;");
        statusColumn.setPrefWidth(150);

        createDateCol.setCellValueFactory(new PropertyValueFactory<>("created_at"));
        createDateCol.setStyle("-fx-font-size: 14px; -fx-alignment: CENTER;");
        createDateCol.setPrefWidth(100);

        updateDateCol.setCellValueFactory(new PropertyValueFactory<>("updated_at"));
        updateDateCol.setStyle("-fx-font-size: 14px; -fx-alignment: CENTER;");
        updateDateCol.setPrefWidth(100);

        List<Bills> Bills = new BillsDAO().getAllbills();
        getBillList(Bills);

    }
    public void getBillList(List<Bills> billList){
        ObservableList<Bills> BillList = FXCollections.observableArrayList();
        List<Bills> Bills = billList;
        BillList.addAll(Bills);
        billsTableView.setItems(BillList);
    }

    public void handleTableView(MouseEvent mouseEvent) {
        Bills selected = billsTableView.getSelectionModel().getSelectedItem();
        if(selected != null){
            // hiển thị form chi tiết hóa đơn
            try {
                // Đóng cửa sổ hiện tại
//                ((Stage) usernameField.getScene().getWindow()).close();
                // Khởi chạy dashboard
                BillItemsView billItemsView = new BillItemsView();
                Stage stage = new Stage();
                billItemsView.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
