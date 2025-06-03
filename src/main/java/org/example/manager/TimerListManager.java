package org.example.manager;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.controller.AddTimerController;
import org.example.controller.TimerController;
import org.example.service.interfaces.TimerService;

import java.io.IOException;

import static org.example.utils.Utils.showAlert;

public class TimerListManager {


    private final TimerController timerController;
    private final TimerService timerService;
    private final TimerGuiManager timerGuiManager;

    public TimerListManager(TimerController timerController, TimerService timerService, TimerGuiManager timerGuiManager) {
        this.timerController = timerController;
        this.timerService = timerService;
        this.timerGuiManager = timerGuiManager;

        initListListener();
    }

    public void addSavedTimer() {
        // Load AddTimer dialogue
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/app/AddTimer.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        AddTimerController addTimerController = loader.getController();

        Stage dialogStage = new Stage();
        addTimerController.setStage(dialogStage);

        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setScene(new Scene(root));
        dialogStage.showAndWait(); // blocks until closed

        // Update the list
        updateTimerList();
    }

    public void updateTimerList() {
        timerService.getUserTimers()
                .thenAccept(list -> Platform.runLater(() -> timerController.getTimerListView().setItems(
                        FXCollections.observableArrayList(list))))
                .exceptionally(ex -> {
                    Platform.runLater(() ->
                            showAlert("List update error", ex.getCause().getMessage()));
                    return null;
                });
    }

    private void initListListener() {
        timerController.getTimerListView().getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                timerGuiManager.setSelectedTimer(newVal);
                timerGuiManager.reset();
            }
            timerGuiManager.update();
        });
    }
}
