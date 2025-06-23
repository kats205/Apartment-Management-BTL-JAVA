package com.utc2.apartmentmanagement.Utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import java.util.List;
import java.util.Map;

public class PaginationUtils {
    private static int rowsPerPage = 10;
    public static <T> void setupPagination(
            List<T> fullList,
            TableView<T> tableView,
            Pagination pagination,
            Label countLabel,
            Label noContentLabel
    ) {
        int totalRows = fullList.size();
        int pageCount = (int) Math.ceil((double) totalRows / rowsPerPage);

        pagination.setPageCount(pageCount == 0 ? 1 : pageCount); // Tránh 0 trang
        pagination.setCurrentPageIndex(0);

        pagination.setPageFactory(pageIndex -> {
            int fromIndex = pageIndex * rowsPerPage;
            int toIndex = Math.min(fromIndex + rowsPerPage, totalRows);

            List<T> subList = fullList.subList(fromIndex, toIndex);
            ObservableList<T> currentPageData = FXCollections.observableArrayList(subList);
            tableView.setItems(currentPageData);

            if (noContentLabel != null) {
                noContentLabel.setVisible(currentPageData.isEmpty());
            }

            if (countLabel != null) {
                countLabel.setText(String.valueOf(totalRows));
            }

            return new AnchorPane(); // Không bắt buộc phải trả về TableView
        });
    }

    public static <T> void setupPagination(
            List<T> fullList,
            TableView<T> tableView,
            Pagination pagination
    ) {
        int totalRows = fullList.size();
        int pageCount = (int) Math.ceil((double) totalRows / rowsPerPage);

        pagination.setPageCount(pageCount == 0 ? 1 : pageCount); // Tránh 0 trang
        pagination.setCurrentPageIndex(0);

        pagination.setPageFactory(pageIndex -> {
            int fromIndex = pageIndex * rowsPerPage;
            int toIndex = Math.min(fromIndex + rowsPerPage, totalRows);

            List<T> subList = fullList.subList(fromIndex, toIndex);
            ObservableList<T> currentPageData = FXCollections.observableArrayList(subList);
            tableView.setItems(currentPageData);
            return new AnchorPane(); // Không bắt buộc phải trả về TableView
        });
    }
    public static <T> void setupPagination(
            List<T> fullList,
            TableView<T> tableView,
            Pagination pagination,
            Label countLabel
    ) {
        int totalRows = fullList.size();
        int pageCount = (int) Math.ceil((double) totalRows / rowsPerPage);

        pagination.setPageCount(pageCount == 0 ? 1 : pageCount); // Tránh 0 trang
        pagination.setCurrentPageIndex(0);

        pagination.setPageFactory(pageIndex -> {
            int fromIndex = pageIndex * rowsPerPage;
            int toIndex = Math.min(fromIndex + rowsPerPage, totalRows);

            List<T> subList = fullList.subList(fromIndex, toIndex);
            ObservableList<T> currentPageData = FXCollections.observableArrayList(subList);
            tableView.setItems(currentPageData);
            if (countLabel != null) {
                countLabel.setText(String.valueOf(totalRows));
            }
            return new AnchorPane(); // Không bắt buộc phải trả về TableView
        });
    }


}
