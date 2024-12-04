module de.bbq.versioncontrol {
    requires javafx.controls;
    requires javafx.fxml;

    opens de.bbq.versioncontrol to javafx.fxml;
    exports de.bbq.versioncontrol;
}
