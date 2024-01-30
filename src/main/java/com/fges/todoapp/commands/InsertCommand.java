// com.fges.todoapp.commands.InsertCommand
package com.fges.todoapp.commands;

import com.fges.todoapp.util.PathValidator;
import com.fges.todoapp.util.FileReader;

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
    public int execute(List<String> positionalArgs, Path filePath) throws IOException {
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

    private void processJsonInsertCommand(Path filePath, String todo) throws IOException {
        String fileContent = FileReader.readFileContent(filePath, new PathValidator());
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
    }

    private void processCsvInsertCommand(Path filePath, String todo) throws IOException {
        String fileContent = FileReader.readFileContent(filePath, new PathValidator());
            if (!fileContent.endsWith("\n") && !fileContent.isEmpty()) {
                fileContent += "\n";
            }
            fileContent += todo;

            Files.writeString(filePath, fileContent);
    }
}
