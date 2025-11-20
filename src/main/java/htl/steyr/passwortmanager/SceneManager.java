package htl.steyr.passwortmanager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {

    private static Stage primaryStage;
    public static final String startupPagePath = "main-page.fxml";



    public static void init(Stage stage) {
        primaryStage = stage;
    }

    public static void switchTo(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            throw new RuntimeException("Konnte Scene nicht laden: " + fxmlPath, e);
        }
    }
}
