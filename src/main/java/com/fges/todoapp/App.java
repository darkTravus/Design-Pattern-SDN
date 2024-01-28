package com.fges.todoapp;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.MissingNode;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Hello world!
 */
public class App {

    private static final Map<String, Command> commandRegistry = new HashMap<>();

    // Table de correspondence
    static {
        commandRegistry.put("insert", new InsertCommand());
        commandRegistry.put("list", new ListCommand());
    }

    /**
     * Do not change this method
     */
    public static void main(String[] args) throws Exception {
        System.exit(exec(args));
    }

    public static int exec(String[] args) {
        Options cliOptions = new Options();
        cliOptions.addRequiredOption("s", "source", true, "File containing the todos");

        // Analyse des options de ligne de commande
        CommandLine cmd = CommandLineProcessor.parseCommandLine(args, cliOptions);

        if (cmd == null) return 1;

        // Traitement de la commande
        MyCommandProcessor commandProcessor = new MyCommandProcessor();
        int result = commandProcessor.processCommand(cmd);
        String fileName = commandProcessor.getFileName();

        List<String> positionalArgs = commandProcessor.getPositionalArgs();

        if(result != 0) return 1;

        String command = positionalArgs.get(0);
        Path filePath = Paths.get(fileName);
        FileReader.readFileContent(filePath, new PathValidator());

        // Utilisation de la table de correspondence pour déterminer la commande
        Command commandExecutor = createCommandExecutor(command);
        if (commandExecutor != null) {
            commandExecutor.execute(positionalArgs, filePath);
        } else {
            System.err.println("Commande inconnue: " + command);
            return 1;
        }

        System.err.println("Done.");
        return 0;
    }

    interface CommandProcessor {
        int processCommand(CommandLine cmd);
    }

    interface ArgumentValidator {
        boolean validateArguments(CommandLine cmd);
    }

    interface Command {
        int execute(List<String> positionalArgs, Path filePath);
    }

    static class ListCommand implements Command {
        @Override
        public int execute(List<String> positionalArgs, Path filePath) {
            if (filePath.toString().endsWith(".json")) {
                // JSON
                processJsonListCommand(filePath);
            }
            if (filePath.toString().endsWith(".csv")) {
                // CSV
                processCsvListCommand(filePath);
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
    static class InsertCommand implements Command {
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

    private static Command createCommandExecutor(String command) {
        return commandRegistry.get(command);
    }

    static class PathValidator {
        public boolean validatePath(Path filePath) {
            return Files.exists(filePath);
        }
    }

    static class PositionalArgumentValidator implements ArgumentValidator {
        @Override
        public boolean validateArguments(CommandLine cmd) {
            List<String> positionalArgs = cmd.getArgList();
            return !positionalArgs.isEmpty();
        }
    }

    static class FileReader {
        public static String readFileContent(Path filePath, PathValidator pathValidator) {
                try {
                    if (pathValidator.validatePath(filePath)) {
                        return Files.readString(filePath);
                    } else {
                        System.err.println("Le chemin du fichier n'est pas valide");
                        return null;
                    }
                } catch (IOException ex) {
                    System.err.println("Impossible de lire le fichier: " + ex.getMessage());
                    return null;
                }
        }
    }

    static class MyCommandProcessor implements  CommandProcessor {
        private String fileName;
        private List <String> positionalArgs;

        @Override
        public int processCommand(CommandLine cmd){
            this.fileName = cmd.getOptionValue("s");

            PositionalArgumentValidator argumentValidator = new PositionalArgumentValidator();
            if (!argumentValidator.validateArguments(cmd)) {
                System.err.println("Argument manquant");
                return 1;
            }

            this.positionalArgs = cmd.getArgList();
            return 0;
        }
        public String getFileName() {
            return this.fileName;
        }
        public List<String> getPositionalArgs() {
            return positionalArgs;
        }
    }

    static class CommandLineProcessor {
        public static CommandLine parseCommandLine (String[] args, Options options) {
            CommandLineParser parser = new DefaultParser();
            try {
                return parser.parse(options, args);
            } catch (ParseException ex) {
                System.err.println("Fail to parse arguments: " + ex.getMessage());
                return null;
            }
        }
    }
}
