package com.fges.todoapp.storage;

import com.fges.todoapp.todo.Todo;

import java.io.IOException;
import java.util.List;

public interface DataAccessor {
    void insert(Todo todo) throws IOException;
    List<Todo> read() throws IOException;
}
