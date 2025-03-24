module org.example.learn1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.desktop;

    opens org.example.learn1 to javafx.fxml;
    exports org.example.learn1;
    exports org.example.learn1.Events;
    opens org.example.learn1.Events to javafx.fxml;
}