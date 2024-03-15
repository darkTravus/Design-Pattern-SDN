package com.fges.todoapp.cli.options;

public class OutputOptionHandler implements OptionHandler {
    private String outputFile;

    @Override
    public void handleOption(String optionValue) {
        outputFile = optionValue;
    }

    public String getOutputFile() {
        return outputFile;
    }
}

