package com.utc2.apartmentmanagement.Views;

import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TableViewPaginationExample extends Application {

    private final static int ROWS_PER_PAGE = 15;
    private final static Duration ANIMATION_DURATION = Duration.millis(400);

    private ObservableList<String> data = FXCollections.observableArrayList();
    private TableView<String> table = new TableView<>();
    private Pagination pagination;
    private boolean isAnimating = false;

    @Override
    public void start(Stage primaryStage) {
        // Tạo dữ liệu mẫu
        for (int i = 1; i <= 100; i++) {
            data.add("Dòng số " + i + " - Dữ liệu mẫu");
        }

        // Tạo cột với width tự động
        TableColumn<String, String> column = new TableColumn<>("Tên dòng");
        column.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()));
        column.prefWidthProperty().bind(table.widthProperty().subtract(2));
        table.getColumns().add(column);

        // Thiết lập TableView
        table.setRowFactory(tv -> {
            TableRow<String> row = new TableRow<>();
            row.setStyle("-fx-background-color: transparent;");
            return row;
        });

        // Tạo phân trang
        pagination = new Pagination((int) Math.ceil((double) data.size() / ROWS_PER_PAGE), 0);
        pagination.setPageFactory(this::createPage);

        // Thêm listener để kiểm soát animation
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            if (!isAnimating && oldIndex.intValue() != newIndex.intValue()) {
                animatePageTransition(oldIndex.intValue(), newIndex.intValue());
            }
        });

        // Khởi tạo trang đầu tiên
        loadPageData(0);

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(table, pagination);
        vbox.setStyle("-fx-padding: 10; -fx-background-color: #f5f5f5;");

        Scene scene = new Scene(vbox, 600, 500);
        scene.getStylesheets().add(getClass().getResource("/styles.css") != null ?
                getClass().getResource("/styles.css").toExternalForm() : "");

        primaryStage.setScene(scene);
        primaryStage.setTitle("Phân trang TableView với Hiệu ứng Mượt mà");
        primaryStage.show();

        // Hiệu ứng khởi tạo
        initialAnimation();
    }

    // Hiệu ứng ban đầu khi khởi động
    private void initialAnimation() {
        table.setOpacity(0);
        table.setTranslateY(20);
        pagination.setOpacity(0);
        pagination.setTranslateY(20);

        // Animation cho table
        FadeTransition tableFade = new FadeTransition(Duration.millis(1000), table);
        tableFade.setFromValue(0);
        tableFade.setToValue(1);

        TranslateTransition tableSlide = new TranslateTransition(Duration.millis(1000), table);
        tableSlide.setFromY(20);
        tableSlide.setToY(0);

        // Animation cho pagination
        FadeTransition paginationFade = new FadeTransition(Duration.millis(1000), pagination);
        paginationFade.setFromValue(0);
        paginationFade.setToValue(1);
        paginationFade.setDelay(Duration.millis(800));

        TranslateTransition paginationSlide = new TranslateTransition(Duration.millis(1000), pagination);
        paginationSlide.setFromY(20);
        paginationSlide.setToY(0);
        paginationSlide.setDelay(Duration.millis(800));

        ParallelTransition parallel = new ParallelTransition(
                tableFade, tableSlide, paginationFade, paginationSlide
        );
        parallel.play();
    }

    // Hàm tạo trang (chỉ trả về container, không load data)
    private VBox createPage(int pageIndex) {
        return new VBox(table);
    }

    // Hiệu ứng chuyển trang mượt mà
    private void animatePageTransition(int oldPageIndex, int newPageIndex) {
        if (isAnimating) return;

        isAnimating = true;

        // Xác định hướng animation (trái/phải)
        boolean slideLeft = newPageIndex > oldPageIndex;
        double slideDistance = 50;

        // Phase 1: Fade out và slide out
        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), table);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.3);

        TranslateTransition slideOut = new TranslateTransition(Duration.millis(200), table);
        slideOut.setFromX(0);
        slideOut.setToX(slideLeft ? -slideDistance : slideDistance);

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), table);
        scaleDown.setFromX(1.0);
        scaleDown.setFromY(1.0);
        scaleDown.setToX(0.95);
        scaleDown.setToY(0.95);

        ParallelTransition phaseOut = new ParallelTransition(fadeOut, slideOut, scaleDown);

        // Phase 2: Load new data và slide in
        phaseOut.setOnFinished(e -> {
            loadPageData(newPageIndex);

            // Đặt vị trí ban đầu cho slide in
            table.setTranslateX(slideLeft ? slideDistance : -slideDistance);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), table);
            fadeIn.setFromValue(0.3);
            fadeIn.setToValue(1.0);

            TranslateTransition slideIn = new TranslateTransition(Duration.millis(300), table);
            slideIn.setFromX(slideLeft ? slideDistance : -slideDistance);
            slideIn.setToX(0);

            ScaleTransition scaleUp = new ScaleTransition(Duration.millis(300), table);
            scaleUp.setFromX(0.95);
            scaleUp.setFromY(0.95);
            scaleUp.setToX(1.0);
            scaleUp.setToY(1.0);

            // Thêm hiệu ứng bounce nhẹ
            slideIn.setInterpolator(Interpolator.SPLINE(0.25, 0.46, 0.45, 0.94));

            ParallelTransition phaseIn = new ParallelTransition(fadeIn, slideIn, scaleUp);

            phaseIn.setOnFinished(event -> {
                isAnimating = false;
                // Đảm bảo table trở về trạng thái bình thường
                table.setTranslateX(0);
                table.setScaleX(1.0);
                table.setScaleY(1.0);
            });

            phaseIn.play();
        });

        phaseOut.play();
    }

    // Load dữ liệu cho trang cụ thể
    private void loadPageData(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, data.size());

        ObservableList<String> pageData = FXCollections.observableArrayList(
                data.subList(fromIndex, toIndex)
        );

        table.setItems(pageData);

        // Thêm hiệu ứng highlight cho rows mới
        animateNewRows();
    }

    // Hiệu ứng highlight cho các rows mới
    private void animateNewRows() {
        Timeline rowAnimation = new Timeline();

        for (int i = 0; i < table.getItems().size(); i++) {
            final int rowIndex = i;
            KeyFrame keyFrame = new KeyFrame(
                    Duration.millis(50 * i),
                    e -> highlightRow(rowIndex)
            );
            rowAnimation.getKeyFrames().add(keyFrame);
        }

        rowAnimation.play();
    }

    // Highlight một row cụ thể
    private void highlightRow(int rowIndex) {
        if (rowIndex < table.getItems().size()) {
            TableRow<?> row = getTableRow(rowIndex);
            if (row != null) {
                // Tạo hiệu ứng highlight nhanh
                FadeTransition highlight = new FadeTransition(Duration.millis(100), row);
                highlight.setFromValue(0.7);
                highlight.setToValue(1.0);
                highlight.setCycleCount(2);
                highlight.setAutoReverse(true);
                highlight.play();
            }
        }
    }

    // Helper method để lấy TableRow
    private TableRow<?> getTableRow(int index) {
        try {
            // Sử dụng lookup để tìm row (cách đơn giản nhất)
            return (TableRow<?>) table.lookup(".table-row-cell:nth-child(" + (index + 1) + ")");
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}