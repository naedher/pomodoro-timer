package org.example.model;

// AppContext singleton, the instance can be accessed from anywhere in the application

public class AppContext {
    private static final AppContext instance = new AppContext();
    private String authToken;

    private AppContext() {}

    public static AppContext getInstance() {
        return instance;
    }

    public synchronized String getAuthToken() {
        return authToken;
    }

    public synchronized void setAuthToken(String token) {
        this.authToken = token;
    }
}
