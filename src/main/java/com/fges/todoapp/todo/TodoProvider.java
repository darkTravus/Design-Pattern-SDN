package com.fges.todoapp.todo;

import com.fges.todoapp.dto.TodoDTO;
import com.fges.todoapp.storage.DataAccessor;
import fr.anthonyquere.dumbcrud.CrudProvider;

import java.util.ArrayList;
import java.util.List;

public class TodoProvider implements CrudProvider<TodoDTO> {
    private final DataAccessor dataAccessor;

    public TodoProvider(DataAccessor dataAccessor) {
        this.dataAccessor = dataAccessor;
    }

    @Override
    public void add(TodoDTO todoDTO) throws Exception {
        Todo todo = new Todo(todoDTO.getContent(), todoDTO.getStatus());
        this.dataAccessor.insert(todo);
        System.err.println("Todo added from the server");
    }

    @Override
    public List<TodoDTO> list() throws Exception {
        List<Todo> todos = this.dataAccessor.read();
        List<TodoDTO> todoDTOs = new ArrayList<>();
        for (Todo todo : todos) {
            todoDTOs.add(new TodoDTO(todo.getName(), todo.getStatus()));
        }
        System.err.println("Todo listed to the server");
        return todoDTOs;
    }
}
