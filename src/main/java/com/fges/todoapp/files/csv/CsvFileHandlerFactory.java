package com.fges.todoapp.files.csv;

import com.fges.todoapp.files.FileHandler;
import com.fges.todoapp.files.FileHandlerFactory;

public class CsvFileHandlerFactory implements FileHandlerFactory {
    @Override
    public FileHandler createFileHandler() {
        return new CsvFileHandler();
    }
}
