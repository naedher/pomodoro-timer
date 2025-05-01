module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.net.http;
    requires org.json;
    requires spring.webflux;
    requires spring.web;
    requires reactor.core;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.databind;

    opens org.example to javafx.fxml;

    exports org.example.api;
    opens org.example.api to javafx.fxml;
    exports org.example.demo;
    opens org.example.demo to javafx.fxml;
    exports org.example.ui;
    opens org.example.ui to javafx.fxml;
}
