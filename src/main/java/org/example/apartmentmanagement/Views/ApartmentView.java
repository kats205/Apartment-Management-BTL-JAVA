package org.example.apartmentmanagement.Views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.example.apartmentmanagement.DAO.ApartmentDAO;
import org.example.apartmentmanagement.Model.Apartment;
import org.example.apartmentmanagement.Utils.TableUtils;
import java.util.List;

public class ApartmentView {
    public Parent getView() {
        TableView<Apartment> table = new TableView<>();
        TableColumn<Apartment, Integer> colApartmentId = new TableColumn<>("Apartment ID");
        colApartmentId.setCellValueFactory(new PropertyValueFactory<>("apartmentID"));
        colApartmentId.setPrefWidth(100);
        colApartmentId.setStyle("-fx-font-size: 16px; -fx-alignment: CENTER;");

        TableColumn<Apartment, Integer> colBuildingId = new TableColumn<>("Building ID");
        colBuildingId.setCellValueFactory(new PropertyValueFactory<>("buildingID"));
        colBuildingId.setPrefWidth(100);
        colBuildingId.setCellFactory(tc -> TableUtils.createCenteredCell());
        colBuildingId.setStyle("-fx-font-size: 16px; -fx-alignment: CENTER;");

        TableColumn<Apartment, String> colFloor = new TableColumn<>("Floor");
        colFloor.setCellValueFactory(new PropertyValueFactory<>("floors"));
        colFloor.setPrefWidth(100);
        colFloor.setStyle("-fx-font-size: 16px; -fx-alignment: CENTER;");

        TableColumn<Apartment, Double> colPrice = new TableColumn<>("Price (vnđ)");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("priceApartment"));
        colPrice.setPrefWidth(150);
        colPrice.setStyle("-fx-font-size: 16px; -fx-alignment: CENTER;");
        // Định dạng cột Price (tách riêng để không bị ảnh hưởng bởi vòng lặp)
        TableUtils.formatColPrice(colPrice);

        TableColumn<Apartment, Double> colArea = new TableColumn<>("Area (m²)");
        colArea.setCellValueFactory(new PropertyValueFactory<>("area"));
        colArea.setPrefWidth(150);
        colArea.setStyle("-fx-font-size: 16px; -fx-alignment: CENTER;");
        TableUtils.formatArea(colArea);

        TableColumn<Apartment, Integer> colBedrooms = new TableColumn<>("Bedrooms");
        colBedrooms.setCellValueFactory(new PropertyValueFactory<>("bedRoom"));
        colBedrooms.setPrefWidth(100);
        colBedrooms.setStyle("-fx-font-size: 16px; -fx-alignment: CENTER;");

        TableColumn<Apartment, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colStatus.setPrefWidth(150);
        colStatus.setStyle("-fx-font-size: 16px; -fx-alignment: CENTER;");

        TableColumn<Apartment, Double> colMaintenanceFee = new TableColumn<>("Maintenance Fee");
        colMaintenanceFee.setCellValueFactory(new PropertyValueFactory<>("maintanceFee"));
        colMaintenanceFee.setPrefWidth(200);
        colMaintenanceFee.setStyle("-fx-font-size: 16px; -fx-alignment: CENTER;");

        // thêm tất cả các cột vào table
        table.getColumns().addAll(colApartmentId, colBuildingId, colFloor, colPrice, colArea, colBedrooms, colStatus, colMaintenanceFee);

        // Lấy dữ liệu từ Database
        ObservableList<Apartment> apartmentList = FXCollections.observableArrayList();
        List<Apartment> apartmentList1 = new ApartmentDAO().getAllApartments();
        apartmentList.addAll(apartmentList1);
        table.setItems(apartmentList);

        VBox vbox = new VBox(table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return vbox;
    }


}
