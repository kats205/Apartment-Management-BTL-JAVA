module com.utc2.apartmentmanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires javax.servlet.api;
    requires jakarta.servlet;
    requires javafx.web; // Required for WebView
    requires com.google.gson; // Required for Gson
    requires java.desktop; // Required for java.awt.Desktop
    requires static lombok;
    requires jdk.httpserver;
    requires kernel; // For com.itextpdf.kernel.pdf
    requires layout; // For com.itextpdf.layout
    requires io;     // For com.itextpdf.io.font

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;
    requires bcrypt;

    opens com.utc2.apartmentmanagement.Views to javafx.fxml;
    exports com.utc2.apartmentmanagement.Views;
    opens com.utc2.apartmentmanagement.Controller to javafx.fxml;
    exports com.utc2.apartmentmanagement.Controller;
}