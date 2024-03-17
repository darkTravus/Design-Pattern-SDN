package com.fges.todoapp.storage.nonfiles;

import com.fges.todoapp.todo.Todo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DatabaseFileHandler implements NonFileHandler {
    // Méthode pour lire les données depuis la base de données
    @Override
    public List<Todo> read(Path filePath) {
        // Logique pour lire les données depuis la base de données
        // Exemple : SELECT * FROM todos;
        return new ArrayList<>();
    }

    // Méthode pour écrire les données dans la base de données
    @Override
    public void write(List<Todo> todos, Path filePath) throws IOException {
        // Logique pour écrire les données dans la base de données
        // Exemple : INSERT INTO todos (name, status) VALUES (?, ?);
    }
}

