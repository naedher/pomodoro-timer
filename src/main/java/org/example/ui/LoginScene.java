package org.example.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.example.infrastructure.ApiClient;
import org.example.model.service.impl.AuthServiceImpl;

import java.util.Optional;

public class LoginScene {
    private Main mainApp;
    private TextField emailField;
    private TextField passwordField;
    private final RegisterController registerController = new RegisterController(this);


    public LoginScene(Main mainApp) {
        this.mainApp = mainApp;
    }

    public Scene createScene() {

        Label titleLabel = new Label("Pomodoro Desktop Timer");
        titleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
        titleLabel.setStyle("-fx-text-fill: black;");
        titleLabel.setAlignment(Pos.CENTER);

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

        Button login = createStyledButton("Login", "#888888");
        Button creatAcc = createStyledButton("New Account", "#888888");
        Button exit = createStyledButton("Exit", "#888888");

        login.setOnAction(e -> loginAction());
        creatAcc.setOnAction(e -> newAccAction());
        exit.setOnAction(e -> exitAction());

        HBox buttonBox = new HBox(10, login, creatAcc, exit);
        buttonBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(20,titleLabel, inputGrid, buttonBox);
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
        LoginController loginController = mainApp.getLoginController();
        loginController.sendLoginRequest(email,password);
        //mainApp.timerScene();

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

    private void newAccAction() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (!isValidEmail(email)) {
            showAlert("Invalid Email", "Please enter a valid email address.");
            return;
        }
        if (password.length() < 8) {
            showAlert("Invalid Password", "Password must be at least 8 characters long");
            return;
        }
        registerController.Register(email,password);



    }

    public void registrationSuccess() {

        Platform.runLater(() -> {
            Alert dialog = new Alert(Alert.AlertType.NONE);
            dialog.setTitle("Registration");
            dialog.setHeaderText("Registration successful");
            dialog.setContentText("Log in with your new account?");

            ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getButtonTypes().setAll(yesButton, noButton);

            Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            stage.getIcons().clear();

            dialog.getDialogPane().setStyle("-fx-font-size: 14px; -fx-background-color: #f9f9f9;");
            dialog.getDialogPane().lookupButton(yesButton).setStyle("-fx-background-color: #888888; -fx-text-fill: white;");
            dialog.getDialogPane().lookupButton(noButton).setStyle("-fx-background-color: #888888; -fx-text-fill: white;");

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == yesButton) {
                loginAction();
                mainApp.timerScene();
            } else {
                System.out.println("login cancelled.");
                registerController.reset();
                AuthServiceImpl auth = new AuthServiceImpl();
                ApiClient api = auth.getApiClient();
                api.setToken(null);
            }
        });
    }


    private void exitAction() {
        System.out.println("Exit selected");
        System.exit(0);
    }
}
