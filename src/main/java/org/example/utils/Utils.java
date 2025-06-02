package org.example.utils;

import javafx.scene.control.Alert;

public class Utils {

    public static int minutesToSeconds(int minutes) {
        return minutes * 60;
    }

    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
