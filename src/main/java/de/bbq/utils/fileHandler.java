/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.bbq.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.DosFileAttributeView;
import java.util.LinkedHashSet;
import java.util.Set;
import javafx.scene.control.Alert;

/**
 *
 * @author qp
 */
public class fileHandler {

    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static final LinkedHashSet<String> localRepos = new LinkedHashSet<>();

    public static String hashFile(File file) throws IOException {
        long checksum = 0;

        String filename = file.getName();
        for (char ch : filename.toCharArray()) {
            checksum += (int) ch; // Convert char to int and add
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            int byteData;
            while ((byteData = fis.read()) != -1) {
                checksum += byteData; // Sum up the bytes in the file
            }
        }
        // Convert the checksum to a hexadecimal string
        return Long.toHexString(checksum);
    }

    public static String readContent(File file) throws IOException {
        StringBuilder sB = new StringBuilder();
        try (BufferedReader input = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = input.readLine()) != null) {
                sB.append(line).append("\n");
            }
        }
        String content = sB.toString();
        return content;
    }

    public static void initRepoFiles(Path repoPath) throws IOException {
        //Creating .repo
        try {
            Files.createDirectories(repoPath.resolve(".gud"));
            createIgnoreFIle(repoPath);
            Files.createFile(repoPath.resolve(".gud/commits.json"));
            Files.createFile(repoPath.resolve(".gud/index.json"));
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
            localRepos.add(repoPath.toString());
            saveArray(localRepos,
                    Paths.get(
                            System.getProperty("user.home"),
                            "Documents")
                            .resolve("localRepos.json"));
        } catch (IOException e) {
            System.out.println("lul");
        }
    }

    private static void createIgnoreFIle(Path repoPath) throws IOException {
        File file = repoPath.resolve("./.gudignore.txt").toFile();
        String relativePath = repoPath.relativize(repoPath.resolve(".gud")).toString();
        String initText = relativePath + "\n" + ".gudignore.txt";
        try {
            file.createNewFile();
            Files.writeString(file.toPath(), initText);
        } catch (IOException e) {

        }
    }

    public static void loadlocalRepos() {
        Path commitedFile = Paths.get(System.getProperty("user.home"), "Documents", "localRepos.json");
        if (Files.exists(commitedFile)) {
            try (FileReader reader = new FileReader(commitedFile.toFile())) {
                localRepos.addAll(gson.fromJson(
                        reader,
                        new TypeToken<Set<String>>() {
                        }.getType()
                ));
            } catch (IOException e) {
                System.err.println("Error loading Repos: " + e.getMessage());
            }
        }
    }

    public static void saveArray(Object array, Path filePath) {
        Path commitFile = filePath;
        try (FileWriter writer = new FileWriter(commitFile.toFile())) {
            gson.toJson(array, writer);
        } catch (IOException e) {
            System.err.println("Error saving " + Object.class + ": " + e.getMessage());
        }
    }

    public static void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

}
