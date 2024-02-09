// com.fges.todoapp.commands.InsertCommand
package com.fges.todoapp.commands;

import com.fges.todoapp.files.FileHandler;
import com.fges.todoapp.util.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class InsertCommand implements Command {
    private final FileHandler fileHandler;
    private final TodoFactory todoFactory;
    public InsertCommand(FileHandler fileHandler, TodoFactory todoFactory) {
        this.fileHandler = fileHandler;
        this.todoFactory = todoFactory;
    }

    @Override
    public int execute(List<String> positionalArgs, Path filePath, TaskState taskState) throws IOException {
        if (positionalArgs.size() < 2) {
            System.err.println("Missing TODO name");
            return 1;
        }

        String task = positionalArgs.get(1);
        Todo todo = todoFactory.createTodo();
        todo.setName(task);
        todo.setTaskState(taskState);
        fileHandler.write(todo, filePath);

        return 0;
    }
}
