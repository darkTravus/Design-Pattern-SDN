package com.fges.todoapp.util;

public class Todo {
    private String name;
    private TaskState taskState;

    public Todo(){}

    public Todo (String name, boolean isDone) {
        this.name = name;
        this.taskState = (isDone) ? TaskState.DONE : TaskState.NOT_DONE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TaskState getTaskState() {
        return taskState;
    }

    public void setTaskState(TaskState taskState) {
        this.taskState = taskState;
    }
}

