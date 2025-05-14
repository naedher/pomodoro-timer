package org.example.ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Duration;
import org.example.model.dto.TimerCreate;
import org.example.ui.TimerMode;
import org.json.JSONObject;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

public class TimerController {

    @FXML private Label timerLabel;
    @FXML private Label intervalLabel;
    @FXML private Button startButton;
    @FXML private Button resetButton;
    @FXML private ToggleButton focusButton;
    @FXML private ToggleButton shortBreakButton;
    @FXML private ToggleButton longBreakButton;

    @FXML private ListView<TimerCreate> timerListView;

    private Timeline timeline;
    private boolean running = false;
    private int timeLeft;
    private TimerMode currentMode = TimerMode.FOCUS;
    private int currentInterval = 1;

    private int focusTime = 25 * 60;
    private int shortBreakTime = 5 * 60;
    private int longBreakTime = 15 * 60;
    private int totalIntervals = 4;

    private final ObservableList<TimerCreate> savedTimers = FXCollections.observableArrayList(); // List of saved timers

    @FXML
    public void initialize() {
        setupTimerListView(); // Initialize the ListView
        reset();
    }

    // Method to set up the ListView for saved timers
    private void setupTimerListView() {
        timerListView.setItems(savedTimers);
        timerListView.setCellFactory(lv -> new ListCell<TimerCreate>() {
            @Override
            protected void updateItem(TimerCreate timer, boolean empty) {
                super.updateItem(timer, empty);
                setText(empty || timer == null ? null : formatTimerDisplay(timer));
            }
        });

        // Load saved timers from server
        timerListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newTimer) -> {
            if (newTimer != null) loadTimerSettings(newTimer);
        });
    }

    // Method to format the display of the timer in the ListView
    private String formatTimerDisplay(TimerCreate timer) {
        return String.format("%s (%d/%d/%d - %d intervals)",
                timer.getName(),
                timer.getWorkDuration()/60,
                timer.getShortBreakDuration()/60,
                timer.getLongBreakDuration()/60,
                timer.getPomodoroCount());
    }

    // Method to add a new timer preset
    @FXML
    private void addSavedTimer() {
        TextInputDialog dialog = new TextInputDialog("Work/25/5/15/4");
        dialog.setTitle("Add Timer Preset");
        dialog.setHeaderText("Enter timer in format:");
        dialog.setContentText("Name/FocusMinutes/ShortBreak/LongBreak/Intervals");

        dialog.showAndWait().ifPresent(input -> {
            try {
                String[] parts = input.split("/");
                TimerCreate newTimer = new TimerCreate(
                        parts[0],
                        Integer.parseInt(parts[1]) * 60, //  // Convert minutes to seconds
                        Integer.parseInt(parts[2]) * 60,
                        Integer.parseInt(parts[3]) * 60,
                        Integer.parseInt(parts[4])
                );

                savedTimers.add(newTimer); // Add to the ListView
                saveTimerToServer(newTimer); // Save to server

            } catch (Exception e) {
                showAlert("Invalid Format", "Example: Work/25/5/15/4");
            }
        });
    }

    // Method to load timer settings from the selected timer
    private void loadTimerSettings(TimerCreate timer) {
        this.focusTime = timer.getWorkDuration();
        this.shortBreakTime = timer.getShortBreakDuration();
        this.longBreakTime = timer.getLongBreakDuration();
        this.totalIntervals = timer.getPomodoroCount();
        this.currentInterval = 1;
        reset();
    }

    // Method to save the timer to the server
    private void saveTimerToServer(TimerCreate timer) {
        try {
            JSONObject json = new JSONObject();
            json.put("name", timer.getName());
            json.put("workDuration", timer.getWorkDuration());
            json.put("shortBreakDuration", timer.getShortBreakDuration());
            json.put("longBreakDuration", timer.getLongBreakDuration());
            json.put("pomodoroCount", timer.getPomodoroCount());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://pomodoro-timer.koyeb.app/api/timers"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                    .build();

            HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        /* if (response.statusCode() != 200) {
                            Platform.runLater(() ->
                                    showAlert("Server Error", "Failed to save timer")
                            );
                        } */
                    });

        } catch (Exception e) {
            Platform.runLater(() -> showAlert("Error", e.getMessage()));
        }
    }

    // Method to start or stop the timer
    @FXML
    private void startStop() {
        if (running) {
            timeline.stop();
            running = false;
            startButton.setText("START");
        } else {
            timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                timeLeft--;
                updateDisplay();
                if (timeLeft <= 0) handleTimerCompletion();
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
            running = true;
            startButton.setText("PAUSE");
        }
    }

    // Method to reset the timer
    @FXML
    private void reset() {
        if (timeline != null) timeline.stop();
        running = false;
        startButton.setText("START");

        switch (currentMode) {
            case FOCUS -> timeLeft = focusTime;
            case SHORT_BREAK -> timeLeft = shortBreakTime;
            case LONG_BREAK -> timeLeft = longBreakTime;
        }

        updateDisplay();
        updateIntervalDisplay();
        updateToggleButtons();
    }

    // Method to handle timer completion
    private void handleTimerCompletion() {
        timeline.stop();
        running = false;
        startButton.setText("START");

        if (currentMode == TimerMode.FOCUS) {
            currentInterval++;
            if (currentInterval > totalIntervals) {
                currentInterval = 1;
                currentMode = TimerMode.LONG_BREAK;
            } else {
                currentMode = TimerMode.SHORT_BREAK;
            }
        } else {
            currentMode = TimerMode.FOCUS;
        }

        reset();
    }

    // Method to update the timer display
    private void updateDisplay() {
        int minutes = timeLeft / 60;
        int seconds = timeLeft % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    // Method to update the interval display
    private void updateIntervalDisplay() {
        intervalLabel.setText(String.format("Interval: %d/%d", currentInterval, totalIntervals));
    }

    // Method to update the toggle buttons based on the current mode
    private void updateToggleButtons() {
        focusButton.setSelected(currentMode == TimerMode.FOCUS);
        shortBreakButton.setSelected(currentMode == TimerMode.SHORT_BREAK);
        longBreakButton.setSelected(currentMode == TimerMode.LONG_BREAK);
    }

    // Method to show an alert dialog
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to handle focus button action
    @FXML
    private void handleFocus() {
        if (focusButton.isSelected()) {
            currentMode = TimerMode.FOCUS;
            reset();
        }
    }

    // Method to handle short break button action
    @FXML
    private void handleShortBreak() {
        if (shortBreakButton.isSelected()) {
            currentMode = TimerMode.SHORT_BREAK;
            reset();
        }
    }

    // Method to handle long break button action
    @FXML
    private void handleLongBreak() {
        if (longBreakButton.isSelected()) {
            currentMode = TimerMode.LONG_BREAK;
            reset();
        }
    }

    // Method to handle debug button action
    @FXML
    private void handleDebug() {
        // Set timer to 3 seconds for testing
        timeLeft = 3;
        updateDisplay();

        // Auto-start the timer
        if (!running) {
            startStop();
        }
    }
}