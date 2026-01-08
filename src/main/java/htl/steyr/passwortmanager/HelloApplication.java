package htl.steyr.passwortmanager;

import htl.steyr.passwortmanager.utils.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        SceneManager.init(stage);
        SceneManager.switchTo("auth-view.fxml");
    }
}
