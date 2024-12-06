/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.bbq.versioncontrol;

import de.bbq.utils.fileHandler;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

/**
 *
 * @author qp
 */
public class GudIgnore {
    private final HashSet<String> gudIgnore = new HashSet<>();
    
    public GudIgnore(File repoPath) throws IOException {
        try {
            String[] ignoredFiles = fileHandler.readContent(repoPath).split("\n");
            gudIgnore.addAll(Arrays.asList(ignoredFiles));
        } catch (Exception e) {
            
        }
    }
    
    public boolean isIgnored(String file) {
        String relPath = file.replace("\\", "/");
        for (String str : gudIgnore) {
            if ((str.contains(relPath))) {
                return true;
            }
        }
        return false;
    }
    
}
