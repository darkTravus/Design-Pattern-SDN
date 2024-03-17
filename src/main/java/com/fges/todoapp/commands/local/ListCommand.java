package com.fges.todoapp.commands.local;

import com.fges.todoapp.commands.Command;
import com.fges.todoapp.storage.DataAccessor;
import com.fges.todoapp.todo.TaskState;
import com.fges.todoapp.todo.Todo;

import java.io.IOException;
import java.util.List;

public class ListCommand implements Command {
    private final DataAccessor dataAccessor;
    private final TaskState taskState;

    public ListCommand(DataAccessor dataAccessor, TaskState taskState) {
        this.dataAccessor = dataAccessor;
        this.taskState = taskState;
    }

    @Override
    public int execute(List<String> positionalArgs) throws IOException {
        List<Todo> todoList = this.dataAccessor.read();
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