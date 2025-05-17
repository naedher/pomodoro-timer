package org.example.model;

// AppContext singleton, the instance can be accessed from anywhere in the application

public class AppContext {
    private static final AppContext instance = new AppContext();
    private String authToken;
    private boolean guestMode = false;

    private AppContext() {}

    public static AppContext getInstance() {
        return instance;
    }

    public synchronized String getAuthToken() {
        return authToken;
    }

    public synchronized void setAuthToken(String token) {
        this.authToken = token;
        this.guestMode = false;
    }
    public synchronized boolean isGuestMode() {
        return guestMode;
    }
    public synchronized void setGuestMode(boolean guestMode) {
        this.guestMode = guestMode;
        if (guestMode) this.authToken = null;   // we need to disable authentication for guestmode
    }

}
