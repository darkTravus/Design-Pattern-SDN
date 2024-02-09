// com.fges.todoapp.App
package com.fges.todoapp;

import com.fges.todoapp.commands.*;

import com.fges.todoapp.files.FileHandler;
import com.fges.todoapp.files.FileHandlerFactory;
import com.fges.todoapp.files.FileHandlerRegistry;
import com.fges.todoapp.files.csv.CsvFileHandlerFactory;
import com.fges.todoapp.files.json.JsonFileHandlerFactory;
import com.fges.todoapp.util.TaskState;
import com.fges.todoapp.util.TodoFactory;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.apache.commons.io.FilenameUtils;


/**
 * Hello world!
 */
public class App {
    // Initialisation du registre de fichier
    private static final FileHandlerRegistry registry = new FileHandlerRegistry();
    // Déclaration du registre de commande
    private static final CommandRegistry commandRegistry = new CommandRegistry();

    static {
        registry.registerFileFactory("csv", new CsvFileHandlerFactory());
        registry.registerFileFactory("json", new JsonFileHandlerFactory());
    }


    private static FileHandler detectFileType (String filename) {
        String fileExtension = FilenameUtils.getExtension(filename).toLowerCase();
        FileHandlerFactory fileHandlerFactory = registry.getFileFactory(fileExtension);
        if (fileHandlerFactory == null) {
            System.err.println("Extension de fichier non prise en charge : " + fileExtension);
            return null;
        }
        return fileHandlerFactory.createFileHandler();
    }

    private static void executeCommand(String command, List<String> positionalArgs, Path filePath, TaskState taskState)
            throws IOException {
        Command commandExecutor = commandRegistry.getCommand(command);
        if (commandExecutor != null) {
            commandExecutor.execute(positionalArgs, filePath, taskState);
        } else {
            System.err.println("Commande inconnue: " + command);
        }
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
        TaskState taskState = cmd.hasOption("done") ? TaskState.DONE : TaskState.NOT_DONE;
        List<String> positionalArgs = commandProcessor.getPositionalArgs();

        if(result != 0) return 1;

        String command = positionalArgs.get(0);
        Path filePath = Paths.get(fileName);

        // Utilisation du registre des factories pour créer le FileHandler approprié
        FileHandler fileHandler = detectFileType(fileName);

        // Initialisation du registre de commande
        commandRegistry.createCommand("insert", new InsertCommand(fileHandler, new TodoFactory()));
        commandRegistry.createCommand("list", new ListCommand(fileHandler));

        // Utilisation de la table de correspondence pour déterminer la commande
        executeCommand(command, positionalArgs, filePath, taskState);

        System.err.println("Done.");
        return 0;
    }

}
