package com.fges.todoapp.files;

import com.fges.todoapp.files.csv.CsvFileHandler;
import com.fges.todoapp.files.json.JsonFileHandler;

import java.util.HashMap;
import java.util.Map;

public class FileHandlerRegistry {
    private final Map<String, FileHandler> registry = new HashMap<>();

    private void initialiseFileHandler() {
        registry.put("csv", new CsvFileHandler());
        registry.put("json", new JsonFileHandler());
    }
    private String getFileExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        return filename.substring(lastDotIndex + 1).toLowerCase();
    }
    public FileHandlerRegistry() {
        this.initialiseFileHandler();
    }
    public FileHandler detectFileType (String filename) {
        String fileExtension = this.getFileExtension(filename);
        FileHandler fileHandler = this.registry.get(fileExtension);
        if (fileHandler == null) {
            System.err.println("Extension de fichier non prise en charge : " + fileExtension);
            return null;
        }
        return fileHandler;
    }
}


