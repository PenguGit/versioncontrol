module de.bbq.versioncontrol {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires com.google.gson;

    opens de.bbq.versioncontrol to javafx.fxml;
    exports de.bbq.versioncontrol;
}
