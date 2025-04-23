// src/main/java/org/example/LoginController.java
package org.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

import org.json.JSONObject;

public class LoginController {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String loginUrl = "http://localhost:8081/api/auth/login";

    public CompletableFuture<String> sendLoginRequest(String email, String password) {
        String jsonBody = String.format("""
            {
              "email": "%s",
              "password": "%s"
            }
            """, email, password);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(loginUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(resp -> {
                    int status = resp.statusCode();
                    if (status != 200) {
                        throw new RuntimeException("Login failed: HTTP " + status);
                    }
                    JSONObject obj = new JSONObject(resp.body());
                    return obj.getString("token");
                });
    }
}

