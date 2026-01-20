package htl.steyr.passwortmanager.controller;

import htl.steyr.passwortmanager.dao.PasswordDAO;
import htl.steyr.passwortmanager.model.Password;
import htl.steyr.passwortmanager.security.CryptoService;
import htl.steyr.passwortmanager.security.UserContext;
import htl.steyr.passwortmanager.utils.SceneManager;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class MainController {

    @FXML public ImageView logoImg;
    @FXML public ImageView teamImg;
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

        logoImg.setImage(new Image(
                Objects.requireNonNull(getClass().getResource("/htl/steyr/passwortmanager/img/logo.png")).toExternalForm()
        ));

        teamImg.setImage(new Image(
                Objects.requireNonNull(getClass().getResource("/htl/steyr/passwortmanager/img/team.png")).toExternalForm()
        ));


        lockTableLayout();
        configureSortableColumns();


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

                        Alert alert = new Alert(Alert.AlertType.NONE);
                        alert.setTitle("Passwortdetails");

                        Label title = new Label("🔐 Passwort-Details");
                        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");

                        Label nameLbl = new Label("Name:");
                        nameLbl.setStyle("-fx-text-fill: #cbd5f5;");

                        Label nameVal = new Label(pw.getWebsiteApp());
                        nameVal.setStyle("-fx-text-fill: white;");

                        Label userLbl = new Label("Benutzer:");
                        userLbl.setStyle("-fx-text-fill: #cbd5f5;");

                        Label userVal = new Label(pw.getLoginName());
                        userVal.setStyle("-fx-text-fill: white;");

                        Label passLbl = new Label("Passwort:");
                        passLbl.setStyle("-fx-text-fill: #cbd5f5;");

                        TextField passVal = new TextField(plain);
                        passVal.setEditable(false);
                        passVal.setStyle("""
    -fx-background-color: #020617;
    -fx-text-fill: white;
    -fx-border-color: rgba(255,255,255,0.15);
    -fx-border-radius: 6;
    -fx-background-radius: 6;
""");


                        GridPane grid = new GridPane();
                        grid.setHgap(10);
                        grid.setVgap(10);

                        grid.addRow(0, nameLbl, nameVal);
                        grid.addRow(1, userLbl, userVal);
                        grid.addRow(2, passLbl, passVal);

                        VBox box = new VBox(12, title, grid);
                        box.setAlignment(Pos.CENTER_LEFT);

                        DialogPane pane = alert.getDialogPane();
                        pane.setContent(box);
                        pane.getButtonTypes().add(ButtonType.CLOSE);
                        pane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/htl/steyr/passwortmanager/auth.css")).toExternalForm());

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

    private void lockTableLayout() {

        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        for (TableColumn<?, ?> col : tableView.getColumns()) {
            col.setResizable(false);
            col.setReorderable(false);
        }

        tableView.lookupAll(".column-header").forEach(h -> h.setOnMouseClicked(Event::consume));
    }

    private void configureSortableColumns() {

        hostCol.setSortable(true);
        userCol.setSortable(true);
        tagCol.setSortable(true);
        secCol.setSortable(true);


        pwCol.setSortable(false);
        actionCol.setSortable(false);

        tableView.getSortOrder().clear();
        tableView.setSortPolicy(tv -> {
            if (tv.getSortOrder().size() > 1) {
                TableColumn<Password, ?> first = tv.getSortOrder().getFirst();
                tv.getSortOrder().setAll(first);
            }
            return true;
        });
    }


    private void disableColumnReordering(TableView<?> tableView) {

        tableView.widthProperty().addListener((obs, oldVal, newVal) -> {
            tableView.lookupAll(".column-header").forEach(header -> {
                header.setOnMousePressed(Event::consume);
                header.setOnMouseDragged(Event::consume);
                header.setOnDragDetected(Event::consume);
            });
        });
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
