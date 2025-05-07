package org.example.demo;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.example.demo.exception.BadRequestException;
import org.example.demo.exception.UnauthorizedException;

import java.util.logging.Logger;

public class RequestDemoController {

    @FXML private Button loginButton;
    @FXML private TextField emailField;
    @FXML private TextField passwordField;
    @FXML private Text logText;
    @FXML private TextField selectedTimer;
    @FXML private Text timerText;
    @FXML private Button getTimerButton;

    private Logger logger;

    RequestDemoService service;

    public RequestDemoController() {
        this.service = new RequestDemoService();
        this.logger = Logger.getLogger(RequestDemoController.class.getName());
    }


    // Example of synchronous http requests with threads running concurrently
    @FXML
    public void login(ActionEvent event) {

        String email = emailField.getText();
        String password = passwordField.getText();

        // TODO: do some validation

        Task<Void> loginTask = new Task<>() {
            @Override
            protected Void call() {
                try {
                    // Send credentials to the service to attempt login
                    service.login(email, password);
                } catch (BadRequestException | UnauthorizedException e) {
                    // Handle expected exceptions
                    logger.warning(e.getMessage());
                    failed();
                    return null;
                } catch (Exception e) {
                    // Unexpected exceptions
                    logger.severe(e.getMessage());
                    logText.setText("Something went wrong");
                    failed();
                    return null;
                }

                // If login successful
                // Navigate to the next view
//                Platform.runLater(() -> { // Communication between threads happen through Platform.runLater
//                    try {
//                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/pomodoro.fxml"));
//                        Parent root = loader.load();
//                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//                        stage.setScene(new Scene(root));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        logText.setText("Failed to load main scene.");
//                    }
//                });

                logText.setText("Login successful");
                return null;
            }

            @Override
            protected void failed() {
                Platform.runLater(() -> logText.setText("Login failed."));
            }
        };

        // Run the task in a new thread
        new Thread(loginTask).start();
    }

    // Example of async http requests, no need to manually create threads
    @FXML
    public void getTimerAsync() {
        int id = Integer.parseInt(selectedTimer.getText());

        service.getTimerAsync(id)
                .thenAccept(timerDetails -> {
                    // Run this code if successful
                    timerText.setText("Successfully got timer: " + timerDetails.getName());
                })
                .exceptionally(completionException -> {
                    // Run this code if any exceptions were thrown
                    Throwable ex = completionException.getCause();
                    // Check for expected exceptions and handle them
                    if (ex instanceof BadRequestException) {
                        // Malformed request or failed validation
                        logger.severe(ex.getMessage());
                    } else if (ex instanceof UnauthorizedException) {
                        // Invalid email or password
                        logger.severe(ex.getMessage());
                    } else {
                        // Unexpected error
                        logger.severe(ex.getMessage());
                    }
                    return null;
                });
    }
}
