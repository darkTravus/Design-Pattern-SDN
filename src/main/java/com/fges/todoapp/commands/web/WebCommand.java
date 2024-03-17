package com.fges.todoapp.commands.web;

import com.fges.todoapp.commands.Command;
import com.fges.todoapp.storage.DataAccessor;
import com.fges.todoapp.todo.TodoProvider;
import com.fges.todoapp.util.ServerRunner;

import java.io.IOException;
import java.util.List;

public class WebCommand implements Command {
    private final TodoProvider provider;

    public WebCommand(DataAccessor dataAccessor) {
        this.provider = new TodoProvider(dataAccessor);
    }

    @Override
    public int execute(List<String> positionalArgs) throws IOException {
        new ServerRunner(8888, "todo").startServer(this.provider);
        return 0;
    }
}
