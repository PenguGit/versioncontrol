package de.bbq.versioncontrol;

import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class mainWindow {

    @FXML
    private Button commitButton;

    public static void commitMessage() {
        
    }

    public void initialize() {
        String filepath = System.getProperty("user.home") + "/Documents/versioncontrol/";
        try {
            File dir = new File(filepath);
            dir.mkdir();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
