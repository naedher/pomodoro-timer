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
import org.example.model.TimerServiceFactory;
import org.example.model.dto.TimerDetails;
import org.example.model.service.TimerService;


import java.io.IOException;

public class TimerController {

    @FXML private Label timerLabel;
    @FXML private Label intervalLabel;
    @FXML private Button startButton;
    @FXML private Button resetButton;
    @FXML private ToggleButton focusButton;
    @FXML private ToggleButton shortBreakButton;
    @FXML private ToggleButton longBreakButton;

    @FXML private ListView<TimerDetails> timerListView;

    private Timeline timeline;
    private int timeLeft;
    private TimerMode currentMode;
    private int currentInterval;

    private TimerDetails selectedTimer;
    private TimerService timerService;
    private boolean running;


    @FXML
    public void initialize() {
        this.currentMode = TimerMode.FOCUS;
        this.currentInterval = 1;
        this.running = false;

        // Create a default timer to be shown on startup
        this.selectedTimer = new TimerDetails(-1L, "Default timer", null, 25, 5, 15, 4);
        createNewTimeline();

        // Create TimerService
        String token = AppContext.getInstance().getAuthToken();
        //this.timerService = new RemoteTimerService(token);
        // we simply get factory class here, it choose which logic will work.
        this.timerService = TimerServiceFactory.get();

        initListListener();
        update();
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

        AddTimerController addTimerController = loader.getController();

        Stage dialogStage = new Stage();
        addTimerController.setStage(dialogStage);

        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setScene(new Scene(root));
        dialogStage.showAndWait(); // blocks until closed

        // Update the list
        updateTimerList();
    }

    protected void updateTimerList() {
        timerService.getUserTimers()
                .thenAccept(list -> Platform.runLater(() -> {
                    timerListView.setItems(
                            FXCollections.observableArrayList(list));
                }))
                .exceptionally(ex -> {
                    Platform.runLater(() ->
                            showAlert("List update error", ex.getCause().getMessage()));
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
        if (!running) {
            // start
            start();
        } else {
            // pause
            stop();
        }
    }

    private void onTick() {
        timeLeft--;
        if (timeLeft <= 0) {
            onComplete();
        } else {
            updateDisplay();
        }
    }

    private void initListListener() {
        timerListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedTimer = newVal;
                timeLeft = selectedTimer.getDurationByMode(currentMode);
            }
            update();
        });
    }


    private void stop() {
        timeline.stop();
        running = false;
        startButton.setText("Start");
    }

    private void start() {
        timeline.play();
        running = true;
        startButton.setText("Pause");
    }

    private void update() {
        updateDisplay();
        updateIntervalDisplay();
        updateToggleButtons();
    }

    // Method to reset the timer
    @FXML
    private void reset() {
        stop();
        createNewTimeline();
        update();
    }

    private void createNewTimeline() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> onTick()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeLeft = selectedTimer.getDurationByMode(currentMode);
    }


    // Method to handle timer completion
    private void nextInterval() {
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
        updateIntervalDisplay();
    }

    // Method to update the timer display
    private void updateDisplay(int timeLeft) {
        int minutes = timeLeft / 60;
        int seconds = timeLeft % 60;
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void updateDisplay() {
        updateDisplay(timeLeft);
    }

    public void onComplete() {
        // Handle timer completion logic
        nextInterval();
        reset();
        start();
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
        reset();
        timeLeft = 3;
        updateDisplay();
    }

}