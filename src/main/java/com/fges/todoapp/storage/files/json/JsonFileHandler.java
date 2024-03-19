package com.fges.todoapp.storage.files.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fges.todoapp.storage.StorageHandler;
import com.fges.todoapp.storage.files.FileReader;
import com.fges.todoapp.util.PathValidator;
import com.fges.todoapp.todo.TaskState;
import com.fges.todoapp.todo.Todo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JsonFileHandler implements StorageHandler {
    @Override
    public void write(List <Todo> todos, Path filePath) throws IOException {
        String fileContent = FileReader.readFileContent(filePath, new PathValidator());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(fileContent);
        if (actualObj instanceof MissingNode) {
            // Node was not recognised
            actualObj = JsonNodeFactory.instance.arrayNode();
        }

        if (actualObj instanceof ArrayNode arrayNode) {
            for (Todo todo : todos) {
                if (todo.getTaskState() == TaskState.DONE) {
                    // Nouvelle structure avec la propriété "done"
                    arrayNode.add(JsonNodeFactory.instance.objectNode()
                            .put("text", todo.getName())
                            .put("done", true)
                    );
                } else {
                    // Structure retro compatible sans la propriété "done"
                    arrayNode.add(todo.getName());
                }
            }
        }

        Files.writeString(filePath, actualObj.toString());
    }

    @Override
    public List<Todo> read(Path filePath) {
        try {
            String fileContent = Files.readString(filePath);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(fileContent);

            List<Todo> todoList = new ArrayList<>();

            if (actualObj instanceof MissingNode) {
                // Node was not recognised
                actualObj = JsonNodeFactory.instance.arrayNode();
            }

            if (actualObj instanceof ArrayNode arrayNode) {
                arrayNode.forEach(node -> {
                    JsonNode todoNode = node.get("text");
                    JsonNode doneNode = node.get("done");

                    if (todoNode != null) {
                        String task = todoNode.asText();
                        boolean done = doneNode != null && doneNode.asBoolean();
                        TaskState state = done ? TaskState.DONE : TaskState.NOT_DONE;
                        Todo todo = new Todo(task, state);
                        todoList.add(todo);
                    } else {
                        String task = node.asText();
                        Todo todo = new Todo(task, TaskState.NOT_DONE);
                        todoList.add(todo);
                    }
                });
            }

            return todoList;
        } catch (IOException e) {
            System.err.println("Error processing JSON list: " + e.getMessage());
            return Collections.emptyList();
        }

    }
}
