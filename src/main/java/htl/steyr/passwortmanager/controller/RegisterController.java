package htl.steyr.passwortmanager.controller;

import htl.steyr.passwortmanager.service.AuthService;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {
    public TextField registerUsernameField;
    public PasswordField registerPasswordField;
    public PasswordField confirmPasswordField;

    public void registerButtonClicked(ActionEvent actionEvent) {
        AuthService authService = new AuthService();
        try {
            authService.register(registerUsernameField.getText(), registerPasswordField.getText(), confirmPasswordField.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
