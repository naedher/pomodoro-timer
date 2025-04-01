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

public class App extends Application {

    private int remainingSeconds = 0;
    private boolean isRunning = false;

    private Text timeDisplay;
    private TextField hoursInput;
    private TextField minutesInput;
    private Button startButton;
    private Button pauseButton;
    private Button resetButton;

    @Override
    public void start(Stage primaryStage) {
        // Skapa tidsdisplay
        timeDisplay = new Text("00:00:00");
        timeDisplay.setFont(Font.font(36));

        // Skapa inputfält
        Label hoursLabel = new Label("Hours:");
        hoursInput = new TextField();
        hoursInput.setPromptText("0");
        hoursInput.setPrefWidth(60);

        Label minutesLabel = new Label("Minutes:");
        minutesInput = new TextField();
        minutesInput.setPromptText("0");
        minutesInput.setPrefWidth(60);

        // Input layout
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);
        inputGrid.addRow(0, hoursLabel, hoursInput);
        inputGrid.addRow(1, minutesLabel, minutesInput);
        inputGrid.setAlignment(Pos.CENTER);

        // Skapa knappar
        startButton = createStyledButton("Start", "#4CAF50");
        pauseButton = createStyledButton("Pause", "#FFC107");
        resetButton = createStyledButton("Restore", "#f44336");

        pauseButton.setDisable(true);

        // Layout för knapppar
        HBox buttonBox = new HBox(10, startButton, pauseButton, resetButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Huvudlayout
        VBox root = new VBox(20, timeDisplay, inputGrid, buttonBox);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f5f5f5;");

        // Skapa scen
        Scene scene = new Scene(root, 350, 250);

        // Scen titel
        primaryStage.setTitle("Pomodoro Timer");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white;");
        button.setPrefWidth(80);
        return button;
    }
}