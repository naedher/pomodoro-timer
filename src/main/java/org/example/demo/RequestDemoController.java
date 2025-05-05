package org.example.demo;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.demo.exception.BadRequestException;
import org.example.demo.exception.UnauthorizedException;

import java.io.IOException;

public class RequestDemoController {

    @FXML private Button loginButton;
    @FXML private TextField emailField;
    @FXML private TextField passwordField;
    @FXML private Text logText;

    RequestDemoService service;

    public RequestDemoController() {
        this.service = new RequestDemoService();
    }

    @FXML
    public void login(ActionEvent event) {

        String email = emailField.getText();
        String password = passwordField.getText();

        // TODO: do some validation

        Task<Void> loginTask = new Task<>() {
            @Override
            protected Void call() {
                try {
                    service.login(email, password);
                } catch (BadRequestException | UnauthorizedException e) {
                    failed();
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    logText.setText("Something went wrong");
                    failed();
                    return null;
                }

                // If login successful
                // Navigate to the next view
                Platform.runLater(() -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/pomodoro.fxml"));
                        Parent root = loader.load();
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(new Scene(root));
                    } catch (IOException e) {
                        e.printStackTrace();
                        logText.setText("Failed to load main scene.");
                    }
                });

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
}
