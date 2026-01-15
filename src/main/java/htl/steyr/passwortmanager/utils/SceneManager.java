package htl.steyr.passwortmanager.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class SceneManager {

    private static Stage stage;
    private static Scene scene;

    public static void init(Stage primaryStage) {
        stage = primaryStage;

        scene = new Scene(new StackPane());
        stage.setScene(scene);

        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(null);
        stage.setMaximized(true);
        stage.show();
    }

    public static void switchTo(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(
                    Objects.requireNonNull(
                            SceneManager.class.getResource("/htl/steyr/passwortmanager/" + fxmlPath)
                    )
            );

            scene.setRoot(root);

            if (!stage.isMaximized())
                stage.setMaximized(true);

        } catch (Exception e) {
            throw new RuntimeException("Konnte Scene nicht laden: " + fxmlPath, e);
        }
    }

    public static Stage showPopup(String fxmlPath, String title, boolean wait) {
        try {
            Parent root = FXMLLoader.load(
                    Objects.requireNonNull(
                            SceneManager.class.getResource("/htl/steyr/passwortmanager/" + fxmlPath)
                    )
            );

            Stage popup = new Stage();
            popup.initOwner(stage);
            popup.setResizable(false);
            popup.setTitle(title);
            popup.setScene(new Scene(root));

            if (wait) popup.showAndWait();
            else popup.show();

            return popup;

        } catch (Exception e) {
            throw new RuntimeException("Konnte Popup nicht laden: " + fxmlPath, e);
        }
    }
}
