package com.fges.todoapp.commands;

import java.io.IOException;
import java.util.List;

public interface Command {
    int execute(List<String> positionalArgs) throws IOException;
}
