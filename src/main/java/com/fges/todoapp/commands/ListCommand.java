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
import java.util.stream.Collectors;

public class ListCommand implements Command {
    @Override
    public int execute(List<String> positionalArgs, Path filePath) throws IOException {
        if (filePath.toString().endsWith(".json")) {
            // JSON
            processJsonListCommand(filePath);
        }
        else if (filePath.toString().endsWith(".csv")) {
            // CSV
            processCsvListCommand(filePath);
        }  else {
            System.err.println("Unsupported file type");
            return 1;
        }

        return 0;
    }
    private void processJsonListCommand(Path filePath) {
        try {
            String fileContent = Files.readString(filePath);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(fileContent);
            if (actualObj instanceof MissingNode) {
                // Node was not recognised
                actualObj = JsonNodeFactory.instance.arrayNode();
            }

            if (actualObj instanceof ArrayNode arrayNode) {
                arrayNode.forEach(node -> System.out.println("- " + node.toString()));
            }
        } catch (IOException e) {
            System.err.println("Error processing JSON list: " + e.getMessage());
        }
    }

    private void processCsvListCommand(Path filePath) {
        try {
            String fileContent = Files.readString(filePath);
            System.out.println(Arrays.stream(fileContent.split("\n"))
                    .map(todo -> "- " + todo)
                    .collect(Collectors.joining("\n"))
            );
        } catch (IOException e) {
            System.err.println("Error processing CSV list: " + e.getMessage());
        }
    }

}