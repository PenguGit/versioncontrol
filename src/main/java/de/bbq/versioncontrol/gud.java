/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.bbq.versioncontrol;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributeView;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author qp
 */
public class gud {

    private final Path repoPath;
    private final LinkedList<Commit> commits = new LinkedList<>();
    private final Gson gson;

    public gud(Path repoPath) {
        this.repoPath = repoPath;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        loadCommit();
    }

    public void init() throws IOException {
        //Creating .gud
        try {
            Path gudDir = Paths.get(repoPath.toString());
            Files.createDirectories(gudDir);

            if ((System.getProperty("os.name").toLowerCase()).contains("win")) {
                DosFileAttributeView attributes
                        = Files.getFileAttributeView(
                                gudDir,
                                DosFileAttributeView.class
                        );
                if (attributes != null) {
                    attributes.setHidden(true);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void saveCommit() {
        Path commitFile = repoPath.resolve("commits.json");
        try (FileWriter writer = new FileWriter(commitFile.toFile())) {
            gson.toJson(commits, writer);
        } catch (IOException e) {
            System.err.println("Error saving/loading commit: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadCommit() {
        Path commitedFile = repoPath.resolve("commits.json");
        if (Files.exists(commitedFile)) {
            try (FileReader reader = new FileReader(commitedFile.toFile())) {
                commits.addAll(gson.fromJson(
                        reader,
                        new TypeToken<List<Commit>>() {
                        }.getType()
                ));
            } catch (IOException e) {
                System.err.println("Error saving/loading commit: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void commit(String commitMsg) {
        if (!commits.isEmpty()) {
            commits.add(
                    new Commit(
                            repoPath.toString(),
                            commitMsg,
                            commits
                                    .getLast()
                                    .getHash()
                    )
            );
        } else {
            commits.add(
                    new Commit(
                            repoPath.toString(),
                            commitMsg,
                            "001"
                    )
            );

        }
        saveCommit();

    }
}
