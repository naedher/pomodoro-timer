package org.example;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class TimerManager {
    private Timeline timeline;
    private boolean running = false;
    private int timeLeft;
    private TimerCallback callback;

    public TimerManager(int initialTime, TimerCallback callback) {
        this.timeLeft = initialTime;
        this.callback = callback;
    }

    // Start the timer
    public void start() {
        if (running) return; // Prevent starting if already running

        if (timeline != null) timeline.stop(); // Stop any existing timeline

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> { // Update every second
            timeLeft--;
            if (callback != null) callback.onTick(timeLeft); // Notify about time left
            if (timeLeft <= 0) { // Timer completed
                stop();
                if (callback != null) callback.onComplete(); // Notify completion
            }
        }));

        timeline.setCycleCount(Timeline.INDEFINITE); // Repeat indefinitely
        timeline.play();
        running = true;
    }

    public void stop() {
        if (timeline != null) timeline.stop();
        running = false;
    }

    public void reset(int newTime) {
        stop();
        timeLeft = newTime;
        if (callback != null) callback.onTick(timeLeft); // Notify about reset time
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(int seconds) {
        this.timeLeft = seconds;
    }

    public boolean isRunning() {
        return running;
    }
}
