package com.fges.todoapp.cli.options;

public class SourceOptionHandler implements OptionHandler {
    private String fileName;

    @Override
    public void handleOption(String optionValue) {
        fileName = optionValue;
    }

    public String getFileName() {
        return fileName;
    }
}
