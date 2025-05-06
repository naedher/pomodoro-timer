module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.net.http;
    requires org.json;
//    requires spring.webflux;
//    requires spring.web;
//    requires reactor.core;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.databind;
    requires transitive javafx.graphics;

    exports org.example.model.dto to com.fasterxml.jackson.databind;
    exports org.example.model.service to com.fasterxml.jackson.databind;
    exports org.example.infrastructure to com.fasterxml.jackson.databind;

//    opens org.example to javafx.fxml;
//    exports org.example to javafx.graphics;
//    exports org.example.api;
//    opens org.example.api to javafx.fxml;
}
