/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.bbq.versioncontrol;

/**
 *
 * @author qp
 */
public class Commit {
    private final String path;
    private final String message;
    private final String hash;
    private final long timestamp;
    private final String parentHash;

    public Commit(String repoPath, String message, String parentHash) {
        this.path = repoPath;
        this.parentHash = parentHash;
        this.hash = simpleHash();
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public String getPath() {
        return path;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    private String simpleHash() {
        String data = message + parentHash;
        int hashcode = 0;
        for (int c : data.toCharArray()) {
            hashcode += c;
        }
        return Integer.toString(hashcode);
    }

    public String getHash() {
        return hash;
    }

    public String getParentHash() {
        return parentHash;
    }

}
