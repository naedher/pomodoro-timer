package org.example.model;

import java.time.LocalDateTime;

public class TimerUpdate {
    private String name;
    private int workDuration;
    private int shortBreakDuration;
    private int longBreakDuration;
    private int pomodoroCount;

    public TimerUpdate(String name, int workDuration, int shortBreakDuration, int longBreakDuration, int pomodoroCount) {
        this.name = name;
        this.workDuration = workDuration;
        this.shortBreakDuration = shortBreakDuration;
        this.longBreakDuration = longBreakDuration;
        this.pomodoroCount = pomodoroCount;
    }

    public TimerUpdate() { }

    public String getName() {
        return name;
    }

    public int getWorkDuration() {
        return workDuration;
    }

    public int getShortBreakDuration() {
        return shortBreakDuration;
    }

    public int getLongBreakDuration() {
        return longBreakDuration;
    }

    public int getPomodoroCount() {
        return pomodoroCount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWorkDuration(int workDuration) {
        this.workDuration = workDuration;
    }

    public void setShortBreakDuration(int shortBreakDuration) {
        this.shortBreakDuration = shortBreakDuration;
    }

    public void setLongBreakDuration(int longBreakDuration) {
        this.longBreakDuration = longBreakDuration;
    }

    public void setPomodoroCount(int pomodoroCount) {
        this.pomodoroCount = pomodoroCount;
    }
}
