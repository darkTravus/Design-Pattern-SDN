package com.fges.todoapp.storage.files;

import com.fges.todoapp.storage.DataAccessor;
import com.fges.todoapp.storage.StorageHandler;
import com.fges.todoapp.todo.Todo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileDataAccessor implements DataAccessor {
    private final StorageHandler fileHandler;
    private final Path filePath;

    public FileDataAccessor(StorageHandler fileHandler, Path filePath) {
        this.fileHandler = fileHandler;
        this.filePath = filePath;
    }

    @Override
    public void insert(Todo todo) throws IOException {
        List<Todo> todos = new ArrayList<>(Collections.emptyList());
        todos.add(todo);
        this.fileHandler.write(todos, this.filePath);
    }

    @Override
    public List<Todo> read() throws IOException {
        return this.fileHandler.read(this.filePath);
    }
}
