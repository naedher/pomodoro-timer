package org.example.model.dto;

import java.time.LocalDateTime;

public class TimerUpdate {
    private String name;
    private int workDuration;
    private int breakDuration;
    private int pomodoroCount;

    public TimerUpdate(String name, LocalDateTime createdAt,int workDuration, int breakDuration, int pomodoroCount) {
        this.name = name;
        this.workDuration = workDuration;
        this.breakDuration = breakDuration;
        this.pomodoroCount = pomodoroCount;
    }

    public TimerUpdate() { }

    public String getName() {
        return name;
    }

    public int getWorkDuration() {
        return workDuration;
    }

    public int getBreakDuration() {
        return breakDuration;
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

    public void setBreakDuration(int breakDuration) {
        this.breakDuration = breakDuration;
    }

    public void setPomodoroCount(int pomodoroCount) {
        this.pomodoroCount = pomodoroCount;
    }
}
