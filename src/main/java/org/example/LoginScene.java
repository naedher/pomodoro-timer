package org.example;

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
    private TextField emailField;
    private TextField passwordField;
    private final LoginController loginController = new LoginController();


    public LoginScene(Main mainApp) {
        this.mainApp = mainApp;
    }

    public Scene createScene() {
        Label emailLabel = new Label("Email:");
        emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setPrefWidth(140);

        Label passwordLabel = new Label("Password:");
        passwordField = new TextField();
        passwordField.setPromptText("Password");
        passwordField.setPrefWidth(140);

        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);
        inputGrid.addRow(0, emailLabel, emailField);
        inputGrid.addRow(1, passwordLabel, passwordField);
        inputGrid.setAlignment(Pos.CENTER);

        Button login = createStyledButton("Login", "#4CAF50");
        Button guest = createStyledButton("Guest mode", "#FFC107");
        Button exit = createStyledButton("Exit", "#f44336");

        login.setOnAction(e -> loginAction());
        guest.setOnAction(e -> guestModeAction());
        exit.setOnAction(e -> exitAction());

        HBox buttonBox = new HBox(10, login, guest, exit);
        buttonBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(20, inputGrid, buttonBox);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f5f5f5;");

        return new Scene(root, 350, 250);
    }

    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white;");
        button.setPrefWidth(120);
        return button;
    }

    private void loginAction() {
        String email = emailField.getText();
        String password = passwordField.getText();

        // Felhantering för mail
        if (!isValidEmail(email)) {
            showAlert("Invalid Email", "Please enter a valid email address.");
            return;
        }

        // Felhantering för lösenord
        // Felhantering för stor bokstav, specialtecken, etc kan vi lägga till senare
        if (password.length() < 8) {
            showAlert("Invalid Password", "Password must be at least 8 characters long");
            return;
        }
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

    private boolean isValidEmail(String email) {
        String[] validDomains = {"@gmail.com", "@hotmail.com", "@outlook.com", "@yahoo.com", "@icloud.com"};
        for (String domain : validDomains) {
            if (email.toLowerCase().endsWith(domain)) { // Kolla om email slutar med giltig domän
                return true;
            }
        }
        return false;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void guestModeAction() {
        System.out.println("Guest mode selected");
        mainApp.timerScene();
    }

    private void exitAction() {
        System.out.println("Exit selected");
        System.exit(0);
    }
}