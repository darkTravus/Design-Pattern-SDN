package com.fges.todoapp.commands.local;

import com.fges.todoapp.commands.Command;
import com.fges.todoapp.commands.CommandCreator;
import com.fges.todoapp.commands.web.WebCommand;
import com.fges.todoapp.files.FileHandler;
import com.fges.todoapp.todo.TaskState;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class CommandFactory {

    private static final Map<String, CommandCreator> commandMap = new HashMap<>();

    static {
        commandMap.put("insert", (fileHandler, filePath, outputFileHandler, outputPath, taskState) -> new InsertCommand(fileHandler, filePath, taskState));
        commandMap.put("list", (fileHandler, filePath, outputFileHandler, outputPath, taskState) -> new ListCommand(fileHandler, filePath, taskState));
        commandMap.put("migrate", (fileHandler, filePath, outputFileHandler, outputPath, taskState) -> new MigrateCommand(fileHandler, outputFileHandler, filePath, outputPath));
        commandMap.put("web", (fileHandler, filePath, outputFileHandler, outputPath, taskState) -> new WebCommand(fileHandler, filePath));
    }

    public static Command createCommand(String commandName, FileHandler fileHandler, Path filePath, FileHandler outputFileHandler, Path outputPath, TaskState taskState) {
        CommandCreator creator = commandMap.get(commandName);
        if (creator != null) {
            return creator.create(fileHandler, filePath, outputFileHandler, outputPath, taskState);
        }
        return null;
    }

}
