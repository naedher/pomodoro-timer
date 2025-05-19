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
import java.util.List;

public class RemoteTimerService implements TimerService {
    private final ApiClient apiClient;
    private ObjectMapper mapper;


    public RemoteTimerService(String token) {
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
    public CompletableFuture<List<TimerDetails>> getUserTimers() {
        return apiClient.get("/timers")
                .thenApply(response -> {
                    try {
                        return mapper.readValue(response, 
                            mapper.getTypeFactory().constructCollectionType(List.class, TimerDetails.class));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public CompletableFuture<TimerDetails> createTimer(TimerCreate timer) {
        String jsonBody;
        try {
            jsonBody = mapper.writeValueAsString(timer);
        } catch (JsonProcessingException e) {
            return CompletableFuture.failedFuture(e);
        }
        return apiClient.postWithHeaders("/timers", jsonBody)
                .thenCompose(resp -> {
                    String location = resp.headers().firstValue("location")
                        .orElseThrow(() -> new RuntimeException("No Location header in response"));
                    String id = location.substring(location.lastIndexOf('/') + 1);
                    return getTimerDetails(Long.parseLong(id));
                });
    }

    @Override
    public CompletableFuture<TimerDetails> updateTimer(long id, TimerUpdate request) {
        String jsonBody;
        try {
            jsonBody = mapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return apiClient.put("/timers/" + id, jsonBody)
                .thenApply(response -> {
                    try {
                        return mapper.readValue(response, TimerDetails.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public CompletableFuture<Void> deleteTimer(long id) {
        apiClient.delete("/timers/" + id);
        return CompletableFuture.completedFuture(null);
    }
}
