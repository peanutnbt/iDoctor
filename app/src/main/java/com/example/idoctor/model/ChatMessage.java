package com.example.idoctor.model;
import java.util.Date;

public class ChatMessage {
    private String messText, messUser;
    private long messTime;
    private String type;

    public ChatMessage() {
    }

    public ChatMessage(String messText, String messUser,String type) {
        this.messText = messText;
        this.messUser = messUser;
        this.type = type;
        this.messTime = new Date().getTime();
    }

    public String getMessText() {
        return messText;
    }

    public void setMessText(String messText) {
        this.messText = messText;
    }

    public String getMessUser() {
        return messUser;
    }

    public void setMessUser(String messUser) {
        this.messUser = messUser;
    }

    public long getMessTime() {
        return messTime;
    }

    public void setMessTime(long messTime) {
        this.messTime = messTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
