package com.fges.todoapp.todo;

import com.fges.todoapp.dto.TodoDTO;
import com.fges.todoapp.files.FileHandler;
import fr.anthonyquere.dumbcrud.CrudProvider;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TodoProvider implements CrudProvider<TodoDTO> {
    private final FileHandler fileHandler;
    private final Path filePath;

    public TodoProvider(FileHandler fileHandler, Path filePath) {
        this.fileHandler = fileHandler;
        this.filePath = filePath;
    }

    @Override
    public void add(TodoDTO todoDTO) throws Exception {
        Todo todo = new Todo(todoDTO.getContent(), todoDTO.getStatus());
        List<Todo> todos = new ArrayList<>();
        todos.add(todo);
        this.fileHandler.write(todos, this.filePath);
        System.err.println("Todo added to " + this.filePath + " from the server");
    }

    @Override
    public List<TodoDTO> list() throws Exception {
        List<Todo> todos = this.fileHandler.read(this.filePath);
        List<TodoDTO> todoDTOs = new ArrayList<>();
        for (Todo todo : todos) {
            todoDTOs.add(new TodoDTO(todo.getName(), todo.getStatus()));
        }
        System.err.println("Todo listed from " + this.filePath + " to the server");
        return todoDTOs;
    }
}
