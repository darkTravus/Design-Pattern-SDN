package com.fges.todoapp.commands;

import com.fges.todoapp.files.FileHandler;
import com.fges.todoapp.util.Todo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class MigrateCommand implements Command {
    private final FileHandler readFileHandler;
    private final FileHandler writeFileHandler;
    private final Path inputFile;
    private final Path outputFile;
    public MigrateCommand(FileHandler readFileHandler, FileHandler writeFileHandler, Path inputFile, Path outputFile) {
        this.readFileHandler = readFileHandler;
        this.writeFileHandler = writeFileHandler;
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }
    @Override
    public int execute(List<String> positionalArgs) throws IOException {
        if (writeFileHandler != null) {
            List<Todo> todos = this.readFileHandler.read(this.inputFile);
            this.writeFileHandler.write(todos, this.outputFile);
            return 0;
        } else {
            System.err.println("The target file is MISSING.");
            return 1;
        }
    }
}
