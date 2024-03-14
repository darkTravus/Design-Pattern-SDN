package com.fges.todoapp.todo;

import com.fges.todoapp.files.FileHandler;
import fr.anthonyquere.dumbcrud.CrudProvider;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TodoProvider implements CrudProvider<Todo> {
    private final FileHandler fileHandler;
    private final Path filePath;

    public TodoProvider(FileHandler fileHandler, Path filePath) {
        this.fileHandler = fileHandler;
        this.filePath = filePath;
    }

    @Override
    public void add(Todo todo) throws Exception {
        List<Todo> todos = new ArrayList<>();
        todos.add(todo);
        this.fileHandler.write(todos, this.filePath);
        System.err.println("Todo added to " + this.filePath + " from the server");
    }

    @Override
    public List<Todo> list() throws Exception {
        System.err.println("Todo listed to " + this.filePath + " from the server");
        return this.fileHandler.read(this.filePath);
    }
}
