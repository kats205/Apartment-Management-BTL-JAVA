module org.example.apartmentmanagement {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;

    opens org.example.apartmentmanagement to javafx.fxml;
    exports org.example.apartmentmanagement;
    exports org.example.apartmentmanagement.views;
    opens org.example.apartmentmanagement.views to javafx.fxml;
    exports org.example.apartmentmanagement.controllers;
    opens org.example.apartmentmanagement.controllers to javafx.fxml;
}