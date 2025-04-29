package com.utc2.apartmentmanagement.Utils;

import com.utc2.apartmentmanagement.Model.Apartment;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;

public class ValidateColumn {
    public <S> void formatDoubleColumn(TableColumn<S, Double> column) {
        column.setCellFactory(col -> new TableCell<S, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", item)); // giữ 2 số thập phân
                }
            }
        });
    }


}
