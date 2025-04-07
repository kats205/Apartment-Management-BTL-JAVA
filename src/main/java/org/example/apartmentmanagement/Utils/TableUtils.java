package org.example.apartmentmanagement.Utils;

import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import org.example.apartmentmanagement.Model.Apartment;

import java.text.DecimalFormat;

public class TableUtils {
    public static <T, V> TableCell<T, V> createCenteredCell() {
        return new TableCell<T, V>() {
            @Override
            protected void updateItem(V item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString()); // Hiển thị giá trị dưới dạng chuỗi
                    setAlignment(Pos.CENTER); // Căn giữa nội dung
                }
            }
        };
    }
    public static void formatColPrice(TableColumn<Apartment, Double> colPrice){
        colPrice.setCellFactory(column -> new TextFieldTableCell<Apartment, Double>() {
            private final DecimalFormat df = new DecimalFormat("#,##0"); // Định dạng số nguyên (không có E)

            @Override
            public void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(df.format(item)); // Hiển thị số không có ký hiệu khoa học
                }
            }
        });
    }

    public static void formatArea(TableColumn<Apartment, Double> colArea){
        colArea.setCellFactory(column -> new TableCell<Apartment, Double>() {
            private final DecimalFormat df = new DecimalFormat("#,##0.00"); // Làm tròn 2 chữ số

            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(df.format(item));
                }
            }
        });
    }
}
