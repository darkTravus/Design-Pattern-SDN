// com.fges.todoapp.App
package com.fges.todoapp;

import com.fges.todoapp.commands.*;
import com.fges.todoapp.util.*;
import com.fges.todoapp.util.PositionalArgumentValidator;

import org.apache.commons.cli.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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


    private static Command createCommandExecutor(String command) {
        return commandRegistry.get(command);
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
