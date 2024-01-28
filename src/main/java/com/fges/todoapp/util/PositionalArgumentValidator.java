package com.fges.todoapp.util;

import org.apache.commons.cli.CommandLine;

import java.util.List;

public class PositionalArgumentValidator implements ArgumentValidator {
    @Override
    public boolean validateArguments(CommandLine cmd) {
        List<String> positionalArgs = cmd.getArgList();
        return !positionalArgs.isEmpty();
    }
}
