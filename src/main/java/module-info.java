module htl.steyr.passwortmanager {
    requires javafx.controls;
    requires javafx.fxml;


    opens htl.steyr.passwortmanager to javafx.fxml;
    exports htl.steyr.passwortmanager;
}