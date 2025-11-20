package htl.steyr.passwortmanager.controller;

import htl.steyr.passwortmanager.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class LoginController {
    public PasswordField passwordField;
    public Button loginButton;
    public Label errorLabel;

    public void loginButtonClicked(ActionEvent actionEvent) {
        // @Todo hash it and compare with saved hash
        if (passwordField.getText().isEmpty()) {
            errorLabel.setText("");
            errorLabel.setVisible(false);
            // Load the next page
            try {

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            // Testing:
            SceneManager.switchTo(SceneManager.mainPagePath);

            errorLabel.setText("Wrong Password!");
            errorLabel.setVisible(true);
        }
    }
}
