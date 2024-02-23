package com.fges.todoapp.files;

import com.fges.todoapp.util.Todo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface FileHandler {
    void write(List <Todo> todos, Path filePath) throws IOException;
    List<Todo> read(Path filePath);
}
