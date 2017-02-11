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

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }
}
