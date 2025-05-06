package org.example.model.service;

import java.util.concurrent.CompletableFuture;

public interface AuthService {
    CompletableFuture<String> login(String email, String password);
    CompletableFuture<String> register(String email, String password);
}