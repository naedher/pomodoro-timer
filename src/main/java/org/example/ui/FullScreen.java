package org.example.ui;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.input.MouseEvent;

public class FullScreen {

    private final TimerController timerController;

    public FullScreen(TimerController timerController) {
        this.timerController = timerController;
        new Thread(() -> {
            Platform.runLater(this::showFullscreenStage);
        }).start();
    }

    public void showFullscreenStage() {
        // Skapa fullscreen
        Label titleLabel = new Label("Focus Mode");
        titleLabel.setStyle("-fx-font-size: 60px; -fx-text-fill: white;");

        Label myTimerLabel = new Label("00:00");
        myTimerLabel.setStyle("-fx-font-size: 100px; -fx-text-fill: white;");

        VBox vbox = new VBox(40, titleLabel, myTimerLabel);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: black;");

        Scene scene = new Scene(vbox);
        Stage fullScreenStage = new Stage();
        fullScreenStage.initStyle(StageStyle.UNDECORATED); // Tar bort fönsterkanter
        fullScreenStage.setScene(scene);
        fullScreenStage.setFullScreen(true);
        fullScreenStage.show();


        // ESC → stäng
        scene.setOnKeyPressed(key -> {
            if (key.getCode() == KeyCode.ESCAPE) {
                timerController.stop();
                fullScreenStage.close();
            }
        });

        // Klick → stäng
        scene.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            timerController.stop();
            fullScreenStage.close();
        });


        // Uppdatera timeretiketten från kontrollern
        IntegerProperty timeLeft = timerController.timeLeftProperty();
        timeLeft.addListener((obs, oldVal, newVal) -> {
            myTimerLabel.setText(formatTime(newVal.intValue()));
        });
    }

    // formatera om tiden
    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }
}