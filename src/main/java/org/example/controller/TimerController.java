package org.example.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.service.TimerServiceFactory;
import org.example.model.TimerDetails;
import org.example.service.interfaces.TimerService;
import org.example.manager.TimerGuiManager;
import org.example.manager.TimerListManager;
import org.example.utils.Utils;

import java.io.IOException;

public class TimerController {

    @FXML private Label timerLabel;
    @FXML private Label intervalLabel;
    @FXML private Button startButton;
    @FXML private Button resetButton;
    @FXML private ToggleButton focusButton;
    @FXML private ToggleButton shortBreakButton;
    @FXML private ToggleButton longBreakButton;

    @FXML private ListView<TimerDetails> timerListView;

    private TimerGuiManager timerGuiManager;
    private TimerListManager listManager;
    private final TimerService timerService = TimerServiceFactory.get();

    @FXML
    public void initialize() {

        // Create TimerService
        // instantiating TimerService here for compatibility with future features.
        // we simply get factory class here, it chooses which logic will work.


        // Create TimerGui
        this.timerGuiManager = new TimerGuiManager(this);

        // Create ListManager
        this.listManager = new TimerListManager(this, timerService, timerGuiManager);

        addContextMenu();
    }
    private void addContextMenu() {
        timerListView.setCellFactory(lv -> {
            ListCell<TimerDetails> cell = new ListCell<>() {
                @Override
                protected void updateItem(TimerDetails item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getName());
                }
            };

            MenuItem edit = new MenuItem("Edit");
            edit.setOnAction(e -> openEditDialog(cell.getItem()));
            MenuItem delete = new MenuItem("Delete");
            delete.setOnAction(e -> handleDelete(cell.getItem()));

            ContextMenu cm = new ContextMenu(edit, delete);

            cell.emptyProperty().addListener((obs, wasEmpty, isEmpty) ->
                    cell.setContextMenu(isEmpty ? null : cm));

            return cell;
        });
    }
    @FXML
    private void addSavedTimer() {
        listManager.addSavedTimer();
    }

    // Method to start or stop the timer
    @FXML
    private void startStop() {
        timerGuiManager.startStop();
    }

    // Method to reset the timer
    @FXML
    private void reset() {
        timerGuiManager.reset();
    }

    // Method to handle debug button action
    @FXML
    private void handleDebug() {
        // Set timer to 3 seconds for testing
        timerGuiManager.debug();
    }
    // delete selected timer
    private void handleDelete(TimerDetails timer) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete timer '" + timer.getName() + "'?", ButtonType.OK, ButtonType.CANCEL);
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                timerService.deleteTimer(timer.getId())
                        .thenRun(() -> Platform.runLater(listManager::updateTimerList))
                        .exceptionally(ex -> {                       // UI-friendly error
                            Platform.runLater(() ->
                                    Utils.showAlert("Delete error", ex.getCause().getMessage()));
                            return null;
                        });
            }
        });
    }
    private void openEditDialog(TimerDetails timer) {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/org/example/app/UpdateTimer.fxml"));
            Parent root = loader.load();

            UpdateTimerController ctrl = loader.getController();
            ctrl.init(timer);

            Stage stage = new Stage();
            ctrl.setStage(stage);

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setTitle("Edit timer");
            stage.showAndWait();

            listManager.updateTimerList();
        } catch (IOException ex) {
            Utils.showAlert("Dialog error", ex.getMessage());
        }
    }

    public void setTimerLabelText(String text) {
        this.timerLabel.setText(text);
    }

    public void setIntervalLabelText(String text) {
        this.intervalLabel.setText(text);
    }

    public void setStartButtonText(String text) {
        this.startButton.setText(text);
    }

    public String getStartButtonText() {
        return startButton.getText();
    }

    public ToggleButton getFocusButton() {
        return focusButton;
    }

    public ToggleButton getShortBreakButton() {
        return shortBreakButton;
    }

    public ToggleButton getLongBreakButton() {
        return longBreakButton;
    }

    public ListView<TimerDetails> getTimerListView() {
        return timerListView;
    }
}