module org.example.apartmentmanagement {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;

    opens org.example.apartmentmanagement to javafx.fxml;
    exports org.example.apartmentmanagement;
    exports org.example.apartmentmanagement.controller;
    opens org.example.apartmentmanagement.controller to javafx.fxml;
    exports org.example.apartmentmanagement.Testing;
    opens org.example.apartmentmanagement.Testing to javafx.fxml;
    exports org.example.apartmentmanagement.view;
    opens org.example.apartmentmanagement.view to javafx.fxml;
}