package org.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.CompletableFuture;

import org.example.model.dto.AuthRequest;
import org.example.model.dto.TimerCreate;
import org.example.model.dto.TimerDetails;
import org.example.model.dto.TimerUpdate;
import org.example.model.service.impl.AuthServiceImpl;
import org.example.model.service.impl.TimerServiceImpl;

public class Main {
    private AuthServiceImpl authService;
    private TimerServiceImpl timerService;
    private String authToken;
    private ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        Main main = new Main();
        main.authService = new AuthServiceImpl();

        main.testRegister();
        System.out.println("Register testing finished.");

        main.testLogin();
        System.out.println("Login testing finished.");

        if (main.timerService != null) {
            main.testCreateTimer();
            System.out.println("Create timer testing finished.");

            main.testGetTimerDetails();
            System.out.println("Get timer details testing finished.");

            main.testUpdateTimer();
            System.out.println("Update timer testing finished.");

            main.testDeleteTimer();
            System.out.println("Delete timer testing finished.");
        } else {
            System.out.println("Timer operations skipped due to failed authentication.");
        }
    }

    public void testRegister() {
        System.out.println("Testing registration...");
        CompletableFuture<String> registerFuture = null;
        try {
            registerFuture = authService.register(new AuthRequest("test123456@example.com", "testpassword"));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        registerFuture.thenAccept(token -> {
            System.out.println("Registration successful! Token: " + token.substring(0, 10) + "...");
            this.authToken = token;
            this.timerService = new TimerServiceImpl(token);
        }).exceptionally(throwable -> {
            System.out.println("Registration failed: " + throwable.getMessage());
            return null;
        }).join();
    }

    public void testLogin() {
        System.out.println("Testing login...");
        CompletableFuture<String> loginFuture = null;
        try {
            loginFuture = authService.login(new AuthRequest("test123@example.com", "testpassword"));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        loginFuture.thenAccept(token -> {
            System.out.println("Login successful! Token: " + token.substring(0, 10) + "...");
            this.authToken = token;
            this.timerService = new TimerServiceImpl(token);
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            System.out.println("Login failed: " + throwable.getMessage());
            return null;
        }).join();
    }

    public void testCreateTimer() {
        if (timerService == null) {
            System.out.println("Timer service not initialized. Please login first.");
            return;
        }
        System.out.println("Testing create timer...");

        String name = "Test Timer " + System.currentTimeMillis();
        int workDuration = 25;
        int shortBreakDuration = 5;
        int longBreakDuration = 15;
        int pomodoroCount = 4;

        System.out.println("Creating timer with: name=" + name +
                ", workDuration=" + workDuration +
                ", shortBreakDuration=" + shortBreakDuration +
                ", longBreakDuration=" + longBreakDuration +
                ", pomodoroCount=" + pomodoroCount);

        CompletableFuture<Void> createFuture = null;
        try {
            createFuture = timerService.createTimer(new TimerCreate(name, workDuration, shortBreakDuration, longBreakDuration, pomodoroCount));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        createFuture.thenAccept(v -> {
            System.out.println("Timer created successfully!");
        }).exceptionally(throwable -> {
            System.out.println("Failed to create timer: " + throwable.getMessage());
            return null;
        }).join();
    }

    public void testGetTimerDetails() {
        if (timerService == null) {
            System.out.println("Timer service not initialized. Please login first.");
            return;
        }

        long timerId = 28;
        System.out.println("Testing get timer details for ID: " + timerId);

        CompletableFuture<TimerDetails> detailsFuture = timerService.getTimerDetails(timerId);
        detailsFuture.thenAccept(details -> {
            System.out.println("Timer details retrieved successfully!");
            try {
                System.out.println("Timer details as JSON: " + mapper.writeValueAsString(details));
            } catch (Exception e) {
                System.out.println("Could not serialize details to JSON: " + e.getMessage());
            }
            System.out.println("ID: " + details.getId());
            System.out.println("Name: " + details.getName());
            System.out.println("Work Duration: " + details.getWorkDuration());
            System.out.println("Short Break Duration: " + details.getShortBreakDuration());
            System.out.println("Long Break Duration: " + details.getLongBreakDuration());
            System.out.println("Pomodoro Count: " + details.getPomodoroCount());
        }).exceptionally(throwable -> {
            System.out.println("Failed to get timer details: " + throwable.getMessage());
            return null;
        }).join();
    }

    public void testUpdateTimer() {
        if (timerService == null) {
            System.out.println("Timer service not initialized. Please login first.");
            return;
        }

        long timerId = 25;
        System.out.println("Testing update timer for ID: " + timerId);

        TimerUpdate update = new TimerUpdate(
                "Updated Timer",
                null,
                30,
                10,
                20,
                6
        );

        System.out.println("Updating timer with:" +
                " Name=" + update.getName() +
                ", workDuration=" + update.getWorkDuration() +
                ", shortBreakDuration=" + update.getShortBreakDuration() +
                ", longBreakDuration=" + update.getLongBreakDuration() +
                ", pomodoroCount=" + update.getPomodoroCount());

        CompletableFuture<Void> updateFuture = null;
        try {
            updateFuture = timerService.updateTimer(timerId, update);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        updateFuture.thenAccept(v -> {
            System.out.println("Timer updated successfully!");
        }).exceptionally(throwable -> {
            System.out.println("Failed to update timer: " + throwable.getMessage());
            return null;
        }).join();
    }

    public void testDeleteTimer() {
        if (timerService == null) {
            System.out.println("Timer service not initialized. Please login first.");
            return;
        }

        long timerId = 243;
        System.out.println("Testing delete timer for ID: " + timerId);

        CompletableFuture<Void> deleteFuture = timerService.deleteTimer(timerId);
        deleteFuture.thenAccept(v -> {
            System.out.println("Timer deleted successfully!");
        }).exceptionally(throwable -> {
            System.out.println("Failed to delete timer: " + throwable.getMessage());
            return null;
        }).join();
    }
}