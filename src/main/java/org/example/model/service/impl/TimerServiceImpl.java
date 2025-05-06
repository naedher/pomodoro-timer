package org.example.model.service.impl;

import org.example.infrastructure.ApiClient;
import org.example.model.dto.TimerDetails;
import org.example.model.dto.TimerRequest;
import org.example.model.service.TimerService;

import java.util.concurrent.CompletableFuture;
import java.util.HashMap;
import java.util.Map;

public class TimerServiceImpl implements TimerService {
    private final ApiClient apiClient;

    public TimerServiceImpl(String token) {
        this.apiClient = new ApiClient(token);
    }

    @Override
    public CompletableFuture<TimerDetails> getTimerDetails(long id) {
        return apiClient.get("/timers/" + id, TimerDetails.class);
    }

    @Override
    public CompletableFuture<Void> createTimer(String name, int workDuration, int breakDuration, int pomodoroCount) {
        // Create a map for the request body to match the backend's TimerCreateRequest format
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("name", name);
        requestMap.put("workDuration", workDuration);
        requestMap.put("breakDuration", breakDuration);
        requestMap.put("pomodoroCount", pomodoroCount);

        return apiClient.post("/timers", requestMap, Void.class);
    }

    @Override
    public CompletableFuture<Void> updateTimer(long id, TimerRequest request) {
        // Create a map for the request body to match the backend's TimerUpdateRequest format
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("name", request.getName());
        requestMap.put("workDuration", request.getWorkDuration());
        requestMap.put("breakDuration", request.getBreakDuration());
        requestMap.put("pomodoroCount", request.getPomodoroCount());

        return apiClient.put("/timers/" + id, requestMap);
    }

    @Override
    public CompletableFuture<Void> deleteTimer(long id) {
        return apiClient.delete("/timers/" + id);
    }
}