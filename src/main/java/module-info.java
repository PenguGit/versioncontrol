module de.bbq.versioncontrol {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens de.bbq.versioncontrol to javafx.fxml;
    exports de.bbq.versioncontrol;
}
