package org.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RequestDemoMain extends Application {
    private Stage primaryStage;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        loginDemo();
    }

    public void loginDemo() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("requestDemo.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setTitle("POMODORO");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
