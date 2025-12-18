package htl.steyr.passwortmanager.controller;

import htl.steyr.passwortmanager.utils.SceneManager;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

public class MainController {
    public PasswordField passwordField;
    public Button loginButton;
    public Label errorLabel;


    public void loginButtonClicked(ActionEvent actionEvent) {
        // @Todo hash it and compare with saved hash
        if(passwordField.getText().isEmpty()) {
            errorLabel.setText("");
            errorLabel.setVisible(false);
        } else {
            TODO:
            SceneManager.switchTo(SceneManager.mainView);
        }
    }
}