package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.model.TimerServiceFactory;
import org.example.model.dto.TimerCreate;
import org.example.service.interfaces.TimerService;

import java.util.concurrent.ExecutionException;


public class AddTimerController {
    @FXML private TextField nameField;
    @FXML private Spinner<Integer> workTimeSpinner;
    @FXML private Spinner<Integer> shortBreakTimeSpinner;
    @FXML private Spinner<Integer> longBreakTimeSpinner;
    @FXML private Spinner<Integer> intervalSpinner;
    @FXML private Button createButton;

    private Stage stage;
    private final TimerService timerService = TimerServiceFactory.get();

    @FXML
    private void initialize() {
        initSpinners();
        createButton.setOnAction(e -> handleCreate());
    }

    private void initSpinners() {
        addSpinnerValueFactory(workTimeSpinner, 5, 999);
        addSpinnerValueFactory(shortBreakTimeSpinner, 1, 999);
        addSpinnerValueFactory(longBreakTimeSpinner, 1, 999);
        addSpinnerValueFactory(intervalSpinner, 1, 999);
    }

    private void addSpinnerValueFactory(Spinner<Integer> spinner, int lower, int upper) {
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(lower, upper));

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void handleCreate() {
        TimerCreate request = new TimerCreate(
                nameField.getText(),
                workTimeSpinner.getValue(),
                shortBreakTimeSpinner.getValue(),
                longBreakTimeSpinner.getValue(),
                intervalSpinner.getValue()
        );
        try {
            timerService.createTimer(request).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        stage.close();
    }
}
