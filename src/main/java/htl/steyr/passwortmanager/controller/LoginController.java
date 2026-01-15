package htl.steyr.passwortmanager.controller;

import htl.steyr.passwortmanager.service.AuthService;
import htl.steyr.passwortmanager.utils.SceneManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class LoginController {

    @FXML private TextField usernameTF;
    @FXML private PasswordField passwordTF;
    @FXML private Label errorLabel;

    private final AuthService authService = new AuthService();


    @FXML
    public void initialize() {
        Platform.runLater(() -> usernameTF.requestFocus());
    }


    @FXML
    public void loginClicked(ActionEvent event) {

        hideError();

        String username = usernameTF.getText();
        String password = passwordTF.getText();

        if (username == null || username.isBlank()
                || password == null || password.isBlank()) {
            showError("Username and password required");
            return;
        }

        try {
            boolean success = authService.login(username.trim(), password);

            if (!success) {
                showError("Invalid username or password");
                return;
            }

            SceneManager.switchTo("main-view.fxml");

        } catch (Exception e) {
            e.printStackTrace();
            showError("Login failed – internal error");
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void hideError() {
        errorLabel.setVisible(false);
    }

    public void onEnterPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            loginClicked(null);
        }
    }
}
