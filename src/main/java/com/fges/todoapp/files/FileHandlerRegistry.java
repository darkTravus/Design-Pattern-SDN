package com.fges.todoapp.files;

import java.util.HashMap;
import java.util.Map;

public class FileHandlerRegistry {
    private Map<String, FileHandler> registry = new HashMap<>();

    public void registerFileFactory(String extension, FileHandler fileHandler) {
        registry.put(extension, fileHandler);
    }

    public FileHandler getFileHandler(String extension) {
        return registry.get(extension);
    }
}


