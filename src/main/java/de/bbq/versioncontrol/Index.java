/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.bbq.versioncontrol;

/**
 *
 * @author qp
 */
public class Index {

    private final String filePath;
    private final String hash;
    //private final long date;
    private String content;

    public Index(String filePath, String hash, String content) {
        this.content = content;
        this.filePath = filePath;
        this.hash = hash;
        //date = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    }

    public String getFilePath() {
        return filePath;
    }

    public String getHash() {
        return hash;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
