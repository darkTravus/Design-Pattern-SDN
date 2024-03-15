package com.fges.todoapp.cli;

import com.fges.todoapp.cli.options.OptionHandler;
import com.fges.todoapp.cli.options.OutputOptionHandler;
import com.fges.todoapp.cli.options.SourceOptionHandler;
import com.fges.todoapp.cli.options.StatusOptionHandler;
import com.fges.todoapp.todo.TaskState;
import com.fges.todoapp.util.PositionalArgumentValidator;
import org.apache.commons.cli.CommandLine;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandProcessor {
    private final Map<String, OptionHandler> optionHandlers = new HashMap<>();
    private final CommandLine cmd;

    public CommandProcessor(CommandLine cmd) {
        this.cmd = cmd;
        initializeOptionHandlers();
    }

    private void initializeOptionHandlers() {
        optionHandlers.put("s", new SourceOptionHandler());
        optionHandlers.put("done", new StatusOptionHandler());
        optionHandlers.put("output", new OutputOptionHandler());
    }

    private SourceOptionHandler getSourceOptionHandler() {
        return (SourceOptionHandler) optionHandlers.get("s");
    }

    private StatusOptionHandler getStatusOptionHandler() {
        return (StatusOptionHandler) optionHandlers.get("done");
    }

    private OutputOptionHandler getOutputOptionHandler() {
        return (OutputOptionHandler) optionHandlers.get("output");
    }

    public int processCommand() {
        for (Map.Entry<String, OptionHandler> entry : optionHandlers.entrySet()) {
            String option = entry.getKey();
            OptionHandler handler = entry.getValue();
            if (cmd.hasOption(option)) {
                handler.handleOption(cmd.getOptionValue(option));
            }
        }

        // Traitement des arguments positionnels
        PositionalArgumentValidator argumentValidator = new PositionalArgumentValidator();
        if (!argumentValidator.validateArguments(cmd, 0)) {
            System.err.println("Argument manquant");
            return 1;
        }

        return 0;
    }

    public String getCommand() {
        return cmd.getArgList().get(0);
    }

    public  String getTodoContent() {
        return cmd.getArgList().get(1);
    }

    public String getFileName() {
        return getSourceOptionHandler().getFileName();
    }

    public List<String> getPositionalArgs() {
        // Obtenir les arguments positionnels
        return cmd.getArgList();
    }

    public TaskState getTaskState() {
        return getStatusOptionHandler().getTaskState();
    }

    public String getOutputFile() {
        return getOutputOptionHandler().getOutputFile();
    }
}
