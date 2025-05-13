package org.example.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private Stage primaryStage;
    private LoginController loginC;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        loginScene(); // Starta med inloggningsscen
    }

    // Scen för inloggning
    public void loginScene() {
        LoginScene loginScene = new LoginScene(this);
        loginC = new LoginController(this);
        Scene scene = loginScene.createScene();
        primaryStage.setTitle("Pomodoro Timer - Login");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public LoginController getLoginController(){
        return loginC;
    }


    // Scen för timer
    public void timerScene() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("pomodoro.fxml"));
            Scene scene = new Scene(root, 350, 500);
            primaryStage.setTitle("POMODORO");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}