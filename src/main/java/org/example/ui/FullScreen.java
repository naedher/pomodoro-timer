package org.example.ui;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.input.MouseEvent;

public class FullScreen {

    private final TimerController timerController;
    private Stage fullScreenStage;
    Label titleLabel;


    // skapar fullscreen och har stäng & paus funktion via klick eller esc

    public FullScreen(TimerController timerController) {
        this.timerController = timerController;
        Platform.runLater(this::showFullscreenStage);
    }

    public void showFullscreenStage() {
        // Skapa fullscreen
        titleLabel = new Label("Focus Mode");
        titleLabel.setStyle("-fx-font-size: 60px; -fx-text-fill: white;");

        Label myTimerLabel = new Label("00:00");
        myTimerLabel.setStyle("-fx-font-size: 100px; -fx-text-fill: white;");

        VBox vbox = new VBox(40, titleLabel, myTimerLabel);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: black;");

        Pane clickOverlay = new Pane();
        clickOverlay.setStyle("-fx-background-color: transparent;");
        clickOverlay.setOnMouseClicked(event -> {
            Platform.runLater(() -> {
                timerController.stop();
                timerController.nll();
                fullScreenStage.close();
            });
        });

        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: black;");
        root.getChildren().addAll(vbox, clickOverlay);

        // Hämta skärmstorlek
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();

// Sätt klick-overlay manuellt till full skärm
        clickOverlay.setPrefWidth(screenWidth);
        clickOverlay.setPrefHeight(screenHeight);


        Scene scene = new Scene(root);

        fullScreenStage = new Stage();
        fullScreenStage.initStyle(StageStyle.UNDECORATED);
        fullScreenStage.setScene(scene);
        fullScreenStage.setFullScreen(true);
        fullScreenStage.show();


        // ESC stänger
        scene.setOnKeyPressed(key -> {
            if (key.getCode() == KeyCode.ESCAPE) {
                timerController.stop();
                timerController.nll();
                fullScreenStage.close();
            }
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

    // uppdatera label
    public void setTitleText(String text) {
        Platform.runLater(() -> {
            if (titleLabel != null) {
                titleLabel.setText(text);
            }
        });
    }

}