package com.fges.todoapp.storage;

import com.fges.todoapp.storage.files.csv.CsvFileHandler;
import com.fges.todoapp.storage.files.json.JsonFileHandler;
import com.fges.todoapp.storage.nonfiles.DatabaseFileHandler;

import java.util.HashMap;
import java.util.Map;

public class StorageFactory {
    private final Map<String, StorageHandler> registry = new HashMap<>();

    private void initialiseHandlers() {
        registry.put("csv", new CsvFileHandler());
        registry.put("json", new JsonFileHandler());

        registry.put("api", new DatabaseFileHandler());
    }

    public StorageFactory() {
        this.initialiseHandlers();
    }

    public StorageHandler detectHandler(String sourceType) {
        String fileExtension = this.getFileExtension(sourceType);
        StorageHandler handler;

        if (fileExtension == null) {
            handler = this.registry.get(sourceType);
        } else {
            handler = this.registry.get(fileExtension);
        }

        if (handler == null) {
            System.err.println("Unsupported data source type: " + sourceType);
            return null;
        }
        return handler;
    }

    private String getFileExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return null;
        }
        return filename.substring(lastDotIndex + 1).toLowerCase();
    }
}
