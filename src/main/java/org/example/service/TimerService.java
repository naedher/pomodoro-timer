package org.example.service;

import org.example.model.dto.TimerCreate;
import org.example.model.dto.TimerDetails;
import org.example.model.dto.TimerUpdate;

import java.util.concurrent.CompletableFuture;
import java.util.List;

public interface TimerService {
    CompletableFuture<TimerDetails> getTimerDetails(long id);
    CompletableFuture<TimerDetails> createTimer(TimerCreate timer);
    CompletableFuture<TimerDetails> updateTimer(long id, TimerUpdate request);
    CompletableFuture<Void> deleteTimer(long id);
    CompletableFuture<List<TimerDetails>> getUserTimers();
}