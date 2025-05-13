package org.example.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.example.infrastructure.ApiClient;
import org.example.model.dto.AuthRequest;
import org.example.model.service.impl.AuthServiceImpl;

import java.util.concurrent.CompletableFuture;

public class RegisterController {
    LoginScene loginScene;
    private String[] Success = new String[1];
    private AuthServiceImpl auth = new AuthServiceImpl();

    public RegisterController(LoginScene loginScene){
        this.loginScene = loginScene;
    }

    public void Register(String Email, String Password){

        final String[] error = {""};
        try {
            Register2(Email, Password)
                    .thenAccept(result -> {
                        if (!result.startsWith("error:")) {
                            System.out.println("Allt gick bra");
                            loginScene.registrationsuccess();
                            ApiClient api = auth.getApiClient();
                            api.setToken(result);

                        } else {

                            if(result.equals("error: java.lang.RuntimeException: Failed to create data: HTTP 500 - {\"error\":\"Something went wrong\"}")){
                                error[0] = "Email adress already in use.";
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
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public CompletableFuture<String> Register2(String Email, String Password) throws JsonProcessingException {
        AuthRequest authRequest = new AuthRequest(Email,Password);

        return auth.register(authRequest)
                .thenApply(token ->{
                    Success[0] = "Success";
                    return token;
                })
                .exceptionally(ex -> {
                    System.out.println("Registration failed: " + ex.getMessage());
                    return "error: " + ex.getMessage();
                });

    }

    public String[] getSuccess(){
        return Success;

    }

    public void reset(){
        Success[0] = null;
    }

}

