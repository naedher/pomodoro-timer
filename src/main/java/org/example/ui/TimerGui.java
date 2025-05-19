package org.example.ui;

import javafx.application.Platform;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import org.example.model.dto.TimerDetails;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class TimerGui {

    private final TimerController timerController;
    private final TimerManager timerManager;

    private final Map<ToggleButton, TimerMode> modeMapping;

    private ToggleButton lastButtonSelected;

    public TimerGui(TimerController timerController) {
        this.timerController = timerController;
        this.timerManager = new TimerManager(this);

        this.lastButtonSelected = timerController.getFocusButton();

        this.modeMapping = initModeMapping();
        initModeGroup();

        update();
    }

    private Map<ToggleButton, TimerMode> initModeMapping() {
        Map<ToggleButton, TimerMode> buttonTimerModeMap = new HashMap<>();

        buttonTimerModeMap.put(timerController.getFocusButton(), TimerMode.FOCUS);
        buttonTimerModeMap.put(timerController.getShortBreakButton(), TimerMode.SHORT_BREAK);
        buttonTimerModeMap.put(timerController.getLongBreakButton(), TimerMode.LONG_BREAK);

        return buttonTimerModeMap;
    }

    public void initModeGroup() {
        ToggleGroup group = new ToggleGroup();
        timerController.getFocusButton().setToggleGroup(group);
        timerController.getShortBreakButton().setToggleGroup(group);
        timerController.getLongBreakButton().setToggleGroup(group);

        group.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == lastButtonSelected || newToggle == null || oldToggle == null) {
                // do nothing
                Platform.runLater(() -> group.selectToggle(lastButtonSelected));
            } else {
                ToggleButton selected = (ToggleButton) newToggle;
                handleModeButton(selected);
                lastButtonSelected = selected;
            }
        });
    }

    private void handleModeButton(ToggleButton button) {
        TimerMode mode = modeMapping.get(button);
        timerManager.setMode(mode);
        reset();
        setStartButtonText(StartPause.START);
    }

    public void startStop() {
        if (isRunning()) {
            stop();
        } else {
            start();
        }
    }

    private boolean isRunning() {
        return timerController.getStartButtonText().equals(StartPause.PAUSE.getButtonText());
    }

    private void start() {
        timerManager.start();
        setStartButtonText(StartPause.PAUSE);
    }

    private void stop() {
        timerManager.stop();
        setStartButtonText(StartPause.START);
    }

    public void reset() {
        stop();
        timerManager.reset();
        updateDisplay();
    }

    public void update() {
        updateIntervalDisplay();
        updateDisplay();
    }

    private void updateDisplay() {
        int timeLeft = timerManager.getTimeLeft();
        updateDisplay(timeLeft);
    }

    // Method to update the timer display
    public void updateDisplay(int timeLeft) {
        int minutes = timeLeft / 60;
        int seconds = timeLeft % 60;
        timerController.setTimerLabelText(String.format("%02d:%02d", minutes, seconds));
    }

    private void updateIntervalDisplay() {
        int currentInterval = timerManager.getCurrentInterval();
        int totalIntervals = timerManager.getTotalIntervals();
        updateIntervalDisplay(currentInterval, totalIntervals);
    }

    // Method to update the interval display
    public void updateIntervalDisplay(int currentInterval, int totalIntervals) {
        timerController.setIntervalLabelText(String.format("Interval: %d/%d", currentInterval, totalIntervals));
    }


    public void setStartButtonText(StartPause startPause) {
        timerController.setStartButtonText(startPause.getButtonText());
    }

    public void setSelectedTimer(TimerDetails timerDetails) {
        timerManager.setSelectedTimer(timerDetails);
    }

    public void playAlarmSound() {
        try {
            // Load the sound file from the classpath
            InputStream audio = getClass().getResourceAsStream("/sound/alarm1.wav");
            // If there's no sound file
            if (audio == null) {
                System.err.println("Sound file not found");
                return;
            }
            // Create buffered stream for efficiency
            InputStream bufferedIn = new BufferedInputStream(audio);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn); // Create audio input stream
            Clip clip = AudioSystem.getClip(); // Create a clip to play the sound
            clip.open(audioStream); // Open the audio input stream
            clip.start(); // Start playing the sound
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void debug() {
        timerManager.setTimeLeft(3);
        updateDisplay();
    }
}
