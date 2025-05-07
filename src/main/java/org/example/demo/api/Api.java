package org.example.demo.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.demo.exception.BadRequestException;
import org.example.demo.exception.UnauthorizedException;
import org.example.demo.LoginRequest;
import org.example.demo.model.TimerDetails;
import org.json.JSONObject;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class Api {

    private ObjectMapper mapper;
    private Connection conn;


    public Api() {
        this.mapper = new ObjectMapper();
        this.conn = new Connection();
    }

    public String login(LoginRequest loginRequest) throws JsonProcessingException, BadRequestException, UnauthorizedException {
        // Serialize to JSON format
        String body = mapper.writeValueAsString(loginRequest);
        try {
            // Make post request with the login credentials in the body
            JSONObject response = conn.post("/api/auth/login", body);
            // from the response body, get the token
            return response.getString("token");
        } catch (BadRequestException | UnauthorizedException e) { // Login failed
            throw e; // rethrow for handling in controller
        } catch (Exception e) { // Unexpected error
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<TimerDetails> getTimer(int id) {
        return conn.getAsyncAuth("/timers/" + id)
                .thenApply(response -> {
                    try {
                        return mapper.readValue(response, TimerDetails.class);
                    } catch (JsonProcessingException e) {
                        throw new CompletionException(e);
                    }
                });
    }
}
