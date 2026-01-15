package htl.steyr.passwortmanager.controller;

import htl.steyr.passwortmanager.dao.PasswordDAO;
import htl.steyr.passwortmanager.model.Password;
import htl.steyr.passwortmanager.security.UserContext;
import htl.steyr.passwortmanager.utils.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MainController {

    @FXML private TableView<Password> tableView;
    @FXML private TableColumn<Password, String> hostCol;
    @FXML private TableColumn<Password, String> userCol;
    @FXML private TableColumn<Password, String> pwCol;
    @FXML private TableColumn<Password, Void> actionCol;

    private final PasswordDAO passwordDAO = new PasswordDAO();

    private int currentUserId;

    @FXML
    public void initialize() {

        currentUserId = UserContext.getUserId();

    //    hostCol.setCellValueFactory(p -> p.getValue().websiteAppProperty());
      //  userCol.setCellValueFactory(p -> p.getValue().loginNameProperty());

        // Passwort immer maskiert anzeigen
     //   pwCol.setCellValueFactory(p -> p.getValue().dummyPasswordProperty());
        pwCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : "••••••••");
            }
        });

        // Eye-Button-Spalte
        actionCol.setCellFactory(col -> new TableCell<>() {

            private final Button eyeBtn = new Button("👁");

            {
                eyeBtn.setOnAction(e -> {
                    Password pw = getTableView().getItems().get(getIndex());

                    // hier nur zum Testen – KEIN Klartext speichern!
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Passwort");
                    alert.setHeaderText(pw.getWebsiteApp());
                    alert.setContentText("[ENT­SCHLÜS­SEL­TES PASSWORT]");
                    alert.showAndWait();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : eyeBtn);
            }
        });

        loadPasswords();
    }

    private void loadPasswords() {
        try {
            tableView.getItems().setAll(
                    passwordDAO.findPrivatePasswords(currentUserId)
            );
        } catch (Exception e) {
            e.printStackTrace();
            showError("Passwörter konnten nicht geladen werden");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }

    public void createNewPassword(ActionEvent event) {
        SceneManager.showPopup("add-password.fxml", "Add new Password", true);
    }
}