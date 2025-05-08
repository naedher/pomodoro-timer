package org.example.model.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.infrastructure.ApiClient;
import org.example.model.dto.TimerCreate;
import org.example.model.dto.TimerDetails;
import org.example.model.dto.TimerUpdate;
import org.example.model.service.TimerService;

import java.util.concurrent.CompletableFuture;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class TimerServiceImpl implements TimerService {
    private final ApiClient apiClient;
    private ObjectMapper mapper;


    public TimerServiceImpl(String token) {
        this.apiClient = new ApiClient(token);
        this.mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    public void setObjectMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public CompletableFuture<TimerDetails> getTimerDetails(long id) {
        return apiClient.get("/timers/" + id)
                .thenApply(response -> {
                    try {
                        return mapper.readValue(response, TimerDetails.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public CompletableFuture<Void> createTimer(TimerCreate timer) throws JsonProcessingException {
        String jsonBody = mapper.writeValueAsString(timer);
        apiClient.post("/timers", jsonBody);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> updateTimer(long id, TimerUpdate request) throws JsonProcessingException {
        String jsonBody = mapper.writeValueAsString(request);
        apiClient.put("/timers/" + id, jsonBody);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> deleteTimer(long id) {
        apiClient.delete("/timers/" + id);
        return CompletableFuture.completedFuture(null);
    }
}