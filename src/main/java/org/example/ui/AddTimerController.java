package org.example.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.exceptions.HttpException;
import org.example.model.AppContext;
import org.example.model.dto.TimerCreate;
import org.example.model.service.TimerService;
import org.example.model.service.impl.TimerServiceImpl;

import java.util.concurrent.TimeUnit;


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

        createButton.setOnAction(e -> handleCreate());
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

        timerService.createTimer(request).join(); // missing error handling for now
        stage.close();
    }


}
