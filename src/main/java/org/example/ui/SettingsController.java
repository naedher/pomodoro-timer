package org.example.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import org.example.model.dto.TimerPreference;
import org.example.model.service.TimerService;
import org.example.model.TimerServiceFactory;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class SettingsController {
    @FXML private CheckBox soundEnabledCheckbox;
    @FXML private ComboBox<String> soundComboBox;
    @FXML private ComboBox<String> themeComboBox;
    @FXML private Button previewButton;

    private Stage stage;
    private TimerPreference timerPreference;
    private final TimerService timerService = TimerServiceFactory.get();

    private static final List<String> AVAILABLE_SOUNDS = Arrays.asList(
            "alarm1.wav",
            "alarm2.wav",
            "alarm3.wav"
    );

    private static final List<String> AVAILABLE_THEMES = Arrays.asList(
            "light",
            "dark"
    );

    @FXML
    public void initialize() {
        soundComboBox.getItems().addAll(AVAILABLE_SOUNDS);
        soundComboBox.setValue(AVAILABLE_SOUNDS.get(0));

        themeComboBox.getItems().addAll(AVAILABLE_THEMES);
        themeComboBox.setValue(AVAILABLE_THEMES.get(0));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setTimerPreference(TimerPreference preference) {
        this.timerPreference = preference;
        if (preference != null) {
            soundEnabledCheckbox.setSelected(preference.isEnabledSound());
            soundComboBox.setValue(AVAILABLE_SOUNDS.get(preference.getAlarmNumber() - 1));
            themeComboBox.setValue(preference.getTheme());
        }
    }

    public TimerPreference getTimerPreference() {
        return timerPreference;
    }

    @FXML
    private void previewSound() {
        if (!soundEnabledCheckbox.isSelected()) return;

        try {
            String soundFile = soundComboBox.getValue();
            InputStream audio = getClass().getResourceAsStream("/sounds/" + soundFile);
            if (audio == null) {
                showAlert("Error", "Sound file not found: " + soundFile);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audio);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            showAlert("Error", "Could not play sound: " + e.getMessage());
        }
    }

    @FXML
    private void handleSave() {
        if (timerPreference == null) {
            timerPreference = new TimerPreference();
        }

        timerPreference.setEnabledSound(soundEnabledCheckbox.isSelected());
        timerPreference.setAlarmNumber(AVAILABLE_SOUNDS.indexOf(soundComboBox.getValue()) + 1);
        timerPreference.setTheme(themeComboBox.getValue());

        // Save preferences to backend
        timerService.savePreferences(timerPreference)
                .thenRun(() -> stage.close())
                .exceptionally(ex -> {
                    showAlert("Error", "Could not save preferences: " + ex.getMessage());
                    return null;
                });
    }

    @FXML
    private void handleCancel() {
        stage.close();
    }

    private void showAlert(String title, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}