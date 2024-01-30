package com.fges.todoapp.commands;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface Command {
    int execute(List<String> positionalArgs, Path filePath) throws IOException;
}