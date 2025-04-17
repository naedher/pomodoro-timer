package org.example;

import javafx.application.Platform;

public class timerTask extends Thread {
    private int timeLeft;
    private final Runnable onTick;  // Funktion som körs varje sekund
    private final Runnable onFinish; // Funktion som körs när tiden är slut
    private boolean running = true;

    public timerTask(int seconds, Runnable onTick, Runnable onFinish) {
        this.timeLeft = seconds;
        this.onTick = onTick;
        this.onFinish = onFinish;
        setDaemon(true);
    }

    public void stopTimer() {
        running = false;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    @Override
    public void run() {
        while (timeLeft > 0 && running) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }

            timeLeft--;

            // Uppdatera UI från tråden (JavaFX kräver att detta görs på huvudtråden)
            Platform.runLater(onTick);
        }

        if (running && timeLeft <= 0) {
            Platform.runLater(onFinish);
        }
    }
}
