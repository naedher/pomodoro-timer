package org.example.demo.api;

import org.example.demo.exception.*;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Logger;

public class Connection {

    private static final Logger logger = Logger.getLogger(Connection.class.getName());
    private final HttpClient client;
    private final URI baseUrl;

    public Connection() {
        this("https://pomodoro-timer.koyeb.app");
    }

    public Connection(String baseUrl) {
        this.baseUrl = URI.create(baseUrl);
        this.client = HttpClient.newHttpClient();
    }

    public JSONObject post(String path, String body) throws HttpException {
        // Build the http request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(baseUrl.resolve(path))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        try {
            // Send request, get response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) { // Successful
                // Return the response body as a JSON object
                return new JSONObject(response.body());
            }
            // request failed, throw exception
            logger.severe("HTTP error " + response.statusCode() + ": " + response.body());
            throw HttpExceptionFactory.getHttpException(response.statusCode(), response.body());

        } catch (IOException | InterruptedException e) { // connection failed, or other IO error
            throw new NetworkException("Failed to connect to server", e);
        }
    }
}
