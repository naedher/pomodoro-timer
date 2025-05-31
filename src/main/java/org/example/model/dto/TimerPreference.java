package org.example.model.dto;

public class TimerPreference {
    private boolean enabledSound;
    private String theme;
    private int alarmNumber;

    public TimerPreference() { }

    public TimerPreference(boolean enabledSound, String theme, int alarmNumber) {
        this.enabledSound = enabledSound;
        this.theme = theme;
        this.alarmNumber = alarmNumber;
    }

    public boolean isEnabledSound() {
        return enabledSound;
    }
    public void setEnabledSound(boolean enabledSound) {
        this.enabledSound = enabledSound;
    }
    public String getTheme() {
        return theme;
    }
    public void setTheme(String theme) {
        this.theme = theme;
    }
    public int getAlarmNumber() {
        return alarmNumber;
    }
    public void setAlarmNumber(int alarmNumber) {
        this.alarmNumber = alarmNumber;
    }

}
