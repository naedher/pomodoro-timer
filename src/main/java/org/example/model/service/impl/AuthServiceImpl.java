package org.example.model.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.infrastructure.ApiClient;
import org.example.model.dto.AuthRequest;
import org.example.model.service.AuthService;

import java.util.concurrent.CompletableFuture;

public class AuthServiceImpl implements AuthService {
    private final ApiClient apiClient;
    private final ObjectMapper mapper;

    public AuthServiceImpl() {
        this.apiClient = new ApiClient(null); // No token for authentication endpoints
        this.mapper = new ObjectMapper();
    }

    @Override
    public CompletableFuture<String> login(AuthRequest authRequest) throws JsonProcessingException {
        String jsonBody = mapper.writeValueAsString(authRequest);
        return apiClient.post("/api/auth/login", jsonBody)
                .thenApply(response -> {
                    try {
                        return mapper.readTree(response).get("token").asText();
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse response: " + e.getMessage() + ", Response: " + response);
                    }
                });
    }

    @Override
    public CompletableFuture<String> register(AuthRequest authRequest) throws JsonProcessingException {
        String jsonBody = mapper.writeValueAsString(authRequest);
        return apiClient.post("/api/auth/register", jsonBody)
                .thenApply(response -> {
                    try {
                        return mapper.readTree(response).get("token").asText();
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse response: " + e.getMessage() + ", Response: " + response);
                    }
                });
    }
}