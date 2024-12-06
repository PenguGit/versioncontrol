/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.bbq.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author qp
 */
public class fileHandler {

    public static String hashFile(File file) throws IOException {
        long checksum = 0;

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
}

