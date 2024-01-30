package com.fges.todoapp.commands;

import com.fges.todoapp.util.PositionalArgumentValidator;
import org.apache.commons.cli.CommandLine;

import java.util.List;

public class MyCommandProcessor implements CommandProcessor {
    private String fileName;
    private List<String> positionalArgs;

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