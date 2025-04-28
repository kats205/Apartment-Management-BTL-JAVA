package com.utc2.apartmentmanagement.Controller;

import com.utc2.apartmentmanagement.DAO.ApartmentDAO;
import com.utc2.apartmentmanagement.Model.Apartment;
import com.utc2.apartmentmanagement.Utils.AlertBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class ApartmentViewController implements Initializable {
    @FXML
    public AnchorPane apartmentView;
    @FXML
    private ComboBox<String> buildingComboBox;

    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private Button searchButton;

    @FXML
    private Button refreshButton;

    @FXML
    private Button addApartmentButton;

    @FXML
    private TableView<Apartment> apartmentTable;

    @FXML
    private TableColumn<Apartment, String> idColumn;

    @FXML
    private TableColumn<Apartment, Integer> buildingColumn;

    @FXML
    private TableColumn<Apartment, Integer> floorColumn;

    @FXML
    private TableColumn<Apartment, Double> areaColumn;

    @FXML
    private TableColumn<Apartment, Integer> bedroomsColumn;

    @FXML
    private TableColumn<Apartment, Integer> bathroomsColumn;

    @FXML
    private TableColumn<Apartment, Double> priceColumn;

    @FXML
    private TableColumn<Apartment, String> statusColumn;

    @FXML
    private TableColumn<Apartment, Double> maintenanceFeeCol;

    @FXML
    private Label noContentLabel;

    @FXML
    private Label apartmentCountLabel;

    @FXML
    private Button detailButton;

    @FXML
    private Button editButton;

    @FXML
    private Button exportButton;

    @FXML
    private Button closeButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Khởi tạo các thành phần UI
//        initializeComponents();

        // Thiết lập các sự kiện
//        setupEventHandlers();

        // Tải dữ liệu
        loadData();

        // load combobox status
        loadStatusCB();

        // load combobox id
        loadIdCB();

        // load data apartment
        loadDataApartment();

        int totalCount;
        try {
            totalCount = new ApartmentDAO().countApartment();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        apartmentCountLabel.setText(String.valueOf(totalCount));
    }
    private void getValueCol(){
        idColumn.setCellValueFactory(new PropertyValueFactory<>("apartmentID"));
        idColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");
        idColumn.setPrefWidth(130);

        buildingColumn.setCellValueFactory(new PropertyValueFactory<>("buildingID"));
        buildingColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");
        buildingColumn.setPrefWidth(100);

        floorColumn.setCellValueFactory(new PropertyValueFactory<>("floors"));
        floorColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");
        floorColumn.setPrefWidth(100);

        areaColumn.setCellValueFactory(new PropertyValueFactory<>("area"));
        areaColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");
        areaColumn.setPrefWidth(130);

        areaColumn.setCellFactory(column -> new TableCell<Apartment, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", item)); // giữ lại 2 số thập phân
                }
            }
        });

        bedroomsColumn.setCellValueFactory(new PropertyValueFactory<>("bedRoom"));
        bedroomsColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");
        bedroomsColumn.setPrefWidth(130);

        priceColumn.setCellValueFactory(new PropertyValueFactory<>("priceApartment"));
        priceColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");
        priceColumn.setPrefWidth(150);

        priceColumn.setCellFactory(column -> new TableCell<Apartment, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
                    formatter.setMaximumFractionDigits(2); // tối đa 2 số thập phân
                    formatter.setMinimumFractionDigits(2); // luôn luôn có 2 số thập phân
                    setText(formatter.format(item)); // format dạng 1,510,000,000
                }
            }
        });

        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");
        statusColumn.setPrefWidth(140);

        maintenanceFeeCol.setCellValueFactory(new PropertyValueFactory<>("maintanceFee"));
        maintenanceFeeCol.setStyle("-fx-alignment: CENTER; -fx-font-size: 14px;");
        maintenanceFeeCol.setPrefWidth(120);
    }
    private void loadDataApartment(){
        getValueCol();

        List<Apartment> apartmentList = new ApartmentDAO().getAllApartments();
        loadTableListView(apartmentList);
    }
    private void loadTableListView(List<Apartment> apartmentList){
        ObservableList<Apartment> apartmentListOb = FXCollections.observableArrayList();
        List<Apartment> apartmentList1 = apartmentList;
        apartmentListOb.addAll(apartmentList1);
        apartmentTable.setItems(apartmentListOb);
    }

    private void loadTableView(Apartment apartment){
        ObservableList<Apartment> apartmentList = FXCollections.observableArrayList();
        apartmentList.add(apartment);
        apartmentTable.setItems(apartmentList);
    }

    private void loadIdCB(){
        List<String> apartmentIdList = new ApartmentDAO().getAllIdApartment();
        for(String s : apartmentIdList){
            buildingComboBox.getItems().add(s);
        }
    }

    private void loadStatusCB(){
        List<String> statusList = new ApartmentDAO().getAllStatus();
        for(String s : statusList){
            statusComboBox.getItems().add(s);
        }

    }

    private void initializeComponents() {
        // Khởi tạo các ComboBox
        initializeComboBoxes();

        // Khởi tạo TableView
        initializeTableView();

    }

    private void initializeComboBoxes() {
        // TODO: Khởi tạo dữ liệu cho buildingComboBox

        // TODO: Khởi tạo dữ liệu cho statusComboBox
    }

    private void initializeTableView() {
        // TODO: Cấu hình TableView và các cột
    }

    private void setupEventHandlers() {
        // Xử lý sự kiện tìm kiếm
        searchButton.setOnAction(event -> searchApartments());

        // Xử lý sự kiện làm mới
        refreshButton.setOnAction(event -> refreshData());

        // Xử lý sự kiện thêm căn hộ mới
        addApartmentButton.setOnAction(event -> addNewApartment());

        // Xử lý sự kiện xem chi tiết
        detailButton.setOnAction(event -> viewApartmentDetails());

        // Xử lý sự kiện chỉnh sửa
        editButton.setOnAction(event -> editApartment());

        // Xử lý sự kiện xuất báo cáo
        exportButton.setOnAction(event -> exportReport());

        // Nút đóng mặc định không làm gì - sẽ được xử lý bởi DashboardController
    }

    private void loadData() {
        // TODO: Tải dữ liệu căn hộ từ nguồn dữ liệu

        // Hiển thị số lượng căn hộ
        updateApartmentCount();
    }

    private void searchApartments() {
        // TODO: Thực hiện tìm kiếm căn hộ dựa trên điều kiện

        // Cập nhật bảng và số lượng
        updateApartmentCount();
    }

    private void refreshData() {
        // Xóa các lựa chọn trong ComboBox
        buildingComboBox.getSelectionModel().clearSelection();
        statusComboBox.getSelectionModel().clearSelection();

        // Tải lại dữ liệu
        loadData();
    }

    private void addNewApartment() {
        // TODO: Mở form thêm căn hộ mới
    }

    private void viewApartmentDetails() {
        // TODO: Hiển thị chi tiết căn hộ được chọn
    }

    private void editApartment() {
        // TODO: Mở form chỉnh sửa căn hộ được chọn
    }

    private void exportReport() {
        // TODO: Xuất báo cáo danh sách căn hộ
    }

    private void updateApartmentCount() {
        // TODO: Cập nhật số lượng căn hộ dựa trên dữ liệu hiện tại
        // Giả sử hiện tại có 0 căn hộ
        apartmentCountLabel.setText("0");

        // Hiển thị/ẩn nhãn không có dữ liệu
        boolean hasData = false; // TODO: Kiểm tra có dữ liệu hay không
        noContentLabel.setVisible(!hasData);
    }

    // Getter cho nút đóng để DashboardController có thể truy cập
    public Button getCloseButton() {
        return closeButton;
    }

    public void handleButtonSearch(ActionEvent event) {
        String apartmentId = buildingComboBox.getValue();
        System.out.println("Apartment ID: " + apartmentId);
        String status = statusComboBox.getValue();
        System.out.println("Status: " + status);

        if(apartmentId != null && status == null) {
            Apartment apartment = new ApartmentDAO().findApartmentById(apartmentId);
            loadTableView(apartment);
        }
        else if(apartmentId == null && status != null){
            List<Apartment> apartmentList = new ApartmentDAO().findApartmentByStatus(status);
            loadTableListView(apartmentList);
        }
        else{
            Apartment apartment = new ApartmentDAO().findApartmentByIdAndStatus(apartmentId, status);
            if(apartment == null){
                new AlertBox().showAlertForUser("Thông báo", "Không tìm thấy căn hộ nào với ID và trạng thái đã chọn");
            }
            else{
                loadTableView(apartment);
            }
        }

    }

    public void handleRefresh(ActionEvent event) {
        loadDataApartment();
    }

    public void handleCloseButton(ActionEvent event) {
        // Đóng cửa sổ hiện tại
        apartmentView.setVisible(false);
    }
}