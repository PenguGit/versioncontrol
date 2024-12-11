/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.bbq.versioncontrol;

import com.google.gson.reflect.TypeToken;
import de.bbq.utils.fileHandler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author qp
 */
public class repo {
    private static final HashSet<String> gudIgnore = new HashSet<>();
    private final TreeMap<String, ArrayList<Index>> indexHistory = new TreeMap<>();
    private final LinkedList<Commit> commits = new LinkedList<>();
    private final Path repoPath;

    public repo(Path repoPath) throws IOException {
        this.repoPath = repoPath;
    }

    public TreeMap<String, ArrayList<Index>> getIndexHistory() {
        return indexHistory;
    }

    public LinkedList<Commit> getCommits() {
        return commits;
    }

    public Path getRepoPath() {
        return repoPath;
    }

    private ArrayList<Index> traverseDir(File repo) throws IOException {

        ArrayList<Index> snapshot = new ArrayList<>();
        for (File file : repo.listFiles()) {
            Index fileState;
            String relativePath
                    = repoPath.relativize(file.toPath()).toString();
            if (isIgnored(relativePath)) {
            } else if (file.isDirectory()) {
                for (Index index : traverseDir(file)) {
                    snapshot.add(index);
                }
            } else {
                String fileHash = fileHandler.hashFile(file);
                fileState = new Index(relativePath, fileHash, fileHandler.readContent(file));
                snapshot.add(fileState);
            }
        }
        return snapshot;
    }

    public void loadCommit() {
        Path commitedFile = repoPath.resolve(".gud/commits.json");
        if (Files.exists(commitedFile)) {
            try (FileReader reader = new FileReader(commitedFile.toFile())) {
                commits.addAll(fileHandler.gson.fromJson(
                        reader,
                        new TypeToken<List<Commit>>() {
                        }.getType()
                ));
            } catch (IOException e) {
                System.err.println("Error loading commits: " + e.getMessage());
            }
        }
    }
    public void loadIgnore() {
        Path ignoreFile = repoPath.resolve("./.gudignore.txt");
        if (Files.exists(ignoreFile)) {
            try (BufferedReader reader = new BufferedReader(new FileReader(ignoreFile.toFile()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    gudIgnore.add(line);
                }
            } catch (IOException e) {
                System.err.println("Error loading gudignore: " + e.getMessage());
            }
        }
    }
    public static boolean isIgnored(String file) {
        String relPath = file.replace("\\", "/");
        for (String str : gudIgnore) {
            System.out.println(str);
            if ((str.contains(relPath))) {
                return true;
            }
        }
        return false;
    }
    
    public void loadIndex() {
        Path indexFile = repoPath.resolve(".gud/index.json");
        if (Files.exists(indexFile)) {
            try (FileReader reader = new FileReader(indexFile.toFile())) {
                indexHistory.putAll(fileHandler.gson.fromJson(
                        reader,
                        new TypeToken<Map<String, ArrayList<Index>>>() {
                        }.getType()
                ));
            } catch (IOException e) {
                System.err.println("Error loading Indices: " + e.getMessage());
            }
        }
    }

    public final void commit(String commitMsg) throws IOException {
        Commit commit;
        if (!commits.isEmpty()) {
            commit = new Commit(repoPath.toString(), commitMsg, commits.getLast().getHash());
            commits.add(commit);
        } else {
            commit = new Commit(repoPath.toString(), commitMsg, "001");
            commits.add(commit);
        }
        File repo = new File(repoPath.toString());
        if (!repo.exists() || !repo.isDirectory()) {
            throw new IllegalArgumentException("Invalid repository path");
        }
        indexHistory.put(commit.getHash(), traverseDir(repo));
        fileHandler.saveArray(indexHistory, repoPath.resolve(".gud/index.json"));
        fileHandler.saveArray(commits, repoPath.resolve(".gud/commits.json"));
    }
}
