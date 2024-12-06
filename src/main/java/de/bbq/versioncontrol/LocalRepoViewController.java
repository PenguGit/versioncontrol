/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package de.bbq.versioncontrol;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

/**
 * FXML Controller class
 *
 * @author qp
 */
public class LocalRepoViewController {

    @FXML
    private ListView<String> localrepoView;

    /**
     * Initializes the controller class.
     */
    
    public void initialize() {
        ObservableList<String> items = FXCollections.observableArrayList(repo.localRepos);
        localrepoView.setItems(items);
    }    
    
}
