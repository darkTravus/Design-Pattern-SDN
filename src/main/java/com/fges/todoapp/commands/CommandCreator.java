package com.fges.todoapp.commands;

import com.fges.todoapp.files.FileHandler;
import com.fges.todoapp.todo.TaskState;

import java.nio.file.Path;

public interface CommandCreator {
    Command create(FileHandler fileHandler, Path filePath, FileHandler outputFileHandler, Path outputPath, TaskState taskState);
}
