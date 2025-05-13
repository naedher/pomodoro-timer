package org.example.ui;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class TimerController implements TimerCallback {

    @FXML private Label timerLabel, intervalLabel; // Labels for timer and interval display
    @FXML private Button startButton, resetButton; // Start and reset buttons
    @FXML private ToggleButton focusButton, shortBreakButton, longBreakButton; // Toggle buttons for timer modes
    @FXML private Button debugButton; // Debug button for testing
    @FXML private ListView<String> timerListView; // List view for saved timers
    @FXML private Button addTimerButton; // Button to add a saved timer
    private Timer timer;

    // Initialize the timer and set up the UI
    @FXML
    public void initialize() {
        timer = new Timer(4, this); // deafult interval 4
        updateDisplay(timer.getTimeLeft()); // Update the timer display
        updateIntervalDisplay();
    }

    // Set up the timer when the scene is shown
    @FXML
    private void startStop() {
        if (timer.isRunning()) {
            timer.pause();
            startButton.setText("START");
        } else {
            timer.start();
            startButton.setText("PAUSE");
        }
    }

    // Reset the timer to its initial state
    @FXML
    private void reset() {
        timer.resetCurrentMode();
        startButton.setText("START");
        updateDisplay(timer.getTimeLeft());
        updateIntervalDisplay();
    }

    // Set the timer mode to focus
    @FXML
    private void setFocus() {
        timer.setMode(TimerMode.FOCUS);
        updateToggleButtons(TimerMode.FOCUS);
    }

    // Set the timer mode to short break
    @FXML
    private void setShortBreak() {
        timer.setMode(TimerMode.SHORT_BREAK);
        updateToggleButtons(TimerMode.SHORT_BREAK);
    }

    // Set the timer mode to long break
    @FXML
    private void setLongBreak() {
        timer.setMode(TimerMode.LONG_BREAK);
        updateToggleButtons(TimerMode.LONG_BREAK);
    }

    // Update the timer display with the remaining time
    private void updateDisplay(int timeLeft) {
        int minutes = timeLeft / 60; // Calculate minutes
        int seconds = timeLeft % 60; // Calculate seconds
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
    }

    // Update the interval display with the current interval and total intervals
    private void updateIntervalDisplay() {
        intervalLabel.setText("Interval: " + timer.getCurrentInterval() + "/" + timer.getTotalIntervals());
    }

    // Update the toggle buttons based on the current mode
    private void updateToggleButtons(TimerMode mode) {
        focusButton.setSelected(mode == TimerMode.FOCUS);
        shortBreakButton.setSelected(mode == TimerMode.SHORT_BREAK);
        longBreakButton.setSelected(mode == TimerMode.LONG_BREAK);
    }

    // Update the display when the timer ticks
    @Override
    public void onTick(int timeLeft) {
        updateDisplay(timeLeft);
    }

    // Update the display when the timer completes
    @Override
    public void onComplete() {
        startButton.setText("Start");
        updateIntervalDisplay();
        updateToggleButtons(timer.getCurrentMode());
    }

    // Add a saved timer to the list view
    @FXML
    private void addSavedTimer() {
        // Empty for now. Will add logic when API connection is implemented
        System.out.println("Add timer button clicked");
    }

    // debug button for testing (will be removed before release)
    @FXML
    private void debugButton() {
        timer.setTimeLeft(3); // Set time left to 3 seconds for testing
        updateDisplay(timer.getTimeLeft());
    }
}
