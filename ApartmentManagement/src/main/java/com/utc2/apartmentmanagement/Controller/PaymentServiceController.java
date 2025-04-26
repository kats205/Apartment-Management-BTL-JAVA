package com.utc2.apartmentmanagement.Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import org.example.apartmentmanagement.DAO.DatabaseConnection;
import org.example.apartmentmanagement.Model.BillItems;
import org.example.apartmentmanagement.Model.Bills;
import org.example.apartmentmanagement.Model.Payment;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class PaymentServiceController implements Initializable {
    @FXML private ComboBox<String> apartmentComboBox;
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

    // Tab Chi tiết hóa đơn
    @FXML private ComboBox<Bills> billSelectionComboBox;
    @FXML private Label billIdLabel;
    @FXML private Label apartmentIdLabel;
    @FXML private Label billingDateLabel;
    @FXML private Label dueDateLabel;
    @FXML private TableView<BillItems> billItemsTableView;
    @FXML private TableColumn<BillItems, String> itemTypeColumn;
    @FXML private TableColumn<BillItems, String> descriptionColumn;
    @FXML private TableColumn<BillItems, Double> amountColumn;
    @FXML private TableColumn<BillItems, Double> quantityColumn;
    @FXML private TableColumn<BillItems, Double> totalColumn;
    @FXML private Label totalBillAmountLabel;
    @FXML private Label totalLateFeeLabel;
    @FXML private Label finalAmountLabel;

    // Tab Thanh toán
    @FXML
    private ComboBox<Bills> paymentBillComboBox;
    @FXML private Label amountDueLabel;
    @FXML private TextField paymentAmountField;
    @FXML private ComboBox<String> paymentMethodComboBox;
    @FXML private TextField transactionIdField;
    @FXML private TableView<Payment> paymentHistoryTableView;
    @FXML private TableColumn<Payment, Integer> paymentIdColumn;
    @FXML private TableColumn<Payment, Double> paymentAmountColumn;
    @FXML private TableColumn<Payment, LocalDate> paymentDateColumn;
    @FXML private TableColumn<Payment, String> paymentMethodColumn;
    @FXML private TableColumn<Payment, String> transactionIdColumn;
    @FXML private TableColumn<Payment, String> paymentStatusColumn;

    @FXML private Label statusLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeComponents();
        loadApartments();
        loadStatusOptions();
        loadBills();
    }

    private void loadBills() {
    }

    private void initializeComponents() {
        // Khởi tạo các combobox
        statusComboBox.setItems(FXCollections.observableArrayList(
                "Tất cả", "pending", "paid", "overdue", "cancelled"
        ));
        statusComboBox.setValue("Tất cả");

        paymentMethodComboBox.setItems(FXCollections.observableArrayList(
                "Tiền mặt", "Chuyển khoản", "Thẻ tín dụng", "Ví điện tử"
        ));
        paymentMethodComboBox.setValue("Tiền mặt");

        // Khởi tạo các bảng
        initializeBillsTable();
        initializeBillItemsTable();
        initializePaymentHistoryTable();
    }

    private void initializeBillsTable() {
        billIdColumn.setCellValueFactory(new PropertyValueFactory<>("billId"));
        apartmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("apartmentId"));
        billingDateColumn.setCellValueFactory(new PropertyValueFactory<>("billingDate"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        totalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        lateFeeColumn.setCellValueFactory(new PropertyValueFactory<>("lateFee"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Format columns
        billingDateColumn.setCellFactory(column -> new TableCell<Bills, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
//                    setText(dateFormatter.format(item));
                }
            }
        });

        dueDateColumn.setCellFactory(column -> new TableCell<Bills, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
//                    setText(dateFormatter.format(item));
                }
            }
        });

        totalAmountColumn.setCellFactory(column -> new TableCell<Bills, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(String.format("%,.0f VNĐ", item));
                }
            }
        });

        lateFeeColumn.setCellFactory(column -> new TableCell<Bills, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(String.format("%,.0f VNĐ", item));
                }
            }
        });

        statusColumn.setCellFactory(column -> new TableCell<Bills, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    switch (item) {
                        case "paid":
                            setText("Đã thanh toán");
                            setStyle("-fx-text-fill: green;");
                            break;
                        case "pending":
                            setText("Chờ thanh toán");
                            setStyle("-fx-text-fill: blue;");
                            break;
                        case "overdue":
                            setText("Quá hạn");
                            setStyle("-fx-text-fill: red;");
                            break;
                        case "cancelled":
                            setText("Đã hủy");
                            setStyle("-fx-text-fill: gray;");
                            break;
                        default:
                            setText(item);
                            setStyle("");
                    }
                }
            }
        });

        // Add action buttons
        actionColumn.setCellFactory(param -> new TableCell<Bills, Void>() {
            private final Button viewButton = new Button("Xem");
            private final Button payButton = new Button("Thanh toán");

            {
                viewButton.setOnAction(event -> {
                    Bills bill = getTableView().getItems().get(getIndex());
//                    viewBillDetails(bill);
                });

                payButton.setOnAction(event -> {
                    Bills bill = getTableView().getItems().get(getIndex());
//                    setupPaymentForBill(bill);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Bills bill = getTableView().getItems().get(getIndex());
                    HBox buttons = new HBox(5);
                    buttons.getChildren().add(viewButton);

                    if ("pending".equals(bill.getStatus()) || "overdue".equals(bill.getStatus())) {
                        buttons.getChildren().add(payButton);
                    }

                    setGraphic(buttons);
                }
            }
        });

//        billsTableView.setItems(billsList);
    }

    private void initializeBillItemsTable() {
        itemTypeColumn.setCellValueFactory(new PropertyValueFactory<>("itemType"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

        // Format columns
        amountColumn.setCellFactory(column -> new TableCell<BillItems, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(String.format("%,.0f VNĐ", item));
                }
            }
        });

        totalColumn.setCellFactory(column -> new TableCell<BillItems, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(String.format("%,.0f VNĐ", item));
                }
            }
        });

