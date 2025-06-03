package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.model.TimerDetails;
import org.example.model.TimerUpdate;
import org.example.service.TimerServiceFactory;
import org.example.service.interfaces.TimerService;
import org.example.utils.Utils;

public class UpdateTimerController {

    @FXML private TextField nameField;
    @FXML private Spinner<Integer> workSpinner;
    @FXML private Spinner<Integer> shortBreakSpinner;
    @FXML private Spinner<Integer> longBreakSpinner;
    @FXML private Spinner<Integer> intervalSpinner;
    @FXML private Button saveButton;

    private TimerDetails original;
    private Stage stage;
    private final TimerService timerService = TimerServiceFactory.get();

    @FXML
    private void initialize() {
        initSpinners();
        saveButton.setOnAction(e -> handleSave());
    }

    private void initSpinners() {
        addSpinnerValueFactory(workSpinner,        5, 999);
        addSpinnerValueFactory(shortBreakSpinner,  1, 999);
        addSpinnerValueFactory(longBreakSpinner,   1, 999);
        addSpinnerValueFactory(intervalSpinner,    1, 999);
    }

    private void addSpinnerValueFactory(Spinner<Integer> spinner, int lower, int upper) {
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(lower, upper));
    }

    //Called by TimerController before the dialog is shown.
    public void init(TimerDetails timer) {
        this.original = timer;
        nameField.setText(timer.getName());
        workSpinner.getValueFactory().setValue(timer.getWorkDuration());
        shortBreakSpinner.getValueFactory().setValue(timer.getShortBreakDuration());
        longBreakSpinner.getValueFactory().setValue(timer.getLongBreakDuration());
        intervalSpinner.getValueFactory().setValue(timer.getPomodoroCount());
    }

    private void handleSave() {
        TimerUpdate req = new TimerUpdate(
                nameField.getText(),
                workSpinner.getValue(),
                shortBreakSpinner.getValue(),
                longBreakSpinner.getValue(),
                intervalSpinner.getValue()
        );
        timerService.updateTimer(original.getId(), req)
                .thenRun(() -> stage.close())
                .exceptionally(ex -> {
                    Utils.showAlert("Update error", ex.getCause().getMessage());
                    return null;
                });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}

