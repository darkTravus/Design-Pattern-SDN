package com.fges.todoapp.commands;

import java.util.HashMap;
import java.util.Map;

// Registre des commandes
public class CommandRegistry {
    private Map<String, Command> commandMap = new HashMap<>();

    public void createCommand (String commandName, Command newCommand) {
        commandMap.put(commandName, newCommand);
    }

    public Command getCommand(String commandName) {
        return commandMap.get(commandName);
    }
}

