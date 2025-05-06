package org.example.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class ApiClient {
    private static final String BASE_URL = "https://pomodoro-timer.koyeb.app/api";
    private final HttpClient client;
    private final ObjectMapper mapper;
    private String token;

    public ApiClient(String token) {
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        this.token = token;
    }

    public <T> CompletableFuture<T> get(String path, Class<T> responseType) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(resp -> {
                    if (resp.statusCode() < 200 || resp.statusCode() >= 300) {
                        throw new RuntimeException("Failed to get data: HTTP " + resp.statusCode() + " - " + resp.body());
                    }
                    try {
                        return mapper.readValue(resp.body(), responseType);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse response: " + e.getMessage() + ", Body: " + resp.body());
                    }
                });
    }

    public <T> CompletableFuture<T> post(String path, Object body, Class<T> responseType) {
        try {
            String jsonBody = mapper.writeValueAsString(body);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + path))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            // Only add token header if token is not null (for authentication endpoints)
            if (token != null) {
                request = HttpRequest.newBuilder(request.uri())
                        .headers(request.headers().map().entrySet().stream()
                                .flatMap(entry -> entry.getValue().stream()
                                        .map(value -> new String[]{entry.getKey(), value}))
                                .flatMap(java.util.Arrays::stream)
                                .toArray(String[]::new))
                        .header("Authorization", "Bearer " + token)
                        .method(request.method(), request.bodyPublisher().get())
                        .build();
            }

            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(resp -> {
                        if (resp.statusCode() < 200 || resp.statusCode() >= 300) {
                            throw new RuntimeException("Failed to create data: HTTP " + resp.statusCode() + " - " + resp.body());
                        }
                        try {
                            if (responseType == String.class) {
                                return (T) resp.body();
                            }
                            return mapper.readValue(resp.body(), responseType);
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to parse response: " + e.getMessage() + ", Body: " + resp.body());
                        }
                    });
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    public CompletableFuture<Void> put(String path, Object body) {
        try {
            String jsonBody = mapper.writeValueAsString(body);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + path))
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(resp -> {
                        if (resp.statusCode() < 200 || resp.statusCode() >= 300) {
                            throw new RuntimeException("Failed to update data: HTTP " + resp.statusCode() + " - " + resp.body());
                        }
                        return null;
                    });
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    public CompletableFuture<Void> delete(String path) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + path))
                .header("Authorization", "Bearer " + token)
                .DELETE()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(resp -> {
                    if (resp.statusCode() < 200 || resp.statusCode() >= 300) {
                        throw new RuntimeException("Failed to delete data: HTTP " + resp.statusCode() + " - " + resp.body());
                    }
                    return null;
                });
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}