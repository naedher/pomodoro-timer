package org.example.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Duration;
import org.example.infrastructure.ApiClient;
import org.example.model.AppContext;
import org.example.model.dto.TimerCreate;
import org.example.ui.TimerMode;
import org.json.JSONObject;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.concurrent.CompletableFuture;

public class TimerController implements TimerCallback{

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

    private Timer timer;

    private ApiClient apiClient;

    @FXML
    public void initialize() {

        apiClient = new ApiClient(AppContext.getInstance().getAuthToken());
        timer = new Timer(4, this);
        updateDisplay(timer.getTimeLeft());
        updateIntervalDisplay();
    }

    @FXML
    private void addSavedTimer() {
        // Create input dialog
        TextInputDialog dialog = new TextInputDialog("Work/25/5/15/4");
        dialog.setTitle("Add Timer Preset");
        dialog.setHeaderText("Enter timer in format:");
        dialog.setContentText("Name/FocusMinutes/ShortBreak/LongBreak/Intervals");

        dialog.showAndWait().ifPresent(input -> {
            try {
                String[] parts = input.split("/");
                TimerCreate timerCreate = new TimerCreate(
                        parts[0],
                        Integer.parseInt(parts[1]) * 60, // Convert to seconds
                        Integer.parseInt(parts[2]) * 60,
                        Integer.parseInt(parts[3]) * 60,
                        Integer.parseInt(parts[4])
                );

                saveTimer(timerCreate);

            } catch (Exception e) {
                showAlert("Error", "Invalid format. Example: Work/25/5/15/4");
            }
        });
    }

    public CompletableFuture<Void> saveTimer(TimerCreate timer) {
        try {

            String currentToken = AppContext.getInstance().getAuthToken();
            if (currentToken == null || currentToken.isEmpty()) {
                throw new IllegalStateException("Not authenticated - please login again");
            }

            apiClient.setToken(currentToken);

            JSONObject json = new JSONObject();
            json.put("name", timer.getName());
            json.put("workDuration", timer.getWorkDuration());
            json.put("shortBreakDuration", timer.getShortBreakDuration());
            json.put("longBreakDuration", timer.getLongBreakDuration());
            json.put("pomodoroCount", timer.getPomodoroCount());

            System.out.println("Saving timer with token: " + currentToken);
            System.out.println("Request body: " + json.toString());

            return apiClient.post("api/timers", json.toString())
                    .thenAccept(response -> {
                        System.out.println("Save successful: " + response);
                        Platform.runLater(() -> {
                            timerListView.getItems().add(timer);
                            showAlert("Success", "Timer saved successfully!");
                        });
                    })
                    .exceptionally(ex -> {
                        System.err.println("Save failed: " + ex.getMessage());
                        Platform.runLater(() ->
                                showAlert("Error", "Failed to save timer: " +
                                        (ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage()))
                        );
                        return null;
                    });

        } catch (Exception e) {
            Platform.runLater(() ->
                    showAlert("Error", "Failed to prepare timer: " + e.getMessage())
            );
            return CompletableFuture.failedFuture(e);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
                updateDisplay(timeLeft);
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

        updateDisplay(timeLeft);
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
    private void updateDisplay(int timeLeft) {
        int minutes = timeLeft / 60;
        int seconds = timeLeft % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    @Override
    public void onTick(int timeLeft) {
        // Update the timer display with the remaining time
        Platform.runLater(() -> {
            int minutes = timeLeft / 60;
            int seconds = timeLeft % 60;
            timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
        });
    }

    @Override
    public void onComplete() {
        // Handle timer completion logic
        Platform.runLater(() -> {
            handleTimerCompletion();
        });
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
        updateDisplay(timeLeft);

        // Auto-start the timer
        if (!running) {
            startStop();
        }
    }
}