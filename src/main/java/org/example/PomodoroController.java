package org.example;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PomodoroController {
    @FXML private Label timerLabel;
    @FXML private Button startButton, resetButton;
    @FXML private ToggleButton focusButton, shortBreakButton, longBreakButton;
    @FXML private MenuButton menuButton;

    private Timeline timeline; // timeline för javafx
    private boolean running = false;
    private String mode = "FOCUS";
    private int timeLeft;

    private final int focusTime = 25 * 60;    // 25 minuter i sekunder
    private final int shortBreakTime = 5 * 60;  // 5 minuter i sekunder
    private final int longBreakTime = 15 * 60;  // 15 minuter i sekunder

    @FXML
    public void initialize() {
        setupMenu();
        setFocus(); // Default läge
    }

    private void setupMenu() {
        MenuItem settingsItem = new MenuItem("Settings");
        settingsItem.setOnAction(e -> openSettings());

        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setOnAction(e -> showAbout());

        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(e -> exitApplication());

        menuButton.getItems().addAll(settingsItem, aboutItem, new SeparatorMenuItem(), exitItem);
    }

    private void updateDisplay() {
        int min = timeLeft / 60;
        int sec = timeLeft % 60;
        timerLabel.setText(String.format("%02d:%02d", min, sec));
    }

    @FXML
    private void startStop() {
        if (running) {
            pauseTimer();
        } else {
            startTimer();
        }
    }

    private void startTimer() {
        if (timeline != null) {
            timeline.stop();
        }

        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    timeLeft--;
                    updateDisplay();
                    if (timeLeft <= 0) {
                        timeline.stop();
                        running = false;
                        startButton.setText("START");
                    }
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        running = true;
        startButton.setText("PAUSE");
    }

    private void pauseTimer() {
        if (timeline != null) {
            timeline.stop();
        }
        running = false;
        startButton.setText("START");
    }

    @FXML
    private void reset() {
        pauseTimer();
        setInitialTime();
        updateDisplay();
    }

    private void setInitialTime() {
        switch (mode) {
            case "FOCUS":
                timeLeft = focusTime;
                break;
            case "SHORT BREAK":
                timeLeft = shortBreakTime;
                break;
            case "LONG BREAK":
                timeLeft = longBreakTime;
                break;
        }
    }

    @FXML
    private void setFocus() {
        mode = "FOCUS";
        focusButton.setSelected(true);
        shortBreakButton.setSelected(false);
        longBreakButton.setSelected(false);
        reset();
    }

    @FXML
    private void setShortBreak() {
        mode = "SHORT BREAK";
        focusButton.setSelected(false);
        shortBreakButton.setSelected(true);
        longBreakButton.setSelected(false);
        reset();
    }

    @FXML
    private void setLongBreak() {
        mode = "LONG BREAK";
        focusButton.setSelected(false);
        shortBreakButton.setSelected(false);
        longBreakButton.setSelected(true);
        reset();
    }

    private void openSettings() {
        // Implementera senare
    }

    private void showAbout() {
        // Implementer senare
    }

    private void exitApplication() {
        Stage stage = (Stage) menuButton.getScene().getWindow();
        stage.close();
    }
}