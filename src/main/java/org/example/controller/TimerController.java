package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.model.TimerServiceFactory;
import org.example.model.dto.TimerDetails;
import org.example.service.interfaces.TimerService;
import org.example.manager.TimerGuiManager;
import org.example.manager.TimerListManager;

public class TimerController {

    @FXML private Label timerLabel;
    @FXML private Label intervalLabel;
    @FXML private Button startButton;
    @FXML private Button resetButton;
    @FXML private ToggleButton focusButton;
    @FXML private ToggleButton shortBreakButton;
    @FXML private ToggleButton longBreakButton;

    @FXML private ListView<TimerDetails> timerListView;

    private TimerGuiManager timerGuiManager;
    private TimerListManager listManager;

    @FXML
    public void initialize() {

        // Create TimerService
        // instantiating TimerService here for compatibility with future features.
        // we simply get factory class here, it chooses which logic will work.
        TimerService timerService = TimerServiceFactory.get();

        // Create TimerGui
        this.timerGuiManager = new TimerGuiManager(this);

        // Create ListManager
        this.listManager = new TimerListManager(this, timerService, timerGuiManager);
    }

    @FXML
    private void addSavedTimer() {
        listManager.addSavedTimer();
    }

    // Method to start or stop the timer
    @FXML
    private void startStop() {
        timerGuiManager.startStop();
    }

    // Method to reset the timer
    @FXML
    private void reset() {
        timerGuiManager.reset();
    }

    // Method to handle debug button action
    @FXML
    private void handleDebug() {
        // Set timer to 3 seconds for testing
        timerGuiManager.debug();
    }

    public void setTimerLabelText(String text) {
        this.timerLabel.setText(text);
    }

    public void setIntervalLabelText(String text) {
        this.intervalLabel.setText(text);
    }

    public void setStartButtonText(String text) {
        this.startButton.setText(text);
    }

    public String getStartButtonText() {
        return startButton.getText();
    }

    public ToggleButton getFocusButton() {
        return focusButton;
    }

    public ToggleButton getShortBreakButton() {
        return shortBreakButton;
    }

    public ToggleButton getLongBreakButton() {
        return longBreakButton;
    }

    public ListView<TimerDetails> getTimerListView() {
        return timerListView;
    }
}