package org.example.model.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.infrastructure.ApiClient;
import org.example.model.dto.LoginRequest;
import org.example.model.dto.RegisterRequest;
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
    public CompletableFuture<String> login(String email, String password) {
        LoginRequest request = new LoginRequest(email, password);
        return apiClient.post("/auth/login", request, String.class)
                .thenApply(response -> {
                    try {
                        return mapper.readTree(response).get("token").asText();
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse response: " + e.getMessage() + ", Response: " + response);
                    }
                });
    }

    @Override
    public CompletableFuture<String> register(String email, String password) {
        RegisterRequest request = new RegisterRequest(email, password);
        return apiClient.post("/auth/register", request, String.class)
                .thenApply(response -> {
                    try {
                        return mapper.readTree(response).get("token").asText();
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse response: " + e.getMessage() + ", Response: " + response);
                    }
                });
    }
}