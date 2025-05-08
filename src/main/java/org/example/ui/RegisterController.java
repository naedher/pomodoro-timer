package org.example.ui;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.example.model.service.impl.AuthServiceImpl;

import java.util.concurrent.CompletableFuture;

public class RegisterController {
    LoginScene loginScene;

    public RegisterController(LoginScene loginScene){
        this.loginScene = loginScene;
    }

    public void Register(String U, String L){
        final String[] error = {""};
        Register2(U, L)
                .thenAccept(result -> {
                    if (result.equals("success")) {
                        System.out.println("Allt gick bra!");
                        loginScene.registrationsuccess();

                    } else {

                        // Kommande är ett försök att förska få individuella medelande per fel. Ej klart

                        if(result.equals("error: java.lang.RuntimeException: Failed to create data: HTTP 500 - {\"error\":\"Something went wrong\"}")){
                              error[0] = "Email adress already in use.";  // generell?, får upp 500 när den redan är skapad iaf
                        } else if (result.equals("error: java.net.ConnectException")) {
                            error[0] = "Check network connection.";
                        }else{
                            error[0] = result;
                        }

                        System.out.println("Error: " + result);
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Error: " + error[0]);
                            alert.showAndWait();
                        });

                    }
                });

    }

    public CompletableFuture<String> Register2(String u, String l) {
        AuthServiceImpl auth = new AuthServiceImpl();

        return auth.register(u, l)
                .thenApply(token -> "success")
                .exceptionally(ex -> {
                    System.out.println("Registration failed: " + ex.getMessage());
                    return "error: " + ex.getMessage();
                });
    }

}
