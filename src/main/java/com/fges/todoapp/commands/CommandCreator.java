package com.fges.todoapp.commands;

import com.fges.todoapp.storage.StorageHandler;
import com.fges.todoapp.todo.TaskState;

import java.nio.file.Path;

public interface CommandCreator {
    Command create(StorageHandler inputHandler, Path filePath, StorageHandler outputHandler, Path outputPath, TaskState taskState);
}
