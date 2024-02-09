package com.fges.todoapp.commands;

import com.fges.todoapp.util.TaskState;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface Command {
    int execute(List<String> positionalArgs, Path filePath, TaskState taskState) throws IOException;
}
