// com.fges.todoapp.commands.InsertCommand
package com.fges.todoapp.commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.MissingNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class InsertCommand implements Command {
    @Override
    public int execute(List<String> positionalArgs, Path filePath) {
        if (positionalArgs.size() < 2) {
            System.err.println("Missing TODO name");
            return 1;
        }
        String todo = positionalArgs.get(1);

        if (filePath.toString().endsWith(".json")) {
            // JSON
            processJsonInsertCommand(filePath, todo);
        } else if (filePath.toString().endsWith(".csv")) {
            // CSV
            processCsvInsertCommand(filePath, todo);
        } else {
            System.err.println("Unsupported file type");
            return 1;
        }

        return 0;
    }

    private void processJsonInsertCommand(Path filePath, String todo) {
        try {
            String fileContent = Files.readString(filePath);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(fileContent);
            if (actualObj instanceof MissingNode) {
                // Node was not recognised
                actualObj = JsonNodeFactory.instance.arrayNode();
            }

            if (actualObj instanceof ArrayNode arrayNode) {
                arrayNode.add(todo);
            }

            Files.writeString(filePath, actualObj.toString());
        } catch (IOException e) {
            System.err.println("Error processing JSON insert: " + e.getMessage());
        }
    }

    private void processCsvInsertCommand(Path filePath, String todo) {
        try {
            String fileContent = Files.readString(filePath);
            if (!fileContent.endsWith("\n") && !fileContent.isEmpty()) {
                fileContent += "\n";
            }
            fileContent += todo;

            Files.writeString(filePath, fileContent);
        } catch (IOException e) {
            System.err.println("Error processing CSV insert: " + e.getMessage());
        }
    }
}
