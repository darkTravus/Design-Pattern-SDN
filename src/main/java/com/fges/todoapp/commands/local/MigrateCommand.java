package com.fges.todoapp.commands.local;

import com.fges.todoapp.commands.Command;
import com.fges.todoapp.storage.DataAccessor;
import com.fges.todoapp.todo.Todo;

import java.io.IOException;
import java.util.List;

public class MigrateCommand implements Command {
    private final DataAccessor readFileAccessor;
    private final DataAccessor writeFileAccessor;

    public MigrateCommand(DataAccessor readFileAccessor, DataAccessor writeFileAccessor) {
        this.readFileAccessor = readFileAccessor;
        this.writeFileAccessor = writeFileAccessor;
    }

    @Override
    public int execute(List<String> positionalArgs) throws IOException {
        List<Todo> todos = this.readFileAccessor.read();
        for(Todo todo : todos) {
            this.writeFileAccessor.insert(todo);
        }
        return 0;
    }
}
