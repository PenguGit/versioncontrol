/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.bbq.versioncontrol;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author qp
 */
public class gud {
    private final Path repoPath;
    private final List<Commit> commits;
    private final Gson gson;
    
    public gud(Path repoPath) {
        this.repoPath = repoPath;
        this.commits = new ArrayList<>();
        this.gson = new Gson();
    }
    
    public void init() throws IOException {
        //Creating .gud
        Path gudDir = Paths.get(repoPath.toString(), ".gud");
        Files.createDirectories(gudDir);
    }
}