//        billItemsTableView.setItems(billItemsList);
    }

    private void initializePaymentHistoryTable() {
        paymentIdColumn.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        paymentAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        paymentDateColumn.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        paymentMethodColumn.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        transactionIdColumn.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        paymentStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Format columns
        paymentAmountColumn.setCellFactory(column -> new TableCell<Payment, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(String.format("%,.0f VNĐ", item));
                }
            }
        });

        paymentDateColumn.setCellFactory(column -> new TableCell<Payment, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
//                    setText(dateFormatter.format(item));
                }
            }
        });

        paymentStatusColumn.setCellFactory(column -> new TableCell<Payment, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    switch (item) {
                        case "completed":
                            setText("Hoàn thành");
                            setStyle("-fx-text-fill: green;");
                            break;
                        case "pending":
                            setText("Đang xử lý");
                            setStyle("-fx-text-fill: blue;");
                            break;
                        case "failed":
                            setText("Thất bại");
                            setStyle("-fx-text-fill: red;");
                            break;
                        case "refunded":
                            setText("Hoàn tiền");
                            setStyle("-fx-text-fill: orange;");
                            break;
                        default:
                            setText(item);
                            setStyle("");
                    }
                }
            }
        });

//        paymentHistoryTableView.setItems(paymentsList);
    }

    private void loadApartments() {
        ObservableList<String> apartments = FXCollections.observableArrayList();
        apartments.add("Tất cả");

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT apartment_id FROM Apartment");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                apartments.add(rs.getString("apartment_id"));
            }

            apartmentComboBox.setItems(apartments);
            apartmentComboBox.setValue("Tất cả");

        } catch (SQLException e) {
            AlertHelper("Không thể tải danh sách căn hộ");
        }
    }
    private static void AlertHelper(String message) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.show();
    }
    private void loadStatusOptions() {
        // Đã khởi tạo trong initializeComponents()
    }

    public void searchBills(ActionEvent actionEvent) {
    }

    public void refreshBills(ActionEvent actionEvent) {
    }

    public void createNewBill(ActionEvent actionEvent) {
    }

    public void loadBillDetails(ActionEvent actionEvent) {
    }

    public void loadSelectedBillForPayment(ActionEvent actionEvent) {
    }

    public void cancelPayment(ActionEvent actionEvent) {
    }

    public void processPayment(ActionEvent actionEvent) {
    }

    public void exitApplication(ActionEvent actionEvent) {
    }
}
