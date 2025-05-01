package org.example.api;

public class TimerRequest {
    private String name;
    private int workDuration;
    private int breakDuration;
    private int pomodoroCount;

    public TimerRequest(String name, int workDuration, int breakDuration, int pomodoroCount) {
        this.name = name;
        this.workDuration = workDuration;
        this.breakDuration = breakDuration;
        this.pomodoroCount = pomodoroCount;
    }
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
}
