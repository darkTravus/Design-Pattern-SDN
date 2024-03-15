package com.fges.todoapp.todo;

public class Todo {
    private String name;
    private TaskState taskState;
    public Todo () {}
    public Todo (String name, TaskState taskState) {
        this.name = name;
        this.taskState = taskState;
    }
    public Todo (String name, String status) {
        this.name = name;
        this.taskState = (status.equals("DONE")) ? TaskState.DONE : TaskState.NOT_DONE;
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

    public String getStatus() {
        return (this.taskState == TaskState.DONE) ? "DONE" : "NOT_DONE";
    }
}

