package com.fges.todoapp.todo;

public class Todo {
    private String name;
    private TaskState taskState;
    public Todo (String name, TaskState taskState) {
        this.name = name;
        this.taskState = taskState;
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

