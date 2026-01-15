package htl.steyr.passwortmanager.controller;

import htl.steyr.passwortmanager.model.Password;
import htl.steyr.passwortmanager.model.PasswordTag;
import htl.steyr.passwortmanager.model.SecurityLevel;
import htl.steyr.passwortmanager.security.CryptoService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.nio.charset.StandardCharsets;

@Setter
@Getter
public class AddPasswordController {

    public TextField websiteAppTF;
    public TextField usernameTF;
    public PasswordField passwordPF;
    public TextArea noteTA;
    public ComboBox<PasswordTag> tagCB;
    public ComboBox<SecurityLevel> securityTagCB;

    private Password password;
    private Stage stage;

    // must be set by caller
    private int userId;
    private Integer groupId;

    // ================= INIT =================

    @FXML
    private void initialize() {

        tagCB.setItems(FXCollections.observableArrayList(PasswordTag.values()));
        securityTagCB.setItems(FXCollections.observableArrayList(SecurityLevel.values()));

        tagCB.getSelectionModel().selectFirst();
        securityTagCB.getSelectionModel().selectFirst();
    }

    // ================= SAVE =================

    @FXML
    private void saveClicked() {
        if (!initPassword()) return;
        stage.close();
    }

    // ================= CANCEL =================

    @FXML
    private void cancelClicked() {
        password = null;
        stage.close();
    }

    // ================= CREATE PASSWORD =================

    private boolean initPassword() {

        String website = websiteAppTF.getText().trim();
        String login = usernameTF.getText().trim();
        String plainPwd = passwordPF.getText();
        String note = noteTA.getText() == null ? "" : noteTA.getText().trim();

        if (website.isEmpty() || login.isEmpty() || plainPwd.isEmpty()) {
            showAlert("Website, username and password are required.");
            return false;
        }

        try {
            byte[] encrypted = CryptoService.getInstance()
                    .encrypt(plainPwd.getBytes(StandardCharsets.UTF_8));

            password = new Password(
                    website,
                    login,
                    encrypted,
                    note,
                    tagCB.getValue(),
                    securityTagCB.getValue(),
                    userId,
                    groupId
            );

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Encryption failed. Password not saved.");
            return false;
        }
    }

    // ================= UTILS =================

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.initOwner(stage);
        alert.showAndWait();
    }
}
