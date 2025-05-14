package org.example.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class LoginScene {
    private Main mainApp;
    private TextField emailField; // Input field for email
    private TextField passwordField; // Input field for password
    private LoginController loginController = new LoginController();


    public LoginScene(Main mainApp) {
        this.mainApp = mainApp;
    }

    // Creates and returns the login scene UI
    public Scene createScene() {
        Label emailLabel = new Label("Email:");
        emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setPrefWidth(140);

        // Create and configure the password label and input field
        Label passwordLabel = new Label("Password:");
        passwordField = new TextField();
        passwordField.setPromptText("Password");
        passwordField.setPrefWidth(140);

        // Create and configure the input grid
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);
        inputGrid.addRow(0, emailLabel, emailField);
        inputGrid.addRow(1, passwordLabel, passwordField);
        inputGrid.setAlignment(Pos.CENTER);

        // Create and configure the buttons
        Button login = createStyledButton("Login", "#4CAF50");
        Button guest = createStyledButton("Guest mode", "#FFC107");
        Button exit = createStyledButton("Exit", "#f44336");

        // Set button actions
        login.setOnAction(e -> loginAction());
        guest.setOnAction(e -> guestModeAction());
        exit.setOnAction(e -> exitAction());

        // Create and configure the button box
        HBox buttonBox = new HBox(10, login, guest, exit);
        buttonBox.setAlignment(Pos.CENTER);

        // Create and configure the root layout
        VBox root = new VBox(20, inputGrid, buttonBox);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f5f5f5;");

        return new Scene(root, 350, 250);
    }

    // Helper method to create a styled button
    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white;");
        button.setPrefWidth(120);
        return button;
    }

    // Action method for the login button
    private void loginAction() {
        String email = emailField.getText();
        String password = passwordField.getText();

        // Error handling for email
        if (!isValidEmail(email)) {
            showAlert("Invalid Email", "Please enter a valid email address.");
            return;
        }

        // Error handling for password (8 < characters)
        if (password.length() < 8) {
            showAlert("Invalid Password", "Password must be at least 8 characters long");
            return;
        }
        // Send login request
        loginController.sendLoginRequest(email, password)
                .thenAccept(token -> {
                    Platform.runLater(() -> mainApp.timerScene());
                })
                .exceptionally(ex -> {

                    Platform.runLater(() ->
                            showAlert("Login Failed", ex.getMessage())
                    );
                    return null;
                });

        mainApp.timerScene();
    }

    // Helper method to validate email domain
    private boolean isValidEmail(String email) {
        String[] validDomains = {"@gmail.com", "@hotmail.com", "@outlook.com", "@yahoo.com", "@icloud.com"};
        for (String domain : validDomains) {
            if (email.toLowerCase().endsWith(domain)) { // Kolla om email slutar med giltig domän
                return true;
            }
        }
        return false;
    }

    // Helper method to show alert dialog
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Action method for guest mode button
    private void guestModeAction() {
        System.out.println("Guest mode selected");
        mainApp.timerScene();
    }

    // Action method for exit button
    private void exitAction() {
        System.out.println("Exit selected");
        System.exit(0);
    }
}