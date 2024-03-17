// com.fges.todoapp.App.java
package com.fges.todoapp;

import com.fges.todoapp.cli.CommandLineProcessor;
import com.fges.todoapp.cli.CommandProcessor;
import com.fges.todoapp.commands.Command;
import com.fges.todoapp.commands.local.CommandFactory;
import com.fges.todoapp.files.FileHandler;
import com.fges.todoapp.files.FileHandlerRegistry;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class App {
    private static final FileHandlerRegistry fileRegistry = new FileHandlerRegistry();

    public static void main(String[] args) throws Exception {
        System.exit(exec(args));
    }

    public static int exec(String[] args) throws IOException {
        Options cliOptions = buildOptions();
        CommandLine cmd = CommandLineProcessor.parseCommandLine(args, cliOptions);
        if (cmd == null) return 1;

        CommandProcessor commandProcessor = new CommandProcessor(cmd);
        commandProcessor.processCommand();

        FileHandler fileHandler = fileRegistry.detectFileType(commandProcessor.getFileName());
        Path filePath = Paths.get(commandProcessor.getFileName());
        Path outputPath = commandProcessor.getOutputFile() != null ? Paths.get(commandProcessor.getOutputFile()) : null;
        FileHandler outputFileHandler = outputPath != null ? fileRegistry.detectFileType(commandProcessor.getOutputFile()) : null;

        String commandName = commandProcessor.getPositionalArgs().get(0);
        Command command = CommandFactory.createCommand(commandName, fileHandler, filePath, outputFileHandler, outputPath, commandProcessor.getTaskState());
        assert command != null;
        command.execute(commandProcessor.getPositionalArgs());

        System.err.println("Done.");
        return 0;
    }

    private static Options buildOptions() {
        Options cliOptions = new Options();
        cliOptions.addRequiredOption("s", "source", true, "File containing the todos");
        cliOptions.addOption("d", "done", false, "Mark a todo as done");
        cliOptions.addOption("o", "output", true, "The destination file");
        return cliOptions;
    }
}
