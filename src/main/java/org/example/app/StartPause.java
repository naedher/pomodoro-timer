package org.example.app;

public enum StartPause {
    START("Start"),
    PAUSE("Pause");

    private final String buttonText;

    StartPause(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getButtonText() {
        return buttonText;
    }
}
