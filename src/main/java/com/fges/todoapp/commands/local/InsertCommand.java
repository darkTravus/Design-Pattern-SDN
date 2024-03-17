package com.fges.todoapp.commands.local;

import com.fges.todoapp.commands.Command;
import com.fges.todoapp.todo.Todo;
import com.fges.todoapp.todo.TaskState;
import com.fges.todoapp.storage.DataAccessor;

import java.io.IOException;
import java.util.List;

public class InsertCommand implements Command {
    private final DataAccessor dataAccessor;
    private final TaskState taskState;

    public InsertCommand(DataAccessor dataAccessor, TaskState taskState) {
        this.dataAccessor = dataAccessor;
        this.taskState = taskState;
    }

    @Override
    public int execute(List<String> positionalArgs) throws IOException {
        if (positionalArgs.size() < 2) {
            System.err.println("Missing TODO name");
            return 1;
        }

        String task = positionalArgs.get(1);
        Todo todo = new Todo(task, taskState);
        dataAccessor.insert(todo);

        return 0;
    }
}
