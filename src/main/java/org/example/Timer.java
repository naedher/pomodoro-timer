package org.example;

public class Timer {
    private TimerMode currentMode = TimerMode.FOCUS; // Default mode
    private int totalIntervals;
    private int currentInterval = 1; // Start with the first interval

    private TimerCallback callback;
    private TimerManager timerManager;

    public Timer(int totalIntervals, TimerCallback callback) {
        this.totalIntervals = totalIntervals;
        this.callback = callback;

        // Initialize timeManager with callback
        this.timerManager = new TimerManager(currentMode.getDuration(), new TimerCallback() {

            // Update UI with time left
            @Override
            public void onTick(int timeLeft) {
                if (callback != null) {
                    callback.onTick(timeLeft);
                }
            }

            // Handle timer completion
            @Override
            public void onComplete() {
                handleTimerComplete(); // Change mode and reset
                if (callback != null) {
                    callback.onComplete(); // Notify completion
                }
                timerManager.reset(currentMode.getDuration()); // update duration for new mode
                timerManager.start(); // auto-start next mode
            }
        });
        timerManager.reset(currentMode.getDuration()); // Initialize with current mode duration
    }

    // Start the timer
    public void start() {
        timerManager.start();
    }

    // Pause the timer
    public void pause() {
        timerManager.stop();
    }

    // Reset the timer to the current mode's duration
    public void resetCurrentMode() {
        timerManager.reset(currentMode.getDuration());
    }

    // Reset the timer to the initial state
    public void setMode(TimerMode mode) {
        currentMode = mode;
        resetCurrentMode();
    }

    // Set the timer to a specific duration (for debugging or testing)
    public void setTimeLeft(int seconds) {
        timerManager.setTimeLeft(seconds);
    }

    public int getTimeLeft() {
        return timerManager.getTimeLeft();
    }

    public boolean isRunning() {
        return timerManager.isRunning();
    }

    public TimerMode getCurrentMode() {
        return currentMode;
    }

    public int getCurrentInterval() {
        return currentInterval;
    }

    public int getTotalIntervals() {
        return totalIntervals;
    }

    // Called when a timer finishes
    public void handleTimerComplete() {
        if (currentMode == TimerMode.FOCUS) {
            if (currentInterval < totalIntervals) { // Check if we are not at the last interval
                currentInterval++;
                setMode(TimerMode.SHORT_BREAK);
            } else {
                setMode(TimerMode.LONG_BREAK); // if we are at the last interval, switch to long break
            }
        } else if (currentMode == TimerMode.SHORT_BREAK || currentMode == TimerMode.LONG_BREAK) { // if we are in a break
            if (currentMode == TimerMode.LONG_BREAK) {
                currentInterval = 1; // Reset intervals after long break
            }
            setMode(TimerMode.FOCUS); // Switch back to focus mode
        }
    }
}
