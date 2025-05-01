package org.example.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;
import java.net.http.*;
import java.net.http.HttpResponse.BodyHandlers;

public class Connection {

    private static final String BASE = "https://pomodoro-timer.koyeb.app";
    private final HttpClient client;
    private final ObjectMapper mapper;
    private final String token;

    public Connection(String jwtToken) {
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        this.token = jwtToken;
    }

    public TimerDetails getTimerDetails(long id) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/timers/" + id))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

        HttpResponse<String> resp = client.send(req, BodyHandlers.ofString());

        if (resp.statusCode() != 200) {
            System.out.println("Error: " + resp.body());
            return null;
        }

        String json = client.send(req, BodyHandlers.ofString()).body();
        return mapper.readValue(json, TimerDetails.class);
    }

    public void createTimer(String name, int workDuration, int breakDuration, int pomodoroCount) throws Exception {
        TimerRequest body = new TimerRequest(name, workDuration, breakDuration, pomodoroCount);
        String jsonBody = mapper.writeValueAsString(body);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/timers"))
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        var resp = client.send(req, BodyHandlers.ofString());
        if (resp.statusCode() != 201) {
            throw new RuntimeException("Failed : HTTP error code : " + resp.statusCode());
        }

        System.out.println("Created Timer:  " + name);
    }

    public void updateTimer(long id, TimerRequest req) throws Exception {
        String body = mapper.writeValueAsString(req);
        HttpRequest r = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/timers/" + id))
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .build();
        var resp = client.send(r, BodyHandlers.discarding());
        if (resp.statusCode() != 204) {
            throw new RuntimeException("Update failed: HTTP " + resp.statusCode());
        }
    }


    public void deleteTimer(Long id) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/timers/" + id))
                .header("Authorization", "Bearer " + token)
                .DELETE()
                .build();

        var resp = client.send(req, BodyHandlers.ofString());
        if (resp.statusCode() != 204) {
            throw new RuntimeException("Failed : HTTP error code : " + resp.statusCode());
        }

        System.out.println("Timer deleted with id: " + id);
    }

//  public List<TimerDetails> getAllTimers() throws Exception {
//        HttpRequest req = HttpRequest.newBuilder()
//                .uri(URI.create(BASE+"/timers"))
//                .header("Authorization", "Bearer " + token)
//                .GET()
//                .build();
//
//                String json = client.send(req, BodyHandlers.ofString()).body();
//                TimerDetails[] arr = mapper.readValue(json, TimerDetails[].class);
//                return Arrays.asList(arr);
//    }

}
