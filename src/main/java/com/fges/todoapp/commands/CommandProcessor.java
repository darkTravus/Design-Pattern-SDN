package com.fges.todoapp.commands;

import org.apache.commons.cli.CommandLine;

public interface CommandProcessor {
    int processCommand(CommandLine cmd);
}
