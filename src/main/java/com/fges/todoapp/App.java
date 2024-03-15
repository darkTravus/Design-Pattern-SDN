package com.fges.todoapp;

import com.fges.todoapp.cli.CommandLineProcessor;
import com.fges.todoapp.cli.CommandProcessor;

import com.fges.todoapp.commands.local.CommandRegistry;
import com.fges.todoapp.commands.local.InsertCommand;
import com.fges.todoapp.commands.local.ListCommand;
import com.fges.todoapp.commands.local.MigrateCommand;
import com.fges.todoapp.commands.web.WebCommand;
import com.fges.todoapp.files.FileHandler;
import com.fges.todoapp.files.FileHandlerRegistry;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * Hello world!
 */
public class App {
    private static final FileHandlerRegistry fileRegistry = new FileHandlerRegistry();
    private static final CommandRegistry commandRegistry = new CommandRegistry();

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

        CommandLine cmd = CommandLineProcessor.parseCommandLine(args, cliOptions);
        if (cmd == null) return 1;

        CommandProcessor commandProcessor = new CommandProcessor(cmd);
        commandProcessor.processCommand();

        FileHandler fileHandler = fileRegistry.detectFileType(commandProcessor.getFileName());
        Path filePath = Paths.get(commandProcessor.getFileName());
        Path outputPath = commandProcessor.getOutputFile() != null ? Paths.get(commandProcessor.getOutputFile()) : null;
        FileHandler outputFileHandler = outputPath != null ? fileRegistry.detectFileType(commandProcessor.getOutputFile()) : null;

        String command = commandProcessor.getPositionalArgs().get(0);
        commandRegistry.createCommand("insert", new InsertCommand(fileHandler, filePath, commandProcessor.getTaskState()));
        commandRegistry.createCommand("list", new ListCommand(fileHandler, filePath, commandProcessor.getTaskState()));
        commandRegistry.createCommand("migrate", new MigrateCommand(fileHandler, outputFileHandler, filePath, outputPath));
        commandRegistry.createCommand("web", new WebCommand(fileHandler, filePath));
        commandRegistry.executeCommand(command, commandProcessor.getPositionalArgs());

        System.err.println("Done.");
        return 0;
    }
}
