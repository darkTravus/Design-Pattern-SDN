package com.fges.todoapp.cli;

import org.apache.commons.cli.*;

public class CommandLineProcessor {
    public static CommandLine parseCommandLine (String[] args, Options options) {
        CommandLineParser parser = new DefaultParser();
        try {
            return parser.parse(options, args);
        } catch (ParseException ex) {
            System.err.println("Fail to parse arguments: " + ex.getMessage());
            return null;
        }
    }
}