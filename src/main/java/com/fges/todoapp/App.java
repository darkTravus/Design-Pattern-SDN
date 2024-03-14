// com.fges.todoapp.App
package com.fges.todoapp;

import com.fges.todoapp.commands.*;

import com.fges.todoapp.files.FileHandler;
import com.fges.todoapp.files.FileHandlerRegistry;
import com.fges.todoapp.files.csv.CsvFileHandler;
import com.fges.todoapp.files.json.JsonFileHandler;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


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
    // Retourne le FileHandler en fonction de l'extension du fichier
    private static FileHandler detectFileType (String filename) {
        String fileExtension = getFileExtension(filename);
        FileHandler fileHandler = registry.getFileHandler(fileExtension);
        if (fileHandler == null) {
            System.err.println("Extension de fichier non prise en charge : " + fileExtension);
            return null;
        }
        return fileHandler;
    }
    // Exécute une commande
    private static void executeCommand(String command, List<String> positionalArgs) throws IOException {
        Command commandExecutor = commandRegistry.getCommand(command);
        if (commandExecutor != null) {
            commandExecutor.execute(positionalArgs);
        } else {
            System.err.println("Commande inconnue: " + command);
        }
    }
    // Récupérer l'extension d'un fichier
    private static String getFileExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        return filename.substring(lastDotIndex + 1).toLowerCase();
    }

    /**
     * Do not change this method
     */
    public static void main(String[] args) throws Exception {
        System.exit(exec(args));
    }

    public static int exec(String[] args) throws IOException {

        Options cliOptions = new Options();
        // Ajout des options
        cliOptions.addRequiredOption("s", "source", true, "File containing the todos");
        cliOptions.addOption("d", "done", false, "Mark a todo as done");
        cliOptions.addOption("o", "output", true, "The destination file");

        // Analyse des options de ligne de commande
        CommandLine cmd = CommandLineProcessor.parseCommandLine(args, cliOptions);

        if (cmd == null) return 1;

        // Traitement de la commande
        MyCommandProcessor commandProcessor = new MyCommandProcessor();
        commandProcessor.processCommand(cmd);

        // Récupération de la commande
        List<String> positionalArgs = commandProcessor.getPositionalArgs();
        String command = positionalArgs.get(0);

        // Définition des différents chemins
        Path filePath = Paths.get(commandProcessor.getFileName());
        Path outputPath = null;

        // Utilisation du registre pour créer le FileHandler approprié
        FileHandler fileHandler = detectFileType(commandProcessor.getFileName());
        FileHandler outputFileHandler = null;

        // Vérification de l'option "--output"
        if (commandProcessor.getOutputFile() != null) {
            outputPath = Paths.get(commandProcessor.getOutputFile());
            outputFileHandler = detectFileType(commandProcessor.getOutputFile());
        }

        // Initialisation du registre de commande
        commandRegistry.createCommand(
                "insert",
                new InsertCommand(fileHandler, filePath, commandProcessor.getTaskState())
        );
        commandRegistry.createCommand(
                "list",
                new ListCommand(fileHandler, filePath, commandProcessor.getTaskState())
        );
        commandRegistry.createCommand(
                "migrate",
                new MigrateCommand(fileHandler, outputFileHandler, filePath, outputPath)
        );
        commandRegistry.createCommand(
                "web",
                new WebCommand(fileHandler, filePath)
        );

        // Utilisation de la table de correspondence pour déterminer la commande à exécuter
        executeCommand(command, positionalArgs);

        System.err.println("Done.");
        return 0;
    }

}
