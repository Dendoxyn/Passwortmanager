package htl.steyr.passwortmanager.controller;

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
        if(passwordField.getText().isEmpty()) {
            errorLabel.setText("");
            errorLabel.setVisible(false);
            // Load the next page
            try {
                Parent root = FXMLLoader.load(getClass().getResource("main-page.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Main Page");
                stage.setScene(new Scene(root));
                stage.show();

                // Close current window
                Stage current = (Stage) passwordField.getScene().getWindow();
                current.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            errorLabel.setText("Wrong Password!");
            errorLabel.setVisible(true);
        }
    }
}
