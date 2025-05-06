package org.example.model.dto;

import java.time.LocalDateTime;

public class TimerDetails {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer workDuration;
    private Integer breakDuration;
    private Integer pomodoroCount;

    // Default constructor needed for JSON deserialization
    public TimerDetails() {}

    public TimerDetails(Long id, String name, Integer workDuration, Integer breakDuration, Integer pomodoroCount) {
        this.id = id;
        this.name = name;
        this.workDuration = workDuration;
        this.breakDuration = breakDuration;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getWorkDuration() {
        return workDuration;
    }

    public void setWorkDuration(Integer workDuration) {
        this.workDuration = workDuration;
    }

    public Integer getBreakDuration() {
        return breakDuration;
    }

    public void setBreakDuration(Integer breakDuration) {
        this.breakDuration = breakDuration;
    }

    public Integer getPomodoroCount() {
        return pomodoroCount;
    }

    public void setPomodoroCount(Integer pomodoroCount) {
        this.pomodoroCount = pomodoroCount;
    }
}