package com.fges.todoapp.commands.local;

import com.fges.todoapp.commands.Command;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Registre des commandes
public class CommandRegistry {
    private Map<String, Command> commandMap = new HashMap<>();

    public void createCommand (String commandName, Command newCommand) {
        commandMap.put(commandName, newCommand);
    }

    public void executeCommand(String command, List<String> positionalArgs) throws IOException {
        Command commandExecutor = commandMap.get(command);
        if (commandExecutor != null) {
            commandExecutor.execute(positionalArgs);
        } else {
            System.err.println("Commande inconnue: " + command);
        }
    }
}

