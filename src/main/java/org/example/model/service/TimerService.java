package org.example.model.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.model.dto.TimerCreate;
import org.example.model.dto.TimerDetails;
import org.example.model.dto.TimerUpdate;

import java.util.concurrent.CompletableFuture;

public interface TimerService {
    CompletableFuture<TimerDetails> getTimerDetails(long id);
    CompletableFuture<Void> createTimer(TimerCreate timer) throws JsonProcessingException;
    CompletableFuture<Void> updateTimer(long id, TimerUpdate request) throws JsonProcessingException;
    CompletableFuture<Void> deleteTimer(long id);
}