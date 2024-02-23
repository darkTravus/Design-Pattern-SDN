// com.fges.todoapp.App
package com.fges.todoapp;

import com.fges.todoapp.commands.*;

import com.fges.todoapp.files.FileHandler;
import com.fges.todoapp.files.FileHandlerRegistry;
import com.fges.todoapp.files.csv.CsvFileHandler;
import com.fges.todoapp.files.json.JsonFileHandler;
import com.fges.todoapp.util.TaskState;
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
        registry.registerFileFactory("csv", new CsvFileHandler());
        registry.registerFileFactory("json", new JsonFileHandler());
    }


    private static FileHandler detectFileType (String filename) {
        String fileExtension = FilenameUtils.getExtension(filename).toLowerCase();
        FileHandler fileHandler = registry.getFileHandler(fileExtension);
        if (fileHandler == null) {
            System.err.println("Extension de fichier non prise en charge : " + fileExtension);
            return null;
        }
        return fileHandler;
    }

    private static void executeCommand(String command, List<String> positionalArgs) throws IOException {
        Command commandExecutor = commandRegistry.getCommand(command);
        if (commandExecutor != null) {
            commandExecutor.execute(positionalArgs);
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
        cliOptions.addOption("o", "output", true, "The destination file");

        // Analyse des options de ligne de commande
        CommandLine cmd = CommandLineProcessor.parseCommandLine(args, cliOptions);

        if (cmd == null) return 1;

        // Traitement de la commande
        MyCommandProcessor commandProcessor = new MyCommandProcessor();
        int result = commandProcessor.processCommand(cmd);
        String fileName = commandProcessor.getFileName();
        TaskState taskState = commandProcessor.getTaskState();
        String outputFile = commandProcessor.getOutputFile();
        List<String> positionalArgs = commandProcessor.getPositionalArgs();

        if(result != 0) return 1;

        String command = positionalArgs.get(0);
        Path filePath = Paths.get(fileName);
        Path outputPath = null;

        // Utilisation du registre des factories pour créer le FileHandler approprié
        FileHandler fileHandler = detectFileType(fileName);
        FileHandler outputFileHandler = null;

        if (outputFile != null) {
            outputPath = Paths.get(outputFile);
            outputFileHandler = detectFileType(outputFile);
        }

        // Initialisation du registre de commande
        commandRegistry.createCommand("insert", new InsertCommand(fileHandler, filePath, taskState));
        commandRegistry.createCommand("list", new ListCommand(fileHandler, filePath, taskState));
        commandRegistry.createCommand(
                "migrate",
                new MigrateCommand(fileHandler, outputFileHandler, filePath, outputPath)
        );

        // Utilisation de la table de correspondence pour déterminer la commande
        executeCommand(command, positionalArgs);

        System.err.println("Done.");
        return 0;
    }

}
