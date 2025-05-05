module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.net.http;
    requires org.json;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.databind;
    requires java.logging;

    opens org.example to javafx.fxml;

    exports org.example.demo;
    opens org.example.demo to javafx.fxml;
}
