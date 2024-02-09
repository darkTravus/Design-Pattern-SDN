package com.fges.todoapp.files;

import java.util.HashMap;
import java.util.Map;

public class FileHandlerRegistry {
    private Map<String, FileHandlerFactory> registry = new HashMap<>();

    public void registerFileFactory(String extension, FileHandlerFactory factory) {
        registry.put(extension, factory);
    }

    public FileHandlerFactory getFileFactory(String extension) {
        return registry.get(extension);
    }
}


