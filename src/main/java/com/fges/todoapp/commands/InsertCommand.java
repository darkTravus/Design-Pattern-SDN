// com.fges.todoapp.commands.InsertCommand
package com.fges.todoapp.commands;

import com.fges.todoapp.files.FileHandler;
import com.fges.todoapp.todo.Todo;
import com.fges.todoapp.todo.TaskState;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class InsertCommand implements Command {
    private final FileHandler fileHandler;
    private final Path filePath;
    private final TaskState taskState;

    public InsertCommand(FileHandler fileHandler, Path filePath, TaskState taskState) {
        this.fileHandler = fileHandler;
        this.filePath = filePath;
        this.taskState = taskState;
    }

    @Override
    public int execute(List<String> positionalArgs) throws IOException {
        if (positionalArgs.size() < 2) {
            System.err.println("Missing TODO name");
            return 1;
        }

        String task = positionalArgs.get(1);
        List<Todo> todos = new java.util.ArrayList<>(Collections.emptyList());
        Todo todo = new Todo(task, this.taskState);
        todo.setName(task);
        todo.setTaskState(this.taskState);
        todos.add(todo);
        this.fileHandler.write(todos, this.filePath);

        return 0;
    }
}
