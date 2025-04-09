package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;


public class App extends Application {


    private TextField EmailField;
    private TextField PasswordField;
    private Button Login;
    private Button Guest;
    private Button Exit;
    private Scene scene;


    @Override
    public void start(Stage primaryStage) {


        // Skapa inputfält
        Label hoursLabel = new Label("Email:");
        EmailField = new TextField();
        EmailField.setPromptText("Email");
        EmailField.setPrefWidth(140);

        Label minutesLabel = new Label("Password:");
        PasswordField = new TextField();
        PasswordField.setPromptText("Password");
        PasswordField.setPrefWidth(140);

        // Input layout
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);
        inputGrid.addRow(0, hoursLabel, EmailField);
        inputGrid.addRow(1, minutesLabel, PasswordField);
        inputGrid.setAlignment(Pos.CENTER);

        // Skapa knappar
        Login = createStyledButton("Login", "#4CAF50");
        Guest = createStyledButton("Guest mode", "#FFC107");
        Exit = createStyledButton("Exit", "#f44336");


        Login.setOnAction(e -> LoginAction());
        Guest.setOnAction(e -> GuestmodeAction());
        Exit.setOnAction(e -> ExitAction());


        // Layout för knapppar
        HBox buttonBox = new HBox(10, Login, Guest, Exit);
        buttonBox.setAlignment(Pos.CENTER);

        // Huvudlayout
        VBox root = new VBox(20, inputGrid, buttonBox);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f5f5f5;");

        // Skapa scen
        scene = new Scene(root, 350, 250);

        // Scen titel
        primaryStage.setTitle("Pomodoro Timer");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white;");
        button.setPrefWidth(120);
        return button;
    }

    private void LoginAction() {
        System.out.println("LoginAction");
        newApp newApp = new newApp();
        newApp.showTimer();
    }

    private void GuestmodeAction() {
        System.out.println("GuestmodeAction");
        newApp newApp = new newApp();
        newApp.showTimer();
    }

    private void ExitAction() {
        System.out.println("ExitAction");

    }

}