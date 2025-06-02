module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.net.http;
    requires org.json;

    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.databind;
    requires transitive javafx.graphics;

    exports org.example.model.dto to com.fasterxml.jackson.databind;
    exports org.example.model.service to com.fasterxml.jackson.databind;
    exports org.example.infrastructure to com.fasterxml.jackson.databind;

    opens org.example.ui to javafx.fxml;
    exports org.example.ui to javafx.graphics;
    exports org.example.controller to javafx.graphics;
    opens org.example.controller to javafx.fxml;
    exports org.example.manager to javafx.graphics;
    opens org.example.manager to javafx.fxml;
}