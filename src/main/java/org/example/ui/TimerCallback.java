package org.example.ui;

// Used to notify the UI about completion and time left
public interface TimerCallback {
    void onTick(int timeLeft);
    void onComplete();
}
