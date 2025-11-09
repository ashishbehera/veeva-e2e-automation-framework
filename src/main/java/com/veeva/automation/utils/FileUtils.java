package com.veeva.automation.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {

    /**
     * Reads the content of a JS file and returns it as a String.
     *
     * @param pathStr path to the file
     * @return file content as String
     */
    public static String readJsFile(String pathStr) {
        try {
            Path path = Paths.get(pathStr);
            return Files.readString(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JS file: " + pathStr, e);
        }
    }
}
