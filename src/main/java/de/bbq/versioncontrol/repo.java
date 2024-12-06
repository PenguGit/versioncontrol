/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.bbq.versioncontrol;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import de.bbq.utils.fileHandler;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributeView;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author qp
 */
public class repo {
    private final TreeMap<String, ArrayList<Index>> indexHistory = new TreeMap<>();
    private final Path repoPath;
    private final LinkedList<Commit> commits = new LinkedList<>();
    private final Gson gson;
    private final GudIgnore gIgnore;
    

    public repo(Path repoPath) throws IOException {
        this.repoPath = repoPath;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        gIgnore = new GudIgnore(repoPath.resolve(".gudignore.txt").toFile());
        try {
            Files.createDirectories(repoPath.resolve(".gud"));
            Files.createFile(repoPath.resolve(".gud/commits.json"));
            Files.createFile(repoPath.resolve(".gud/index.json"));
            commit("Initialize");
            if ((System.getProperty("os.name").toLowerCase()).contains("win")) {
                DosFileAttributeView attributes
                        = Files.getFileAttributeView(
                                repoPath.resolve(".gud"),
                                DosFileAttributeView.class
                        );
                if (attributes != null) {
                    attributes.setHidden(true);
                }
            }
        } catch (IOException e) {
        }
        loadCommit();
        loadIndices();
    }

    private void indexDir() throws IOException {
        File repo = new File(repoPath.toString());
        if (!repo.exists() || !repo.isDirectory()) {
            throw new IllegalArgumentException("Invalid repository path");
        }
        indexHistory.put(String.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)), traverseDir(repo));
    }

    private ArrayList<Index> traverseDir(File dir) throws IOException {
        ArrayList<Index> snapshot = new ArrayList<>();
        for (File file : dir.listFiles()) {
            Index fileState;
            String relativePath
                        = repoPath.relativize(file.toPath()).toString();
            if (gIgnore.isIgnored(relativePath)){
                
            } else if (file.isDirectory()) {
                for (Index index : traverseDir(file)) {
                    snapshot.add(index);
                }
            } else {
                String fileHash = fileHandler.hashFile(file);
                System.out.println(relativePath);
                fileState = new Index(relativePath, fileHash, fileHandler.readContent(file));
                snapshot.add(fileState);
            }
        }
        return snapshot;
    }

    public void init() throws IOException {
        //Creating .repo
    }

    private void saveCommits() {
        Path commitFile = repoPath.resolve(".gud/commits.json");
        try (FileWriter writer = new FileWriter(commitFile.toFile())) {
            gson.toJson(commits, writer);
        } catch (IOException e) {
            System.err.println("Error saving/loading commit: " + e.getMessage());
        }
    }

    private void saveIndices() {
        Path commitFile = repoPath.resolve(".gud/index.json");
        try (FileWriter writer = new FileWriter(commitFile.toFile())) {
            gson.toJson(indexHistory, writer);
        } catch (IOException e) {
            System.err.println("Error saving/loading commit: " + e.getMessage());
        }
    }

    private void loadCommit() {
        Path commitedFile = repoPath.resolve(".gud/commits.json");
        if (Files.exists(commitedFile)) {
            try (FileReader reader = new FileReader(commitedFile.toFile())) {
                commits.addAll(gson.fromJson(
                        reader,
                        new TypeToken<List<Commit>>() {
                        }.getType()
                ));
            } catch (IOException e) {
                System.err.println("Error saving/loading commit: " + e.getMessage());
            }
        }
    }

    private void loadIndices() {
        Path commitedFile = repoPath.resolve(".gud/index.json");
        if (Files.exists(commitedFile)) {
            try (FileReader reader = new FileReader(commitedFile.toFile())) {
                indexHistory.putAll(gson.fromJson(
                        reader,
                        new TypeToken<Map<String, ArrayList<Index>>>() {
                        }.getType()
                ));
            } catch (IOException e) {
                System.err.println("Error saving/loading commit: " + e.getMessage());
            }
        }
    }

    public final void commit(String commitMsg) throws IOException {
        indexDir();
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
        saveIndices();
        saveCommits();

    }
}
