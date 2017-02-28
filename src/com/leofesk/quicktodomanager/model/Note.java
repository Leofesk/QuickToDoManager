package com.leofesk.quicktodomanager.model;

public class Note {
    private int id;
    private String title;
    private String text;
    private String deadline;
    private String createdTime;
    private String endTime;
    private String status;

    public Note(int id, String title, String text, String deadline, String createdTime, String endTime, String status) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.deadline = deadline;
        this.createdTime = createdTime;
        this.endTime = endTime;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getDeadline() {
        return deadline;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
