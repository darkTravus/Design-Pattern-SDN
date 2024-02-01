// com.fges.todoapp.App
package com.fges.todoapp;

import com.fges.todoapp.commands.*;

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

    private static Command createCommandExecutor(String command) {
        return commandRegistry.get(command);
    }

    /**
     * Do not change this method
     */
    public static void main(String[] args) throws Exception {
        System.exit(exec(args));
    }

    public static int exec(String[] args) throws IOException {
        Options cliOptions = new Options();
        cliOptions.addRequiredOption("s", "source", true, "File containing the todos");
        cliOptions.addOption("d", "done", false, "Mark a todo as done");

        // Analyse des options de ligne de commande
        CommandLine cmd = CommandLineProcessor.parseCommandLine(args, cliOptions);

        if (cmd == null) return 1;

        // Traitement de la commande
        MyCommandProcessor commandProcessor = new MyCommandProcessor();
        int result = commandProcessor.processCommand(cmd);
        String fileName = commandProcessor.getFileName();
        boolean isDone = cmd.hasOption("done");
        List<String> positionalArgs = commandProcessor.getPositionalArgs();

        if(result != 0) return 1;

        String command = positionalArgs.get(0);
        Path filePath = Paths.get(fileName);

        // Utilisation de la table de correspondence pour déterminer la commande
        Command commandExecutor = createCommandExecutor(command);
        if (commandExecutor != null) {
            commandExecutor.execute(positionalArgs, filePath, isDone);
        } else {
            System.err.println("Commande inconnue: " + command);
            return 1;
        }

        System.err.println("Done.");
        return 0;
    }

}
