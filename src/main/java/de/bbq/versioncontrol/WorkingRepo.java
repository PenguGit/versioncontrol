/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package de.bbq.versioncontrol;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;

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
    private ListView<String> affectedList;

    @FXML
    private Button chooseCommit;

    @FXML
    private Button buttonBack;

    @FXML
    private Button commitButton;

    public static void grabRepo(String repoPath) throws IOException {
        activerepo = new repo(Paths.get(repoPath));
        activerepo.loadCommit();
        activerepo.loadIndex();
    }

    @FXML
    private void commitNow() {
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setTitle("Input Dialog");
        inputDialog.setHeaderText("Enter your input:");
        inputDialog.setContentText("Input:");
        inputDialog.showAndWait().ifPresent(input -> {
            try {
                // Use the input (update label)
                activerepo.commit(input);
            } catch (Exception e) {
            }

        });
    }

    @FXML
    private void chooseCommit() throws IOException {
        try {
            ObservableList<String> indexitems = FXCollections.observableArrayList();
            String comHash = commitHist.getSelectionModel().getSelectedItem();
            String[] tempSplit = comHash.split(" ");
            ArrayList<Index> item = activerepo.getIndexHistory().get(tempSplit[0]);
            for (Index index : item) {
                indexitems.add(index.getFilePath());
            }
            affectedList.setItems(indexitems);
        } catch (Exception e) {
            System.out.println("Error Loading Commit Index.");
        }
    }

    @FXML
    private void backButton() throws IOException {
        App.setRoot("LocalRepoView", 265, 420);
    }

    public void initialize() {
        ObservableList<String> items = FXCollections.observableArrayList();

        for (Commit c : activerepo.getCommits()) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String line = c.getHash() + " " + formatter.format(new Date(c.getTimestamp())) + "          " + c.getMessage();
            items.add(line);
        }

        commitHist.setItems(items);
    }

}
