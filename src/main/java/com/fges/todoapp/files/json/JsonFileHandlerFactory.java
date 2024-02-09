package com.fges.todoapp.files.json;

import com.fges.todoapp.files.FileHandler;
import com.fges.todoapp.files.FileHandlerFactory;

public class JsonFileHandlerFactory implements FileHandlerFactory {
    @Override
    public FileHandler createFileHandler() {
        return new JsonFileHandler();
    }
}
