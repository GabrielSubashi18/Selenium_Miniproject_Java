package org.example.Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {

    private FileUtils() {
    }

    public static String readFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read from file: " + filePath, e);
        }
    }
    public static void saveToFile(String fileName, String content) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save to file: " + fileName, e);
        }
    }
}
