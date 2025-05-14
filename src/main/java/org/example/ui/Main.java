package org.example.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application { // Needed for javafx to make stages
    private Stage primaryStage;
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final int LOGIN_WIDTH = 500;
    private static final int LOGIN_HEIGHT = 400;


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        loginScene(); // Start with login scene
    }

    // Shows the login scene
    public void loginScene() {
        LoginScene loginScene = new LoginScene(this);
        Scene scene = loginScene.createScene(); // Create the login scene

        setStage("Pomodoro Timer - Login", scene, LOGIN_WIDTH, LOGIN_HEIGHT);
    }

    // Shows the timer scene
    public void timerScene() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("pomodoro.fxml"));
            Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT); // Create the timer scene

            setStage("POMODORO", scene, WINDOW_WIDTH, WINDOW_HEIGHT);
        } catch (IOException e) {
            System.out.println("FXML Error");
            e.printStackTrace();
        }
    }

    // Sets the stage with the given title, scene, width, and height
    private void setStage(String title, Scene scene, int width, int height) {
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.setWidth(width);
        primaryStage.setHeight(height);
        primaryStage.setMinWidth(width * 0.8);
        primaryStage.setMinHeight(height * 0.8);
        primaryStage.centerOnScreen();
        primaryStage.setResizable(true); // Allow resizing
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}