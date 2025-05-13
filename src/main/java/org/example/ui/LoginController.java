// src/main/java/org/example/LoginController.java


// src/main/java/org/example/LoginController.java
        package org.example.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.example.model.AppContext;
import org.example.model.dto.AuthRequest;
import org.example.model.service.impl.AuthServiceImpl;

import java.util.concurrent.CompletableFuture;

public class LoginController {

    private final Main mainApp;

    public LoginController(Main mainApp) {
        this.mainApp = mainApp;
    }

    public void sendLoginRequest(String Email, String Password){
        try {
            sendLoginRequest2(Email,Password);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<String> sendLoginRequest2(String Email, String Password) throws JsonProcessingException {
        AuthServiceImpl auth = new AuthServiceImpl();
        AuthRequest authRequest = new AuthRequest(Email,Password);

        auth.login(authRequest)
                .thenAccept(token -> {
                    System.out.println("Login successful. Token: " + token);
                    AppContext app = AppContext.getInstance();
                    app.setAuthToken(token);

                    Platform.runLater(() -> {
                        mainApp.timerScene();
                    });
                })
                .exceptionally(ex -> {
                    System.err.println("Login failed: " + ex.getMessage());
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Login Failed");
                        alert.setHeaderText("Invalid login credentials or server error");
                        alert.setContentText(ex.getMessage());
                        alert.showAndWait();
                    });
                    return null;
                });
        return null;
    }
}