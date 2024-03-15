package com.fges.todoapp.util;

import org.apache.commons.cli.CommandLine;

public interface ArgumentValidator {
    boolean validateArguments(CommandLine cmd, int numberOfArgument);
}
