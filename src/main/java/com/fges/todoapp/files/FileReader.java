// com.fges.todoapp.files.FileReader
package com.fges.todoapp.files;

import com.fges.todoapp.util.PathValidator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileReader {
    public static String readFileContent(Path filePath, PathValidator pathValidator) {
        try {
            if (pathValidator.validatePath(filePath)) {
                return Files.readString(filePath);
            } else {
                //System.err.println("Le chemin du fichier n'est pas valide");
                return "";
            }
        } catch (IOException ex) {
            System.err.println("Impossible de lire le fichier: " + ex.getMessage());
            return "";
        }
    }
}
