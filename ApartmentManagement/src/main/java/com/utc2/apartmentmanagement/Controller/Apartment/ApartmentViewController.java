package com.utc2.apartmentmanagement.Controller.Apartment;

import com.utc2.apartmentmanagement.Controller.DashboardController;
import com.utc2.apartmentmanagement.DAO.Apartment.ApartmentDAO;
import com.utc2.apartmentmanagement.DAO.Report.ReportDAO;
import com.utc2.apartmentmanagement.DAO.User.UserDAO;
import com.utc2.apartmentmanagement.Model.Apartment.Apartment;
import com.utc2.apartmentmanagement.Model.PDF_Export;
import com.utc2.apartmentmanagement.Model.Report.Report;
import com.utc2.apartmentmanagement.Model.Session;
import com.utc2.apartmentmanagement.Utils.AlertBox;
import com.utc2.apartmentmanagement.Utils.PaginationUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class ApartmentViewController implements Initializable {
    @FXML
    public AnchorPane apartmentView;
    @FXML public Pagination pagination;
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
    private Button exportButton;

    @Setter
    private DashboardController parentController;

    // Getter cho nút đóng để DashboardController có thể truy cập
    @Getter
    @FXML
    private Button closeButton;

    private static final int ROWS_PER_PAGE = 10;
    private ContextMenu currentContextMenu;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Tải dữ liệu
        loadData();

        // load combobox status
        loadStatusCB();

        // load combobox id
        loadIdCB();

        // load data apartment
        loadDataApartment();

    }

    private void getValueCol() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("apartmentID"));
        idColumn.setStyle("-fx-font-size: 14px; -fx-alignment: CENTER;");
        idColumn.setPrefWidth(140);

        buildingColumn.setCellValueFactory(new PropertyValueFactory<>("buildingID"));
        buildingColumn.setStyle("-fx-font-size: 14px; -fx-alignment: CENTER;");
        buildingColumn.setPrefWidth(100);

        floorColumn.setCellValueFactory(new PropertyValueFactory<>("floors"));
        floorColumn.setStyle("-fx-font-size: 14px; -fx-alignment: CENTER;");
        floorColumn.setPrefWidth(100);

        areaColumn.setCellValueFactory(new PropertyValueFactory<>("area"));
        areaColumn.setStyle("-fx-font-size: 14px; -fx-alignment: CENTER;");
        areaColumn.setPrefWidth(140);

        areaColumn.setCellFactory(column -> new TableCell<>() {
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
        bedroomsColumn.setStyle("-fx-font-size: 14px; -fx-alignment: CENTER;");
        bedroomsColumn.setPrefWidth(140);

        priceColumn.setCellValueFactory(new PropertyValueFactory<>("priceApartment"));
        priceColumn.setStyle("-fx-font-size: 14px; -fx-alignment: CENTER;");
        priceColumn.setPrefWidth(200);

        priceColumn.setCellFactory(column -> new TableCell<>() {
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
        statusColumn.setStyle("-fx-font-size: 14px; -fx-alignment: CENTER;");
        statusColumn.setPrefWidth(200);

        maintenanceFeeCol.setCellValueFactory(new PropertyValueFactory<>("maintanceFee"));
        maintenanceFeeCol.setStyle("-fx-font-size: 14px; -fx-alignment: CENTER;");
        maintenanceFeeCol.setPrefWidth(250);
    }

    void loadDataApartment() {
        getValueCol();
        List<Apartment> apartmentList = new ApartmentDAO().getAllApartments();
        loadTableListView(apartmentList);

    }

    private void loadTableListView(List<Apartment> apartmentList) {
        ObservableList<Apartment> apartmentListOb = FXCollections.observableArrayList();
        apartmentListOb.addAll(apartmentList);
        apartmentTable.setItems(apartmentListOb);
        PaginationUtils.setupPagination(
                apartmentList,
                apartmentTable,
                pagination,
                apartmentCountLabel,
                noContentLabel
        );
        apartmentCountLabel.setText(String.valueOf(apartmentList.size()));
    }

    private void loadTableView(Apartment apartment) {
        ObservableList<Apartment> apartmentList = FXCollections.observableArrayList();
        apartmentList.add(apartment);
        apartmentTable.setItems(apartmentList);
        apartmentCountLabel.setText("1");
        PaginationUtils.setupPagination(
                apartmentList,
                apartmentTable,
                pagination,
                apartmentCountLabel,
                noContentLabel
        );
    }

    private void loadIdCB() {
        buildingComboBox.getItems().clear();
        buildingComboBox.getItems().add("Chọn tòa nhà");

        List<String> apartmentIdList = new ApartmentDAO().getAllIdApartment();
        buildingComboBox.getItems().addAll(apartmentIdList);
        buildingComboBox.getSelectionModel().selectFirst(); // Chọn "Chọn tòa nhà"
    }

    private void loadStatusCB() {
        statusComboBox.getItems().clear();
        statusComboBox.getItems().add("Chọn trạng thái"); // Thêm lựa chọn mặc định

        List<String> statusList = new ApartmentDAO().getAllStatus();
        statusComboBox.getItems().addAll(statusList);
        statusComboBox.getSelectionModel().selectFirst(); // Chọn "Chọn trạng thái"

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
        buildingComboBox.getSelectionModel().selectFirst();
        statusComboBox.getSelectionModel().selectFirst();
        loadData();

    }

    private void addNewApartment() {
        // TODO: Mở form thêm căn hộ mới
    }

    private void viewApartmentDetails() {
        // TODO: Hiển thị chi tiết căn hộ được chọn
    }


    @FXML
    private void exportReport() {
        // TODO: Xuất danh sách căn hộ thành PDF
        try {
            String filePath = PDF_Export.exportApartmentList("Apartment_List.pdf");
            int user_id = new UserDAO().getIdByUserName(Session.getUserName());
            System.out.println(user_id);
            LocalDateTime dateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String formattedDateTime = dateTime.format(formatter);
            Report report = new Report("Bao cao can ho", LocalDate.now(), user_id, formattedDateTime, filePath, LocalDate.now(), LocalDate.now());
            new ReportDAO().saveReport(report);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText("Xuất file PDF thành công!");
            alert.setContentText("Đã lưu tại:\n" + filePath);
            alert.showAndWait();

            System.out.println("PDF exported to: " + filePath);

        } catch (Exception e) {
            // Nếu lỗi thì thông báo lỗi
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText("Xuất file thất bại");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void updateApartmentCount() {
        // TODO: Cập nhật số lượng căn hộ dựa trên dữ liệu hiện tại
        // Giả sử hiện tại có 0 căn hộ
        apartmentCountLabel.setText("0");

        // Hiển thị/ẩn nhãn không có dữ liệu
        boolean hasData = false; // TODO: Kiểm tra có dữ liệu hay không
        noContentLabel.setVisible(!hasData);
        loadDataApartment();
    }

    public void handleButtonSearch(ActionEvent event) {
        String apartmentId = buildingComboBox.getValue();
        if ("Chọn tòa nhà".equals(apartmentId)) {
            apartmentId = null;
        }

        String status = statusComboBox.getValue();
        if ("Chọn trạng thái".equals(status)) {
            status = null;
        }

        if (apartmentId != null && status == null) {
            Apartment apartment = new ApartmentDAO().findApartmentById(apartmentId);
            loadTableView(apartment);
        } else if (apartmentId == null && status != null) {
            List<Apartment> apartmentList = new ApartmentDAO().findApartmentByStatus(status);
            loadTableListView(apartmentList);
        } else {
            Apartment apartment = new ApartmentDAO().findApartmentByIdAndStatus(apartmentId, status);
            if (apartment == null) {
                AlertBox.showAlertForUser("Thông báo", "Không tìm thấy căn hộ nào với ID và trạng thái đã chọn");
            } else {
                loadTableView(apartment);
            }
        }

    }

    public void handleRefresh(ActionEvent event) {
        // Xóa các lựa chọn trong ComboBox
        buildingComboBox.getSelectionModel().clearSelection();
        statusComboBox.getSelectionModel().clearSelection();
        buildingComboBox.setValue("Chọn tòa nhà");
        statusComboBox.setValue("Chọn trạng thái");
        // Tải lại dữ liệu
        loadData();
    }

    public void handleCloseButton(ActionEvent event) {
        // Xoá apartment view
        ((Pane) apartmentView.getParent()).getChildren().clear();
        // Thêm lại dashboard nodes từ controller cha
        parentController.getContentArea().getChildren().setAll(parentController.getDashboardNodes());
    }


    public void getSelectedApartment(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            Apartment selectedApartment = apartmentTable.getSelectionModel().getSelectedItem();
            if (selectedApartment == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng chọn căn hộ để chỉnh sửa!");
                alert.showAndWait();
                return;
            }

            // Nếu đã có ContextMenu đang mở -> đóng lại
            if (currentContextMenu != null && currentContextMenu.isShowing()) {
                currentContextMenu.hide();
            }

            // Tạo ContextMenu mới
            ContextMenu contextMenu = new ContextMenu();
            MenuItem editItem = new MenuItem("Chỉnh sửa căn hộ");

            editItem.setOnAction(e -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/utc2/apartmentmanagement/fxml/Apartment/EditApartmentView.fxml"));
                    Parent root = loader.load();

                    EditApartmentViewController editController = loader.getController();
                    editController.setParentController(this);   // set controller cha
                    editController.setApartment(selectedApartment); // set căn hộ cần edit

                    Stage stage = new Stage();
                    stage.setTitle("Chỉnh sửa căn hộ");
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            contextMenu.getItems().add(editItem);

            // Lưu context menu mới
            currentContextMenu = contextMenu;

            // Hiện ContextMenu tại vị trí con trỏ
            contextMenu.show(apartmentTable, mouseEvent.getScreenX(), mouseEvent.getScreenY());

            // Thêm listener để ẩn ContextMenu nếu click chỗ khác
            apartmentTable.getScene().addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                // Ẩn menu nếu click trái hoặc phải ở nơi khác
                if (currentContextMenu != null && currentContextMenu.isShowing()
                        && (event.getButton() == MouseButton.PRIMARY || event.getButton() == MouseButton.SECONDARY)) {
                    currentContextMenu.hide();
                }
            });
        }
    }
}