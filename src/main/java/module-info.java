module qrscan {
    requires javafx.controls;
    requires javafx.fxml;

    opens nl.ls31 to javafx.fxml;
    exports nl.ls31;
}