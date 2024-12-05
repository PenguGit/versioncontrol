package de.bbq.versioncontrol;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class mainWindow {

    @FXML
    private Button commitButton;

    public static void commitMessage() {
        
    }

    public void initialize() throws IOException {
        Path filePath = Paths.get(System.getProperty("user.home"),"Documents", "versioncontrol");
        try {
            filePath = filePath.resolve(".gud");
        } catch (Exception e) {
            e.printStackTrace();
        }
        gud repo = new gud(filePath);
        repo.init();
        repo.commit("ha");
        
    }
}
