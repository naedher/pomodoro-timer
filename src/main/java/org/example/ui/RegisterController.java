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

        Register2(U, L)
                .thenAccept(result -> {
                    if (result.equals("success")) {
                        System.out.println("Allt gick bra!");
                        loginScene.registrationsuccess();

                    } else {

                        // Kommande är ett försök att förska få individuella medelande per fel. Ej klart

                        if(result.equals("java.lang.RuntimeException: Failed to create data: HTTP 500 - {\"error\":\"Something went wrong\"}")){
                            String  errormessage = "Email adress already in use.";
                        } else if (result.equals("java.net.ConnectException")) {
                            String errormessage = "Check network connection.";
                        }else{
                            String errormessage = result;
                        }
                        
                        System.out.println("Error: " + result);
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Error: " + result);
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
