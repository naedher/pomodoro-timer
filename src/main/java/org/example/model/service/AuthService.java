package org.example.model.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.model.dto.AuthRequest;

import java.util.concurrent.CompletableFuture;

public interface AuthService {
    CompletableFuture<String> login(AuthRequest authRequest) throws JsonProcessingException;
    CompletableFuture<String> register(AuthRequest authRequest) throws JsonProcessingException;
}