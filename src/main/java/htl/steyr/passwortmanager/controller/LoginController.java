package htl.steyr.passwortmanager.controller;

import htl.steyr.passwortmanager.utils.SceneManager;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    public PasswordField loginPasswordField;
    public TextField loginUsernameField;
    public Label errorLabel;

    public void loginButtonClicked(ActionEvent actionEvent) {
        // @Todo hash it and compare with saved hash
        if (loginPasswordField.getText().isEmpty()) {
            errorLabel.setText("");
            errorLabel.setVisible(false);
            SceneManager.switchTo(SceneManager.mainPagePath);
        } else {
            errorLabel.setText("Wrong Password!");
            errorLabel.setVisible(true);
        }
    }

}
