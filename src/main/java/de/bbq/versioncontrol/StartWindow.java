/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package de.bbq.versioncontrol;

import de.bbq.utils.fileHandler;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author qp
 */
public class StartWindow {

    @FXML
    private Button chooseRepo;
    @FXML
    private Button createRepo;

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private void createNewRepoWindow() throws IOException {
        App.setRoot("mainWindow", 308,200);
    }
    
    @FXML
    private void localRepoWindow() throws IOException {
        App.setRoot("localRepoView",265,420);
    }
    
    public void initialize() {
        fileHandler.loadlocalRepos();
    }    
}
