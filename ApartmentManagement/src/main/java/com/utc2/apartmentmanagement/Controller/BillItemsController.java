package com.utc2.apartmentmanagement.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.example.apartmentmanagement.DAO.BillItemDAO;
import org.example.apartmentmanagement.Model.BillItems;
import org.example.apartmentmanagement.Model.Bills;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class BillItemsController implements Initializable {

    @FXML
    public TableColumn<BillItems,Integer> itemIdCol;
    @FXML
    public TableColumn<BillItems, Integer> billIdCol;
    @FXML
    public TableColumn<BillItems, String> billTypeCol;
    @FXML
    public TableColumn<BillItems, String> descriptionCol;
    @FXML
    public TableColumn<BillItems, Integer> amountCol;
    @FXML
    public TableColumn<BillItems, Integer> quantityCol;
    @FXML
    public TableColumn<BillItems, Double> totalCol;
    @FXML
    public TableColumn<BillItems, LocalDate> createDateCol;
    @FXML
    public TableColumn<BillItems, LocalDate> updateDateCol;
    @FXML
    private ComboBox<String> billSelectionComboBox;

    @FXML
    private Label billIdLabel;

    @FXML
    private Label apartmentIdLabel;

    @FXML
    private Label billingDateLabel;

    @FXML
    private Label dueDateLabel;

    @FXML
    private TableView<BillItems> billItemsTableView;


    @FXML
    private Label totalBillAmountLabel;

    @FXML
    private Label totalLateFeeLabel;

    @FXML
    private Label finalAmountLabel;


    public void loadBillDetails(ActionEvent actionEvent) {


    }


    public void getView(){
        itemIdCol.setCellValueFactory(new PropertyValueFactory<>("itemID"));
        itemIdCol.setStyle("Font-size: 14px ; -fx-alignment: CENTER;");
        itemIdCol.setPrefWidth(150);

        billIdCol.setCellValueFactory(new PropertyValueFactory<>("billID"));
        billIdCol.setStyle("Font-size: 14px ; -fx-alignment: CENTER;");
        billIdCol.setPrefWidth(150);

        billTypeCol.setCellValueFactory(new PropertyValueFactory<>("itemType"));
        billTypeCol.setStyle("Font-size: 14px ; -fx-alignment: CENTER;");
        billTypeCol.setPrefWidth(100);

        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        descriptionCol.setStyle("Font-size: 14px ; -fx-alignment: CENTER;");
        descriptionCol.setPrefWidth(150);

        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountCol.setStyle("Font-size: 14px ; -fx-alignment: CENTER;");
        amountCol.setPrefWidth(100);

        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityCol.setStyle("Font-size: 14px ; -fx-alignment: CENTER;");
        quantityCol.setPrefWidth(100);

        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
        totalCol.setStyle("Font-size: 14px ; -fx-alignment: CENTER;");
        totalCol.setPrefWidth(100);

        createDateCol.setCellValueFactory(new PropertyValueFactory<>("created_at"));
        createDateCol.setStyle("Font-size: 14px ; -fx-alignment: CENTER;");
        createDateCol.setPrefWidth(100);

        updateDateCol.setCellValueFactory(new PropertyValueFactory<>("updated_at"));
        updateDateCol.setStyle("Font-size: 14px ; -fx-alignment: CENTER;");
        updateDateCol.setPrefWidth(100);

        List<BillItems> Bills = new BillItemDAO().getAllBillItems();
        getBillItemsList(Bills);

    }
    public void getBillItemsList(List<BillItems> billList){
        ObservableList<BillItems> BillList = FXCollections.observableArrayList();
        List<BillItems> Bills = billList;
        BillList.addAll(Bills);
        billItemsTableView.setItems(BillList);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getView();
        List<Integer> itemIdList = new BillItemDAO().getAllItemId();
        for(int i : itemIdList){
            billSelectionComboBox.getItems().add(String.valueOf(i));
        }
    }


    public void setValueForBillItems(BillItems billItems){
        billIdLabel.setText(String.valueOf(billItems.getBillID()));
//        String apartmentId = billItems.getApartmentID();
//        apartmentIdLabel.setText(billItems.);
    }

    public void handleTableView(MouseEvent mouseEvent) {
        BillItems selected = billItemsTableView.getSelectionModel().getSelectedItem();
        if(selected != null){

        }
    }
}
