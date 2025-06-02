package org.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.model.AppContext;
import org.example.model.dto.AuthRequest;
import org.example.service.AuthServiceImpl;
import org.example.app.Main;

import java.util.concurrent.CompletableFuture;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button registerPageButton;
    @FXML private Label errorLabel;

    private Main mainApp;

    // Empty constructor needed for FXML loader
    public LoginController() {
    }

    // Set main application reference
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Please enter both email and password");
            return;
        }

        // Clear previous error
        errorLabel.setVisible(false);

        try {
            sendLoginRequest(email, password);
        } catch (Exception e) {
            showError("Login failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleGuestMode() {
        AppContext.getInstance().setGuestMode(true);
        System.out.println("Guest mode enabled. Auth token cleared.");

        // Navigate to the main application scene
        Platform.runLater(() -> mainApp.timerScene());
    }

    // Handle the action when the user clicks the "Register" button
    @FXML
    private void handleRegisterPage() {
        mainApp.registerScene();
    }

    // Handle the action when the user clicks the "Forgot Password" button
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    public void sendLoginRequest(String email, String password) {
        try {
            sendLoginRequest2(email, password);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<String> sendLoginRequest2(String email, String password) throws JsonProcessingException {
        AuthServiceImpl auth = new AuthServiceImpl();
        AuthRequest authRequest = new AuthRequest(email, password);

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
                        showError("Invalid login credentials or server error");

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