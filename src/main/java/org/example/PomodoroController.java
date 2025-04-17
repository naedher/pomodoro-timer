package org.example;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class PomodoroController {
    @FXML private Label timerLabel;
    @FXML private Button startButton, resetButton;
    @FXML private ToggleButton focusButton, shortBreakButton, longBreakButton;
    @FXML private MenuButton menuButton;

    private timerTask timer;
    private boolean running = false;
    private String mode = "FOCUS";

    private int timeLeft; // Tiden som är kvar (i sekunder)
    private final int focusTime = 25; // Fokus tid i minuter
    private final int shortBreakTime = 5; // Kort paus tid i minuter
    private final int longBreakTime = 15; // Lång paus tid i minuter

    @FXML
    public void initialize() {
        setupMenu();
        setFocus(); // Standardläge
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

    private void openSettings() {
        // implemnteras senare
    }

    private void showAbout() {
        // implementeras senare
        // kan använda alert här
        // eller joption
    }

    private void exitApplication() {
        Stage stage = (Stage) menuButton.getScene().getWindow();
        stage.close();
    }

    private void updateDisplay() {
        int min = timeLeft / 60;
        int sec = timeLeft % 60;
        timerLabel.setText(String.format("%02d:%02d", min, sec));
    }

    @FXML
    private void startStop() {
        if (running) {
            if (timer != null) {
                timer.stopTimer();
                timeLeft = timer.getTimeLeft();
            }
            startButton.setText("START");
            running = false;
        } else {
            startButton.setText("PAUSE");
            running = true;

            timer = new timerTask(timeLeft,
                    this::updateDisplay,
                    () -> {
                        running = false;
                        startButton.setText("START");
                    });

            timer.start();
        }
    }

    @FXML
    private void reset() {
        if (timer != null) timer.stopTimer(); // Stoppa pågående timer
        running = false;
        startButton.setText("START");

        if (mode.equals("FOCUS")) {
            timeLeft = focusTime * 60;
        } else if (mode.equals("SHORT BREAK")) {
            timeLeft = shortBreakTime * 60;
        } else {
            timeLeft = longBreakTime * 60;
        }

        updateDisplay();
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
}
