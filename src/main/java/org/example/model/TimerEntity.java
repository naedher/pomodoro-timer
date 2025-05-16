package org.example.model;

import java.time.LocalDateTime;

public class TimerEntity {

    private long id;
    private String name;
    private LocalDateTime createdAt;
    private int workDuration;
    private int shortBreakDuration;
    private int longBreakDuration;
    private int pomodoroCount;


    public TimerEntity(long id, String name, int workDuration, int shortBreakDuration, int longBreakDuration, int pomodoroCount) {
        this.id = id;
        this.name = name;
        this.createdAt = LocalDateTime.now();
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
    }}
