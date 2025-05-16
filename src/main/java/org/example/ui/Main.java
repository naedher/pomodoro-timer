package org.example.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {
    private Stage primaryStage;
    private LoginController loginC;
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final int LOGIN_WIDTH = 500;
    private static final int LOGIN_HEIGHT = 400;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        loginScene(); // Start with login scene
    }

    // Login scene
    public void loginScene() {
        LoginScene loginScene = new LoginScene(this);
        loginC = new LoginController(this);
        Scene scene = loginScene.createScene();
        setStage("Pomodoro Timer - Login", scene, LOGIN_WIDTH, LOGIN_HEIGHT, false);
    }

    // Timer scene
    public void timerScene() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("pomodoro.fxml"));
            Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            setStage("POMODORO", scene, WINDOW_WIDTH, WINDOW_HEIGHT, true);
        } catch (IOException e) {
            System.out.println("FXML Error");
            e.printStackTrace();
        }
    }

    // Unified stage setup method
    private void setStage(String title, Scene scene, int width, int height, boolean resizable) {
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.setWidth(width);
        primaryStage.setHeight(height);
        primaryStage.setMinWidth(width * 0.8);
        primaryStage.setMinHeight(height * 0.8);
        primaryStage.centerOnScreen();
        primaryStage.setResizable(resizable);
        primaryStage.show();
    }

    public LoginController getLoginController() {
        return loginC;
    }

    private static String authToken;

    public static String getToken() {
        return authToken;
    }

    public static void setToken(String token) {
        authToken = token;
    }

    public static void main(String[] args) {
        launch(args);
    }
}