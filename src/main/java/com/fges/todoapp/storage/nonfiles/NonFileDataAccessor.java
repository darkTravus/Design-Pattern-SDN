package com.fges.todoapp.storage.nonfiles;

import com.fges.todoapp.storage.DataAccessor;
import com.fges.todoapp.storage.StorageHandler;
import com.fges.todoapp.todo.Todo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class NonFileDataAccessor implements DataAccessor {
    private final StorageHandler nonFileHandler;

    public NonFileDataAccessor(NonFileHandler nonFileHandler) {
        this.nonFileHandler = nonFileHandler;
    }

    @Override
    public void insert(Todo todo) throws IOException {
        List<Todo> todos = new ArrayList<>();
        todos.add(todo);
        nonFileHandler.write(todos, null);
    }

    @Override
    public List<Todo> read() throws IOException {
        return List.of();
    }
}
