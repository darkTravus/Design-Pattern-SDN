package com.fges.todoapp.commands;

import com.fges.todoapp.commands.local.InsertCommand;
import com.fges.todoapp.commands.local.ListCommand;
import com.fges.todoapp.commands.local.MigrateCommand;
import com.fges.todoapp.commands.web.WebCommand;
import com.fges.todoapp.storage.StorageHandler;
import com.fges.todoapp.storage.files.FileDataAccessor;
import com.fges.todoapp.todo.TaskState;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class CommandFactory {

    private static final Map<String, CommandCreator> commandMap = new HashMap<>();

    static {
        commandMap.put("insert", (fileHandler, filePath, outputFileHandler, outputPath, taskState) -> new InsertCommand(new FileDataAccessor(fileHandler, filePath), taskState));
        commandMap.put("list", (fileHandler, filePath, outputFileHandler, outputPath, taskState) -> new ListCommand(new FileDataAccessor(fileHandler, filePath), taskState));
        commandMap.put("migrate", (fileHandler, filePath, outputFileHandler, outputPath, taskState) -> new MigrateCommand(new FileDataAccessor(fileHandler, filePath), new FileDataAccessor(outputFileHandler, outputPath)));
        commandMap.put("web", (fileHandler, filePath, outputFileHandler, outputPath, taskState) -> new WebCommand(new FileDataAccessor(fileHandler, filePath)));
    }

    public static Command createCommand(String commandName, StorageHandler inputHandler, Path filePath, StorageHandler outputHandler, Path outputPath, TaskState taskState) {
        CommandCreator creator = commandMap.get(commandName);
        if (creator != null) {
            return creator.create(inputHandler, filePath, outputHandler, outputPath, taskState);
        }
        return null;
    }

}
