package htl.steyr.passwortmanager.controller;

import htl.steyr.passwortmanager.dao.PasswordDAO;
import htl.steyr.passwortmanager.model.Password;
import htl.steyr.passwortmanager.security.CryptoService;
import htl.steyr.passwortmanager.security.UserContext;
import htl.steyr.passwortmanager.utils.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;

import java.nio.charset.StandardCharsets;

public class MainController {

    @FXML private TableView<Password> tableView;

    @FXML private TableColumn<Password, String> hostCol;
    @FXML private TableColumn<Password, String> userCol;
    @FXML private TableColumn<Password, String> pwCol;
    @FXML private TableColumn<Password, String> tagCol;
    @FXML private TableColumn<Password, String> secCol;
    @FXML private TableColumn<Password, Void> actionCol;
    @FXML private TextField searchTF;

    private final ObservableList<Password> masterData = FXCollections.observableArrayList();
    private FilteredList<Password> filteredData;


    private final PasswordDAO passwordDAO = new PasswordDAO();

    private int currentUserId;

    // ================= INIT =================

    @FXML
    public void initialize() {

        currentUserId = UserContext.getUserId();

        // ===== TEXT COLUMNS =====

        hostCol.setCellValueFactory(p ->
                new javafx.beans.property.SimpleStringProperty(
                        p.getValue().getWebsiteApp()
                )
        );

        userCol.setCellValueFactory(p ->
                new javafx.beans.property.SimpleStringProperty(
                        p.getValue().getLoginName()
                )
        );

        tagCol.setCellValueFactory(p ->
                new javafx.beans.property.SimpleStringProperty(
                        p.getValue().getTag().name()
                )
        );

        secCol.setCellValueFactory(p ->
                new javafx.beans.property.SimpleStringProperty(
                        p.getValue().getSecurity().name()
                )
        );

        // ===== PASSWORD COLUMN (MASKED) =====

        pwCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setText(null);
                } else {
                    Password pw = getTableView().getItems().get(getIndex());
                    int len = Math.min(12, pw.getEncryptedPwd().length);
                    setText("•".repeat(len));
                }
            }
        });

        // ===== ACTION COLUMN (EYE BUTTON) =====

        actionCol.setCellFactory(col -> new TableCell<>() {

            private final Button eyeBtn = new Button("👁");


            {

                setAlignment(Pos.CENTER);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

                eyeBtn.setOnAction(e -> {
                    Password pw = getTableView().getItems().get(getIndex());


                    try {
                        byte[] decrypted = CryptoService.getInstance()
                                .decrypt(pw.getEncryptedPwd());

                        String plain = new String(decrypted, StandardCharsets.UTF_8);

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Entschlüsseltes Passwort");
                        alert.setHeaderText(pw.getWebsiteApp());
                        alert.setContentText(plain);
                        alert.showAndWait();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        showError("Password could not be decrypted");
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : eyeBtn);
            }
        });

        filteredData = new FilteredList<>(masterData, p -> true);

        SortedList<Password> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);

        searchTF.textProperty().addListener((obs, oldVal, newVal) -> {
            String filter = newVal.toLowerCase().trim();

            filteredData.setPredicate(pw -> {
                if (filter.isEmpty()) return true;

                return pw.getWebsiteApp().toLowerCase().contains(filter)
                        || pw.getLoginName().toLowerCase().contains(filter)
                        || pw.getTag().name().toLowerCase().contains(filter)
                        || pw.getSecurity().name().toLowerCase().contains(filter);
            });
        });

        loadPasswords();

    }

    // ================= LOAD =================

    private void loadPasswords() {
        try {
            masterData.setAll(
                    passwordDAO.findPrivatePasswords(currentUserId)
            );
        } catch (Exception e) {
            e.printStackTrace();
            showError("Passwörter konnten nicht geladen werden");
        }
    }


    // ================= ADD =================

    public void createNewPassword(ActionEvent event) {

        try {
            AddPasswordController ctrl =
                    SceneManager.showPopupWithController("add-password.fxml", "Add new Password");

            Password result = ctrl.getPassword();

            // user pressed cancel
            if (result == null) return;

            System.out.println("Current userId = " + UserContext.getUserId());
            System.out.println("Password userId = " + result.getUserId());

            passwordDAO.insert(result);
            loadPasswords();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Password could not be saved");
        }
    }

    // ================= UTILS =================

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }
}
