package com.fges.todoapp.dto;

public class TodoDTO {
    private String content;
    private String status;

    public TodoDTO() {}

    public TodoDTO(String content, String status) {
        this.content = content;
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
