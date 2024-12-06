package de.bbq.versioncontrol;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

public class mainWindow {
    
    private Path filePath;
    @FXML
    private Button commitButton;

    public static void commitMessage() {
    }
    @FXML
    private Button initRepo;
    @FXML
    private TextField repoPathTF;
    @FXML
    private Button folderButton;
    
    @FXML
    private void chooseFolder () {
        DirectoryChooser dC = new DirectoryChooser();
        dC.setInitialDirectory(filePath.toFile());
        File selectedDir = dC.showDialog(null);
        if (selectedDir != null) {
            repoPathTF.setText(selectedDir.getPath());
        } else {
            repoPathTF.clear();
        }
    }
    
    @FXML
    private void initChosenRepo () throws IOException {
        try {
            if (!repoPathTF.getText().isEmpty()) {
                filePath = Paths.get(repoPathTF.getText());
            } else {
                return;
            }
            repo repo = new repo(filePath);
        } catch (Exception e) {
            repoPathTF.setPromptText("Not a valid location.");
        }
        
    }
    
    
    public void initialize() throws IOException {
        filePath = Paths.get(System.getProperty("user.home"), "Documents");
    }
}