/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package de.bbq.versioncontrol;

import de.bbq.utils.fileHandler;
import java.io.IOException;
import java.nio.file.Paths;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

/**
 * FXML Controller class
 *
 * @author qp
 */
public class WorkingRepo {

    private static repo activerepo;
    
    @FXML
    private ListView<String> commitHist;
    @FXML
    private Button buttonBack;

    public static void grabRepo(String repoPath) throws IOException {
        activerepo = new repo(Paths.get(repoPath));
        activerepo.loadCommit();
        activerepo.loadIndex();
    }
    

    @FXML
    private void backButton() throws IOException {
        App.setRoot("LocalRepoView", 265, 420);
    }

    public void initialize() {
        ObservableList<String> items = FXCollections.observableArrayList();
        for (Commit c : activerepo.getCommits()) {
            String line = c.getTimestamp() + "          " + c.getMessage();
            items.add(line);
        }
        commitHist.setItems(items);
    }

}
