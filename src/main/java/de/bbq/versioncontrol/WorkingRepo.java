/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package de.bbq.versioncontrol;

import de.bbq.utils.fileHandler;
import static de.bbq.utils.fileHandler.showAlert;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
    ObservableList<String> items = FXCollections.observableArrayList();

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
        activerepo.loadIgnore();
    }
    @FXML
    private Button compButton;

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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Commit c = activerepo.getCommits().getLast();
        String line = c.getHash() + " " + formatter.format(new Date(c.getTimestamp())) + "          " + c.getMessage();
        commitHist.getItems().add(line);
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
        App.setRoot("LocalRepoView", 345, 420);
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

    @FXML
    private void compareCommits() {
        try {
            // Get the selected commit
            String selectedCommitLine = commitHist.getSelectionModel().getSelectedItem();

            if (selectedCommitLine == null) {
                showAlert(Alert.AlertType.INFORMATION, "Selection Error", null, "Please select a commit to compare.");
                return;
            }

            // Extract the hash of the selected commit
            String[] split = selectedCommitLine.split(" ");
            String selectedHash = split[0];

            // Find the index of the selected commit in the commit list
            LinkedList<Commit> commits = activerepo.getCommits();
            int selectedIndex = -1;

            for (int i = 0; i < commits.size(); i++) {
                if (commits.get(i).getHash().equals(selectedHash)) {
                    selectedIndex = i;
                    break;
                }
            }

            if (selectedIndex <= 0) {
                showAlert(Alert.AlertType.INFORMATION, "Comparison Error", null, "No previous commit to compare.");
                return;
            }

            // Get the selected commit and the previous commit
            Commit selectedCommit = commits.get(selectedIndex);
            Commit previousCommit = commits.get(selectedIndex - 1);

            // Retrieve the file states for both commits
            ArrayList<Index> selectedCommitFiles = activerepo.getIndexHistory().get(selectedCommit.getHash());
            ArrayList<Index> previousCommitFiles = activerepo.getIndexHistory().get(previousCommit.getHash());

            if (selectedCommitFiles == null || previousCommitFiles == null) {
                showAlert(Alert.AlertType.ERROR, "Comparison Error", null, "Could not load file states for the selected commits.");
                return;
            }

            // Compare the file states
            List<String> differences = compareFileStates(previousCommitFiles, selectedCommitFiles);

            // Display the differences
            StringBuilder diffOutput = new StringBuilder();
            for (String diff : differences) {
                diffOutput.append(diff).append("\n");
            }
            fileHandler.showAlert(Alert.AlertType.INFORMATION, "Comparison Result", null, diffOutput.toString());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Comparison Error", null, "An error occurred while comparing commits.");
        }
    }

    private List<String> compareFileStates(ArrayList<Index> commit1Files, ArrayList<Index> commit2Files) {
        List<String> differences = new ArrayList<>();

        // Map file paths to hashes for quick lookups
        Map<String, String> commit1Map = commit1Files.stream()
                .collect(Collectors.toMap(Index::getFilePath, Index::getHash));
        Map<String, String> commit2Map = commit2Files.stream()
                .collect(Collectors.toMap(Index::getFilePath, Index::getHash));

        // Check for added and modified files in commit2
        for (String filePath : commit2Map.keySet()) {
            if (!commit1Map.containsKey(filePath)) {
                differences.add("Added: " + filePath);
            } else if (!commit1Map.get(filePath).equals(commit2Map.get(filePath))) {
                differences.add("Modified: " + filePath);
            }
        }

        // Check for removed files in commit1
        for (String filePath : commit1Map.keySet()) {
            if (!commit2Map.containsKey(filePath)) {
                differences.add("Removed: " + filePath);
            }
        }

        return differences;
    }

}
