package org.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LoginController {

    public void sendLoginRequest(String email, String password) {
        try {
            HttpClient client = HttpClient.newHttpClient();


            String json = String.format("""
                    {
                      "email": "%s",
                      "password": "%s"
                    }
                    """, email, password);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8081/api/auth/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        System.out.println("Status code: " + response.statusCode());
                        System.out.println("Response body: " + response.body());
                    })
                    .exceptionally(e -> {
                        System.out.println("Problem with request: " + e.getMessage());
                        return null;
                    });

        } catch (Exception e) {
            System.out.println("Couldn't send request: " + e.getMessage());
        }
    }
}
