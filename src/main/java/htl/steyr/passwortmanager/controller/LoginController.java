package htl.steyr.passwortmanager.controller;

import htl.steyr.passwortmanager.service.AuthService;
import htl.steyr.passwortmanager.utils.SceneManager;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    public TextField loginUsernameField;
    public PasswordField loginPasswordField;
    public Label errorLabel;

    private final AuthService authService = new AuthService();

    public void loginButtonClicked(ActionEvent event) throws Exception {

        hideError();

        String username = loginUsernameField.getText().trim();
        String password = loginPasswordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Username and password required");
            return;
        }

        boolean success = authService.login(username, password);

        if (!success) {
            showError("Invalid username or password");
            return;
        }

        SceneManager.switchTo(SceneManager.mainView);
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    private void hideError() {
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }
}
