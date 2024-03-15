package com.fges.todoapp.cli.options;

import com.fges.todoapp.todo.TaskState;

public class StatusOptionHandler implements OptionHandler {
    private TaskState taskState;

    @Override
    public void handleOption(String optionValue) {
        taskState = optionValue == null ? TaskState.DONE : TaskState.NOT_DONE;
    }

    public TaskState getTaskState() {
        return taskState;
    }
}

