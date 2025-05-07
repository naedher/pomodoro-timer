package org.example;

public enum TimerMode {
    FOCUS("FOCUS", 25 * 60), // 25 minutes
    SHORT_BREAK("SHORT BREAK", 5 * 60), // 5 minutes
    LONG_BREAK("LONG BREAK", 15 * 60); // 15 minutes

    private String label;
    private int duration;

    TimerMode(String label, int duration) {
        this.label = label;
        this.duration = duration;
    }

    public String getLabel() {
        return label;
    }

    public int getDuration() {
        return duration;
    }
}
