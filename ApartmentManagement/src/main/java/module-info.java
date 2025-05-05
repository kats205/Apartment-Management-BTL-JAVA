module com.utc2.apartmentmanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires kernel; // For com.itextpdf.kernel.pdf
    requires layout; // For com.itextpdf.layout
    requires io;     // For com.itextpdf.io.font
    // For javax.swing.text

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;
    requires bcrypt;
    requires itextpdf;
    requires javafx.swing;

    opens com.utc2.apartmentmanagement.Test to javafx.base;
    exports com.utc2.apartmentmanagement.Test;
    opens com.utc2.apartmentmanagement.Views to javafx.fxml;
    exports com.utc2.apartmentmanagement.Views;
    opens com.utc2.apartmentmanagement.Controller to javafx.fxml;
    exports com.utc2.apartmentmanagement.Controller;
    exports com.utc2.apartmentmanagement.Model;
    opens com.utc2.apartmentmanagement.Model to javafx.base;
    opens com.utc2.apartmentmanagement.Controller.User to javafx.fxml;
    exports com.utc2.apartmentmanagement.Controller.User;

}