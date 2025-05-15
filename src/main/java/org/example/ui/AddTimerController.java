package org.example.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.model.AppContext;
import org.example.model.dto.TimerCreate;
import org.example.model.service.TimerService;
import org.example.model.service.impl.TimerServiceImpl;


public class AddTimerController {
    @FXML private TextField nameField;
    @FXML private Spinner<Integer> workTimeSpinner;
    @FXML private Spinner<Integer> shortBreakTimeSpinner;
    @FXML private Spinner<Integer> longBreakTimeSpinner;
    @FXML private Spinner<Integer> intervalSpinner;
    @FXML private Button createButton;

    private Stage stage;
    private TimerService timerService;

    @FXML
    private void initialize() {
        String token = AppContext.getInstance().getAuthToken();
        this.timerService = new TimerServiceImpl(token);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }


    @FXML
    private void handleCreate() {
        TimerCreate request = new TimerCreate(
                nameField.getText(),
                workTimeSpinner.getValue(),
                shortBreakTimeSpinner.getValue(),
                longBreakTimeSpinner.getValue(),
                intervalSpinner.getValue()
        );

        try {
            timerService.createTimer(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }



}
