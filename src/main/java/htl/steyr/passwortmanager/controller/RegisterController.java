package htl.steyr.passwortmanager.controller;

import htl.steyr.passwortmanager.service.AuthService;
import htl.steyr.passwortmanager.utils.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    @FXML private TextField registerUsernameField;
    @FXML private PasswordField registerPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label errorLabel;

    private final AuthService authService = new AuthService();

    @FXML
    public void registerButtonClicked(ActionEvent event) {

        hideError();

        String username = registerUsernameField.getText();
        String password = registerPasswordField.getText();
        String confirm = confirmPasswordField.getText();

        if (username == null || username.isBlank()
                || password == null || password.isBlank()
                || confirm == null || confirm.isBlank()) {
            showError("All fields are required");
            return;
        }

        try {
            boolean success = authService.register(username.trim(), password, confirm);

            if (!success) {
                showError("Username already exists");
                return;
            }

            showSuccess("Account created. Please log in.");
            SceneManager.switchTo("auth-view.fxml");

        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showError("Internal error during registration");
        }

    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: #ef4444;");
        errorLabel.setVisible(true);
    }

    private void showSuccess(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: #22c55e;");
        errorLabel.setVisible(true);
    }

    private void hideError() {
        errorLabel.setVisible(false);
    }
}
