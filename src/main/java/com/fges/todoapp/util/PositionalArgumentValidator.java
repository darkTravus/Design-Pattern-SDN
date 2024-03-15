package com.fges.todoapp.util;

import org.apache.commons.cli.CommandLine;

import java.util.List;

public class PositionalArgumentValidator implements ArgumentValidator {
    @Override
    public boolean validateArguments(CommandLine cmd, int numberOfArgument) {
        List<String> positionalArgs = cmd.getArgList();
        //System.out.println(positionalArgs)
        if (numberOfArgument == 0) {
            return !positionalArgs.isEmpty();
        } else {
            return (positionalArgs.size() < numberOfArgument);
        }
    }
}
