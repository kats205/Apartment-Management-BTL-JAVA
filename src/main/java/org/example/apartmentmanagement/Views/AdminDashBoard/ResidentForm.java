package org.example.apartmentmanagement.Views.AdminDashBoard;

//


import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import  org.example.apartmentmanagement.Controller.ResidentController;

import java.sql.Date;
import java.sql.SQLException;

import static javafx.application.Application.launch;

public class ResidentForm {
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Thêm cư dân mới");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(8);
        grid.setHgap(10);

        Label nameLabel = new Label("Họ và tên:");
        TextField nameInput = new TextField();

        Label idLabel = new Label("CMND/CCCD:");
        TextField idInput = new TextField();

        Label dobLabel = new Label("Ngày sinh:");
        DatePicker dobInput = new DatePicker();

        Label genderLabel = new Label("Giới tính:");
        ComboBox<String> genderInput = new ComboBox<>();
        genderInput.getItems().addAll("Nam", "Nữ", "Khác");

        Label phoneLabel = new Label("Số điện thoại:");
        TextField phoneInput = new TextField();

        Label emailLabel = new Label("Email:");
        TextField emailInput = new TextField();

        Label apartmentLabel = new Label("Mã căn hộ:");
        TextField apartmentInput = new TextField();

//        Label moveInLabel = new Label("Ngày chuyển vào:");
//        DatePicker moveInInput = new DatePicker();

        Label primaryLabel = new Label("Cư dân chính:");
        CheckBox primaryInput = new CheckBox();

        Button saveButton = new Button("Lưu");
        saveButton.setOnAction(e -> {
            ResidentController controller = new ResidentController();
            try {
                controller.addResident(
                        idInput.getText(),
                        apartmentInput.getText(),
                        nameInput.getText(),
                        Date.valueOf(dobInput.getValue()),
                        genderInput.getValue(),
                        phoneInput.getText(),
                        emailInput.getText(),
    //                    moveInInput.getValue(),
                        primaryInput.isSelected()
                );
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        grid.add(nameLabel, 0, 0); grid.add(nameInput, 1, 0);
        grid.add(idLabel, 0, 1); grid.add(idInput, 1, 1);
        grid.add(dobLabel, 0, 2); grid.add(dobInput, 1, 2);
        grid.add(genderLabel, 0, 3); grid.add(genderInput, 1, 3);
        grid.add(phoneLabel, 0, 4); grid.add(phoneInput, 1, 4);
        grid.add(emailLabel, 0, 5); grid.add(emailInput, 1, 5);
        grid.add(apartmentLabel, 0, 6); grid.add(apartmentInput, 1, 6);
//        grid.add(moveInLabel, 0, 7); grid.add(moveInInput, 1, 7);
        grid.add(primaryLabel, 0, 8); grid.add(primaryInput, 1, 8);
        grid.add(saveButton, 1, 9);

        Scene scene = new Scene(grid, 450, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}