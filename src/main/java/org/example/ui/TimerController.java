package org.example.ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.model.AppContext;
import org.example.model.dto.TimerDetails;
import org.example.model.service.TimerService;
import org.example.model.service.impl.TimerServiceImpl;

import java.io.IOException;

public class TimerController implements TimerCallback {

    @FXML private Label timerLabel;
    @FXML private Label intervalLabel;
    @FXML private Button startButton;
    @FXML private Button resetButton;
    @FXML private ToggleButton focusButton;
    @FXML private ToggleButton shortBreakButton;
    @FXML private ToggleButton longBreakButton;

    @FXML private ListView<TimerDetails> timerListView;

    private Timeline timeline;
    private boolean running = false;
    private int timeLeft;
    private TimerMode currentMode = TimerMode.FOCUS;
    private int currentInterval = 1;

    private TimerDetails selectedTimer;
    private Timer timer;
    private TimerService timerService;



    @FXML
    public void initialize() {

        // Create a default timer to be shown on startup
        this.selectedTimer = new TimerDetails(-1L, "Default timer", null, 25, 5, 15, 4);

        // Create TimerService
        String token = AppContext.getInstance().getAuthToken();
        this.timerService = new TimerServiceImpl(token);
        initListListener();

        timer = new Timer(selectedTimer, this);
        updateDisplay(timer.getTimeLeft());
        updateIntervalDisplay();
        updateTimerList();
    }

    @FXML
    private void addSavedTimer() {
        // Load AddTimer dialogue
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ui/AddTimer.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // AddTimerController addTimerController = loader.getController();
        // pass any data or read data when finished

        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setScene(new Scene(root));
        dialogStage.showAndWait(); // blocks until closed

        // Update the list
        updateTimerList();
    }

    private void updateTimerList() {

        timerService.getUserTimers()
                .thenAccept(response -> {
                    ObservableList<TimerDetails> observableList = FXCollections.observableArrayList(response);
                    timerListView.setItems(observableList);
                }).exceptionally(compException -> {
                    Throwable ex = compException.getCause();
                    showAlert("List update error", ex.getMessage());
                    return null;
                });
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

        timeLeft = selectedTimer.getDurationByMode(currentMode);

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
            if (currentInterval > selectedTimer.getPomodoroCount()) {
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
        intervalLabel.setText(String.format("Interval: %d/%d", currentInterval, selectedTimer.getPomodoroCount()));
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

    private void initListListener() {
        timerListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedTimer = newVal;
            }

            updateDisplay(selectedTimer.getDurationByMode(currentMode));
            updateIntervalDisplay();
        });
    }

    private int getCurrentModeDuration() {
        return switch (currentMode) {
            case FOCUS -> selectedTimer.getWorkDuration();
            case LONG_BREAK -> selectedTimer.getLongBreakDuration();
            case SHORT_BREAK -> selectedTimer.getShortBreakDuration();
        };
    }
}