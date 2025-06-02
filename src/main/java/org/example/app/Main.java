package org.example.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.controller.LoginController;
import org.example.controller.RegisterController;

import java.io.IOException;

public class Main extends Application {
    private Stage primaryStage;
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final int LOGIN_WIDTH = 700;
    private static final int LOGIN_HEIGHT = 500;
    private static final int REGISTER_WIDTH = 500;
    private static final int REGISTER_HEIGHT = 450;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        loginScene(); // Start with login scene
    }

    // Login scene using FXML
    public void loginScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/app/Login.fxml"));
            Parent root = loader.load();

            // Get the controller and set mainApp
            LoginController controller = loader.getController();
            controller.setMainApp(this);

            Scene scene = new Scene(root, LOGIN_WIDTH, LOGIN_HEIGHT);
            setStage("Pomodoro Timer - Login", scene, LOGIN_WIDTH, LOGIN_HEIGHT, false);
        } catch (IOException e) {
            System.out.println("Login FXML Error");
            e.printStackTrace();
        }
    }

    // Register scene using FXML
    public void registerScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/app/Register.fxml"));
            Parent root = loader.load();

            // Get the controller and set mainApp
            RegisterController controller = loader.getController();
            controller.setMainApp(this);

            Scene scene = new Scene(root, REGISTER_WIDTH, REGISTER_HEIGHT);
            setStage("Pomodoro Timer - Register", scene, REGISTER_WIDTH, REGISTER_HEIGHT, false);
        } catch (IOException e) {
            System.out.println("Register FXML Error");
            e.printStackTrace();
        }
    }

    // Timer scene
    public void timerScene() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/app/pomodoro.fxml"));
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