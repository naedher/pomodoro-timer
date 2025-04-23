module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.net.http;
    requires org.json;

    opens org.example to javafx.fxml;
    exports org.example;
}
