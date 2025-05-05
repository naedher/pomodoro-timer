package org.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.demo.api.Api;
import org.example.demo.exception.BadRequestException;
import org.example.demo.exception.UnauthorizedException;

public class RequestDemoService {

    private final Api api;

    public RequestDemoService() {
        this.api = new Api();
    }

    public void login(String email, String password) throws UnauthorizedException, BadRequestException, JsonProcessingException {
        // Create DTO
        LoginRequest loginRequest = new LoginRequest(email, password);

        // make the request
        String token = api.login(loginRequest);

        // Set the token in the application context for future use
        AppContext.getInstance().setAuthToken(token);
    }
}
