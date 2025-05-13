package org.example.model.dto;

import java.time.LocalDateTime;

public class TimerDetails {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private Integer workDuration;
    private Integer shortBreakDuration;
    private Integer longBreakDuration;
    private Integer pomodoroCount;

    // Default constructor needed for JSON deserialization
    public TimerDetails() { }

    public TimerDetails(
            Long id,
            String name,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            Integer workDuration,
            Integer shortBreakDuration,
            Integer longBreakDuration,
            Integer pomodoroCount
    ) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.workDuration = workDuration;
        this.shortBreakDuration = shortBreakDuration;
        this.longBreakDuration = longBreakDuration;
        this.pomodoroCount = pomodoroCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getWorkDuration() {
        return workDuration;
    }

    public void setWorkDuration(Integer workDuration) {
        this.workDuration = workDuration;
    }

    public Integer getShortBreakDuration() {
        return shortBreakDuration;
    }

    public void setShortBreakDuration(Integer shortBreakDuration) {
        this.shortBreakDuration = shortBreakDuration;
    }

    public Integer getLongBreakDuration() {
        return longBreakDuration;
    }

    public void setLongBreakDuration(Integer longBreakDuration) {
        this.longBreakDuration = longBreakDuration;
    }

    public Integer getPomodoroCount() {
        return pomodoroCount;
    }

    public void setPomodoroCount(Integer pomodoroCount) {
        this.pomodoroCount = pomodoroCount;
    }
}