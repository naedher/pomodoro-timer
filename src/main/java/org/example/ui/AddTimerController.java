package org.example.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.model.AppContext;
import org.example.model.TimerServiceFactory;
import org.example.model.dto.TimerCreate;
import org.example.model.service.TimerService;
import org.example.model.service.impl.RemoteTimerService;


public class AddTimerController {
    @FXML private TextField nameField;
    @FXML private Spinner<Integer> workTimeSpinner;
    @FXML private Spinner<Integer> shortBreakTimeSpinner;
    @FXML private Spinner<Integer> longBreakTimeSpinner;
    @FXML private Spinner<Integer> intervalSpinner;
    @FXML private Button createButton;

    private Stage stage;
    // i did not understand this, this needs to be final,
    private TimerService timerService = TimerServiceFactory.get();

    @FXML
    private void initialize() {
        String token = AppContext.getInstance().getAuthToken();
        this.timerService = new RemoteTimerService(token);
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
        timerService.createTimer(request).join(); // missing error handling for now
        stage.close();
    }
}
