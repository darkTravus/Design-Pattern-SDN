package com.fges.todoapp.storage.files.csv;

import com.fges.todoapp.storage.StorageHandler;
import com.fges.todoapp.todo.TaskState;
import com.fges.todoapp.todo.Todo;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CsvFileHandler implements StorageHandler {
    @Override
    public void write(List<Todo> todos, Path filePath) {
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        try (
                FileWriter fWriter = new FileWriter(filePath.toFile(), true);
                CSVPrinter csvPrinter = new CSVPrinter(fWriter, csvFormat))
        {
            for (Todo todo : todos) {
                List<String> rowDataList = new ArrayList<>();
                rowDataList.add(todo.getName());
                rowDataList.add((todo.getTaskState() == TaskState.DONE) ? "Done" : "Not Done");

                csvPrinter.printRecord(rowDataList);
            }
        } catch (IOException e) {
            System.err.println("Error processing CSV insert: " + e.getMessage());
        }
    }

    @Override
    public List<Todo> read(Path filePath) {
        List<Todo> todoList = new ArrayList<>();

        try (FileReader fReader = new FileReader(filePath.toFile()); CSVParser csvParser = CSVFormat.DEFAULT.parse(fReader)) {
            for (CSVRecord csvRecord : csvParser) {
                String name = csvRecord.get(0);
                String status = csvRecord.get(1);

                TaskState taskState = status.equals("Done") ? TaskState.DONE : TaskState.NOT_DONE;
                Todo todo = new Todo(name, taskState);
                todoList.add(todo);
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }

        return todoList;
    }
}