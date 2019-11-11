package com.example.idoctor.model;

public class Status {
    private String time;
    private String date;
    private String state;

    public Status() {

    }

    public Status(String time, String date, String state) {
        this.time = time;
        this.date = date;
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
