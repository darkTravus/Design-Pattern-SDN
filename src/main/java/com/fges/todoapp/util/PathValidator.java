package com.fges.todoapp.util;

import java.nio.file.Files;
import java.nio.file.Path;

public class PathValidator {
    public boolean validatePath(Path filePath) {
        return Files.exists(filePath);
    }
}
