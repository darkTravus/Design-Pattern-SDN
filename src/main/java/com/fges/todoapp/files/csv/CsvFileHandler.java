package com.fges.todoapp.files.csv;

import com.fges.todoapp.files.FileHandler;
import com.fges.todoapp.util.TaskState;
import com.fges.todoapp.util.Todo;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CsvFileHandler implements FileHandler {
    @Override
    public void write(List <Todo> todos, Path filePath) throws IOException {
        for (Todo todo : todos) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile(), true))) {
                String taskState = todo.getTaskState() == TaskState.DONE ? "Done" : "Not Done";
                writer.write(todo.getName() + ";" + taskState + "\n");
            } catch (IOException e) {
                System.err.println("Error processing CSV insert: " + e.getMessage());
            }
        }
    }
    @Override
    public List<Todo> read(Path filePath) {
        List<Todo> todoList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(";");
                if (fields.length >= 2) {
                    String task = fields[0].trim();
                    boolean done = fields[1].trim().equalsIgnoreCase("Done");
                    todoList.add(new Todo(task, done));
                }
            }
            return todoList;
        } catch (IOException e) {
            System.err.println("Error processing CSV list: " + e.getMessage());
            return Collections.emptyList();
        }

    }
}
