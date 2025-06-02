package org.example.ui;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.geometry.Insets;
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
import javafx.util.Duration;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


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

        Label clockLabel = new Label();
        clockLabel.setStyle("-fx-font-size: 50px; -fx-text-fill: white;");
        StackPane.setAlignment(clockLabel, Pos.TOP_RIGHT);
        StackPane.setMargin(clockLabel, new Insets(20, 40, 0, 0));

        VBox vbox = new VBox(40, titleLabel, myTimerLabel);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: black;");

        Pane clickOverlay = new Pane();
        clickOverlay.setStyle("-fx-background-color: transparent;");

        // stäng genom musklick
        clickOverlay.setOnMouseClicked(event -> {
            Platform.runLater(this::closeFullscreen);
        });

        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: black;");
        root.getChildren().addAll(vbox, clickOverlay, clockLabel);

        // Hämta storlek
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


        // ESC eller space stänger
        scene.setOnKeyPressed(key -> {
            if (key.getCode() == KeyCode.ESCAPE || key.getCode() == KeyCode.SPACE) {
                closeFullscreen();
            }
        });

        // Uppdatera timer från kontrollern
        IntegerProperty timeLeft = timerController.timeLeftProperty();
        timeLeft.addListener((obs, oldVal, newVal) -> {
            myTimerLabel.setText(formatTime(newVal.intValue()));
        });

        Timeline clockTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            LocalTime now = LocalTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            clockLabel.setText(now.format(formatter));
        }));
        clockTimeline.setCycleCount(Animation.INDEFINITE);
        clockTimeline.play();

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

    private void closeFullscreen() {
        if (fullScreenStage != null && fullScreenStage.isShowing()) {
            timerController.stop();
            timerController.nll();
            // föst gå urr fullsceen mode för att stoppa trådfrys
            fullScreenStage.setFullScreen(false);
            fullScreenStage.close();
        }
    }

}
