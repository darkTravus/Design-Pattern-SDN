// com.fges.todoapp.commands.local.ListCommand
package com.fges.todoapp.commands.local;

import com.fges.todoapp.commands.Command;
import com.fges.todoapp.files.FileHandler;
import com.fges.todoapp.todo.TaskState;
import com.fges.todoapp.todo.Todo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class ListCommand implements Command {
    private final FileHandler fileHandler;
    private final Path filePath;
    private final TaskState taskState;

    public ListCommand(FileHandler fileHandler, Path filePath, TaskState taskState) {
        this.fileHandler = fileHandler;
        this.filePath = filePath;
        this.taskState = taskState;
    }

    @Override
    public int execute(List<String> positionalArg) throws IOException {
        List<Todo> todoList = this.fileHandler.read(filePath);
        printTodoList(todoList, this.taskState);
        return 0;
    }

    private void printTodoList(List<Todo> todoList, TaskState taskState) {
        //System.out.println("Todos:");

        for (Todo todo : todoList) {
            if (taskState == TaskState.DONE && todo.getTaskState() == TaskState.DONE) {
                System.out.println("- Done: " + todo.getName());
            } else if (taskState != TaskState.DONE) {
                if (todo.getTaskState() == TaskState.DONE) {
                    System.out.println("- Done: " + todo.getName());
                } else {
                    System.out.println("- " + todo.getName());
                }
            }
        }
    }
}