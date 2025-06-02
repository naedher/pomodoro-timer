package org.example.ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import org.example.model.dto.TimerDetails;

public class TimerManager {

    private TimerMode currentMode;
    private int currentInterval;
    private int timeLeft;
    private Timeline timeline;
    private TimerDetails selectedTimer;
    private final TimerGuiManager timerGuiManager;

    public TimerManager(TimerGuiManager timerGuiManager) {
        this.timerGuiManager = timerGuiManager;
        this.currentMode = TimerMode.FOCUS;
        this.currentInterval = 1;

        // Create a default timer to be shown on startup
        this.selectedTimer = new TimerDetails(-1L, "Default timer", null, 25, 5, 15, 4);
        createNewTimeline();
    }

    public void start() {
        timeline.play();
    }
    
    public void stop() {
        timeline.stop();
    }

    private void onTick() {
        timeLeft--;
        if (timeLeft <= 0) {
            onComplete();
        } else {
            timerGuiManager.updateDisplay(timeLeft);
        }
    }


    // Handle timer completion logic
    public void onComplete() {
        timerGuiManager.playAlarmSound();
        nextInterval();
        reset();
        start();
        timerGuiManager.update();
    }

    // Method to handle timer completion
    private void nextInterval() {
        if (currentMode != TimerMode.FOCUS) {
            currentMode = TimerMode.FOCUS;
            return;
        }

        currentInterval++;
        if (currentInterval > selectedTimer.getPomodoroCount()) {
            currentInterval = 1;
            currentMode = TimerMode.LONG_BREAK;
        } else {
            currentMode = TimerMode.SHORT_BREAK;
        }
    }

    public void reset() {
        this.timeLeft = selectedTimer.getDurationByMode(currentMode);
    }

    private void createNewTimeline() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> onTick()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        reset();
    }

    public void setMode(TimerMode mode) {
        this.currentMode = mode;
    }

    public void setSelectedTimer(TimerDetails selectedTimer) {
        this.selectedTimer = selectedTimer;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public int getCurrentInterval() {
        return currentInterval;
    }

    public int getTotalIntervals() {
        return selectedTimer.getPomodoroCount();
    }

}

