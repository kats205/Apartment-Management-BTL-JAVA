module com.utc2.apartmentmanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires kernel; // For com.itextpdf.kernel.pdf
    requires layout; // For com.itextpdf.layout
    requires io;     // For com.itextpdf.io.font
    requires java.desktop; // For javax.swing.text

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;
    requires bcrypt;

    opens com.utc2.apartmentmanagement.Views to javafx.fxml;
    exports com.utc2.apartmentmanagement.Views;
    opens com.utc2.apartmentmanagement.Controller to javafx.fxml;
    exports com.utc2.apartmentmanagement.Controller;
    opens com.utc2.apartmentmanagement.Model to javafx.base;

}