package de.bbq.versioncontrol;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class mainWindow {

    private Stage secondaryStage;

    private Path filePath;
    @FXML
    private Button backButton;

    public static void commitMessage() {
    }
    @FXML
    private Button initRepo;
    @FXML
    private TextField repoPathTF;
    @FXML
    private Button folderButton;
    @FXML
    private Button showLocRepos;

    @FXML
    private void chooseFolder() {
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
    private void showlocalRepo() {
        try {
            secondaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("localRepoView.fxml"));
            Scene scene = new Scene(loader.load());
            secondaryStage.setScene(scene);
            secondaryStage.show();
            secondaryStage.setResizable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initChosenRepo() throws IOException {
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

    @FXML
    private void backButton() throws IOException {
        App.setRoot("startWindow", 233, 133);
    }

    public void initialize() throws IOException {

        filePath = Paths.get(System.getProperty("user.home"), "Documents");
    }
}
