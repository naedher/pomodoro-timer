package org.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.app.AppContext;
import org.example.model.AuthRequest;
import org.example.service.AuthServiceImpl;
import org.example.app.Main;

import java.util.concurrent.CompletableFuture;

public class RegisterController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button registerButton;
    @FXML private Button backToLoginButton;
    @FXML private Label errorLabel;

    private Main mainApp;
    private String[] Success = new String[1];
    private AuthServiceImpl auth = new AuthServiceImpl();

    // Empty constructor needed for FXML loader
    public RegisterController() {
    }

    // Set main application reference
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleRegister() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Please enter both email and password");
            return;
        }

        // Clear previous error
        errorLabel.setVisible(false);

        Register(email, password);
    }

    @FXML
    private void handleBackToLogin() {
        mainApp.loginScene();
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    public void Register(String Email, String Password) {
        final String[] error = {""};
        try {
            Register2(Email, Password)
                    .thenAccept(result -> {
                        if (!result.startsWith("error:")) {
                            System.out.println("Registration successful");
                            registrationSuccess();
                            AppContext app = AppContext.getInstance();
                            app.setAuthToken(result);
                        } else {
                            if(result.equals("error: java.lang.RuntimeException: Failed to create data: HTTP 500 - {\"error\":\"Something went wrong\"}")) {
                                error[0] = "Email address already in use.";
                            } else if (result.equals("error: java.net.ConnectException")) {
                                error[0] = "Check network connection.";
                            } else {
                                error[0] = result;
                            }

                            System.out.println("Error: " + result);
                            Platform.runLater(() -> {
                                showError(error[0]);

                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText(null);
                                alert.setContentText("Error: " + error[0]);
                                alert.showAndWait();
                            });
                        }
                    });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<String> Register2(String Email, String Password) throws JsonProcessingException {
        AuthRequest authRequest = new AuthRequest(Email, Password);

        return auth.register(authRequest)
                .thenApply(token -> {
                    Success[0] = "Success";
                    return token;
                })
                .exceptionally(ex -> {
                    System.out.println("Registration failed: " + ex.getMessage());
                    return "error: " + ex.getMessage();
                });
    }

    public void registrationSuccess() {
        Platform.runLater(() -> {
            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registration Successful");
            alert.setHeaderText(null);
            alert.setContentText("Your account has been created successfully!");
            alert.showAndWait();

            // Navigate to timer scene
            mainApp.timerScene();
        });
    }

    public String[] getSuccess() {
        return Success;
    }

    public void reset() {
        Success[0] = null;
    }
}