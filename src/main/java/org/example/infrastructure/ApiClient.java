package org.example.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.exceptions.HttpExceptionFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class ApiClient {
    private static final URI BASE_URL = URI.create("https://pomodoro-timer.koyeb.app/");
    private final HttpClient client;
    private final ObjectMapper mapper;
    private String token;

    public ApiClient(String token) {
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        this.token = token;
    }

    public CompletableFuture<String> get(String path) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(BASE_URL.resolve(path))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(resp -> {
                    if (resp.statusCode() >= 200 && resp.statusCode() < 300) {
                        return resp.body();
                    }
                    throw HttpExceptionFactory.getHttpException(resp.statusCode(), resp.body());
                });
    }

    public CompletableFuture<String> post(String path, String body) {
        try {
            HttpRequest request;

            // Only add token header if token is not null (for authentication endpoints)
            if (token != null) {
                request = HttpRequest.newBuilder()
                        .uri(BASE_URL.resolve(path))
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + token)
                        .POST(HttpRequest.BodyPublishers.ofString(body))
                        .build();
            } else {
                request = HttpRequest.newBuilder()
                        .uri(BASE_URL.resolve(path))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(body))
                        .build();
            }

            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(resp -> {
                        if (resp.statusCode() >= 200 && resp.statusCode() < 300) {
                            return resp.body();
                        }
                        throw HttpExceptionFactory.getHttpException(resp.statusCode(), resp.body());
                    });
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    public CompletableFuture<String> put(String path, String body) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(BASE_URL.resolve(path))
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(resp -> {
                        if (resp.statusCode() >= 200 && resp.statusCode() < 300) {
                            return resp.body();
                        }
                        throw HttpExceptionFactory.getHttpException(resp.statusCode(), resp.body());
                    });
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    public CompletableFuture<Void> delete(String path) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(BASE_URL.resolve(path))
                .header("Authorization", "Bearer " + token)
                .DELETE()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(resp -> {
                    if (resp.statusCode() >= 200 && resp.statusCode() < 300) {
                        return null;
                    }
                    throw HttpExceptionFactory.getHttpException(resp.statusCode(), resp.body());
                });
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getClient() {
        return client.toString();
    }
}