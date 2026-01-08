package htl.steyr.passwortmanager.controller;

import htl.steyr.passwortmanager.service.AuthService;
import htl.steyr.passwortmanager.utils.SceneManager;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {
    public TextField registerUsernameField;
    public PasswordField registerPasswordField;
    public PasswordField confirmPasswordField;
    public Label errorLabel;

    public void registerButtonClicked(ActionEvent actionEvent) {
        AuthService authService = new AuthService();

        errorLabel.setText("");
        errorLabel.setVisible(true);

        try {
            boolean success = authService.register(registerUsernameField.getText(), registerPasswordField.getText(), confirmPasswordField.getText());

            if (success) {
                errorLabel.setText("User " + registerUsernameField.getText() + " successfully registered!");
                errorLabel.setStyle("-fx-text-fill: green;");
                SceneManager.switchTo("main-view.fxml");
            } else{
                errorLabel.setText("Error");
                errorLabel.setStyle("-fx-text-fill: RED");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
