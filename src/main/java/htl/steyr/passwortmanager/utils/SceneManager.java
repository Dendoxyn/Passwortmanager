package htl.steyr.passwortmanager.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {

    private static Stage primaryStage;
    public static final String startupPagePath = "login-view.fxml";
    public static final String mainPagePath = "main-view.fxml";



    public static void init(Stage stage) {
        primaryStage = stage;
    }

    public static void switchTo(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneManager.class.getResource("/htl/steyr/passwortmanager/" + fxmlPath)
            );

            Parent root = loader.load();

            Scene scene = new Scene(root);
            primaryStage.setMaximized(true);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            throw new RuntimeException("Konnte Scene nicht laden: " + fxmlPath, e);
        }
    }
}