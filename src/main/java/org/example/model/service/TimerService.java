package org.example.model.service;

import org.example.model.dto.TimerDetails;
import org.example.model.dto.TimerRequest;

import java.util.concurrent.CompletableFuture;

public interface TimerService {
    CompletableFuture<TimerDetails> getTimerDetails(long id);
    CompletableFuture<Void> createTimer(String name, int workDuration, int breakDuration, int pomodoroCount);
    CompletableFuture<Void> updateTimer(long id, TimerRequest request);
    CompletableFuture<Void> deleteTimer(long id);
}