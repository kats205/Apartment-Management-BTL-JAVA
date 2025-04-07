package org.example.apartmentmanagement.Utils;

import javafx.geometry.Pos;
import javafx.scene.control.TableCell;

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
}
