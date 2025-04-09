package org.example;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Settings {

    public void show() {
        Stage stage = new Stage(); // Nytt fönster

        Label label = new Label("Hej från det nya fönstret!");
        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 250, 150);

        stage.setTitle("Nytt fönster");
        stage.setScene(scene);
        stage.show();
    }

}
