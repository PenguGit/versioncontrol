/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package de.bbq.versioncontrol;

import de.bbq.utils.fileHandler;
import java.io.IOException;
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
public class LocalRepoView {

    @FXML
    private ListView<String> localrepoView;
    @FXML
    private Button backButton;
    @FXML
    private Button chooseRepo;

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private void backButton() throws IOException {
        App.setRoot("startWindow", 233, 133);
    }
    
    @FXML
    private void chooseRepo() throws IOException {
        String selectedRepo = localrepoView.getSelectionModel().getSelectedItem();
        if (selectedRepo != null) {
            WorkingRepo.grabRepo(selectedRepo);
        }
        App.setRoot("WorkingRepo", 800, 400);
    }
    
    public void initialize() {
        ObservableList<String> items = FXCollections.observableArrayList(fileHandler.localRepos);
        localrepoView.setItems(items);
        chooseRepo.setDisable(true);
        localrepoView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            chooseRepo.setDisable(newValue == null); // Disable if nothing is selected
        });
    }    
    
}
