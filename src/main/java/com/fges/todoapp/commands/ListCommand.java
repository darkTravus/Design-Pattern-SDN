// com.fges.todoapp.commands.ListCommand
package com.fges.todoapp.commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.MissingNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class ListCommand implements Command {
    @Override
    public int execute(List<String> positionalArgs, Path filePath, boolean markDone) throws IOException {
        if (filePath.toString().endsWith(".json")) {
            // JSON
            processJsonListCommand(filePath, markDone);
        }
        else if (filePath.toString().endsWith(".csv")) {
            // CSV
            processCsvListCommand(filePath, markDone);
        }  else {
            System.err.println("Unsupported file type");
            return 1;
        }

        return 0;
    }
    private void processJsonListCommand(Path filePath, boolean markDone) {
        try {
            String fileContent = Files.readString(filePath);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(fileContent);

            if (actualObj instanceof MissingNode) {
                // Node was not recognised
                actualObj = JsonNodeFactory.instance.arrayNode();
            }

            if (actualObj instanceof ArrayNode arrayNode) {
                if (markDone) {
                    arrayNode.forEach(node -> {
                        JsonNode todoNode = node.get("text");
                        JsonNode doneNode = node.get("done");
                        if (todoNode != null && doneNode != null && doneNode.asBoolean()) {
                            System.out.println("- Done: " + todoNode.asText());
                        }
                    });
                } else {
                    arrayNode.forEach(node -> {
                        JsonNode todoNode = node.get("text");

                        if (todoNode == null) {
                            System.out.println("- " + node.asText());
                        } else {
                            JsonNode doneNode = node.get("done");
                            if (doneNode != null && doneNode.asBoolean()) {
                                System.out.println("- Done: " + todoNode.asText());
                            }
                        }
                    });
                }
            }
        } catch (IOException e) {
            System.err.println("Error processing JSON list: " + e.getMessage());
        }
    }

    private void processCsvListCommand(Path filePath, boolean markDone) {
        try {
            String fileContent = Files.readString(filePath);
            Arrays.stream(fileContent.split("\n"))
                    .filter(todo -> !markDone || todo.contains("Done"))
                    .map(todo -> markDone ? todo : "- " + todo)
                    .forEach(System.out::println);

        } catch (IOException e) {
            System.err.println("Error processing CSV list: " + e.getMessage());
        }
    }

}