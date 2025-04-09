package org.example;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class newApp {

    private int remainingSeconds = 0;
    private boolean isRunning = false;
    private boolean wasPaused = false;

    private Text timeDisplay;
    private TextField hoursInput;
    private TextField minutesInput;
    private Button startButton;
    private Button pauseButton;
    private Button resetButton;
    private Timeline timer;

    public void showTimer() {
        Stage stage = new Stage(); // Nytt fönster
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

        startButton.setOnAction(e -> startB());
        pauseButton.setOnAction(e -> pauseB());
        resetButton.setOnAction(e -> resetB());


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
        stage.setTitle("Pomodoro Timer");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white;");
        button.setPrefWidth(80);
        return button;
    }

    private void startB() {
        System.out.println("Start-knappen klickades!");
        if (isRunning) return; // Om timern redan körs, gör inget
        if (wasPaused){

        }

        try {
            if (!wasPaused){
                int hours = Integer.parseInt(hoursInput.getText().isEmpty() ? "0" : hoursInput.getText());
                int minutes = Integer.parseInt(minutesInput.getText().isEmpty() ? "0" : minutesInput.getText());
                remainingSeconds = (hours * 3600) + (minutes * 60);
            }

            if (remainingSeconds <= 0) {
                timeDisplay.setText("Invalid input");
                return;
            }

            isRunning = true;
            startButton.setDisable(true); // Inaktivera Start-knappen när timern startar
            pauseButton.setDisable(false); // Aktivera Pause-knappen

            timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimer()));
            timer.setCycleCount(Timeline.INDEFINITE); // Kör tills vi stoppar den
            timer.play();

        } catch (NumberFormatException e) {
            timeDisplay.setText("Invalid input");
        }
    }


    private void pauseB() {
        System.out.println("Pause-knappen klickades!");
        if (isRunning) {
            timer.stop();
            isRunning = false;
            startButton.setDisable(false); // Tillåter att starta om timern
            wasPaused = true;
        }
    }

    private void resetB() {
        System.out.println("Restore-knappen klickades!");
        if (timer != null) {
            timer.stop();
        }
        isRunning = false;
        remainingSeconds = 0;
        timeDisplay.setText("00:00:00");
        startButton.setDisable(false);
        pauseButton.setDisable(true);
        wasPaused = false;

        Settings newWindow = new Settings(); // Skapa det nya fönstret
        newWindow.show();

    }

    private void updateTimer() {
        if (remainingSeconds > 0) {
            remainingSeconds--;
            int hours = remainingSeconds / 3600;
            int minutes = (remainingSeconds % 3600) / 60;
            int seconds = remainingSeconds % 60;
            timeDisplay.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
        } else {
            timer.stop();
            isRunning = false;
            startButton.setDisable(false); // Återaktivera Start-knappen när timern är slut
            pauseButton.setDisable(true); // Inaktivera Pause-knappen
            wasPaused = false;
            timeDisplay.setText("Time's up!");
            alarm();
        }
    }

    private void alarm(){

        String fileName = "/sound/alarm.wav"; // Anpassa efter var filen ligger
        try {
            InputStream inputStream = App.class.getResourceAsStream(fileName);
            if (inputStream == null) {
                System.err.println("Ljudfilen hittades inte!");
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new BufferedInputStream(inputStream));
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();

            // Vänta tills ljudet är klart
            Thread.sleep(clip.getMicrosecondLength() / 1000);

            clip.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}



