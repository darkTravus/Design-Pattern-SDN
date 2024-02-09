// com.fges.todoapp.commands.ListCommand
package com.fges.todoapp.commands;

import com.fges.todoapp.files.FileHandler;
import com.fges.todoapp.util.TaskState;
import com.fges.todoapp.util.Todo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class ListCommand implements Command {
    private final FileHandler fileHandler;

    public ListCommand(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    @Override
    public int execute(List<String> positionalArg, Path filePath, TaskState taskState) throws IOException {
        List<Todo> todoList = fileHandler.read(filePath);
        printTodoList(todoList, taskState);
        return 0;
    }

    private void printTodoList(List<Todo> todoList, TaskState taskState) {
        //System.out.println("Todos:");

        for (Todo todo : todoList) {
            if (taskState == TaskState.DONE && todo.getTaskState() == TaskState.DONE) {
                System.out.println("- Done: " + todo.getName());
            } else if (taskState != TaskState.DONE) {
                System.out.println("- " + todo.getName());
            }
        }
    }
}