package com.example.andrewspc.mimickme.Chats;

public class MessageObject {

    private String time;
    private String senderId;
    private String text;
    private String currentUserFromDB;

    public MessageObject() {
    }

    public MessageObject(String time, String senderId, String text, String currentUserFromDB) {
        this.time = time;
        this.senderId = senderId;
        this.text = text;
        this.currentUserFromDB = currentUserFromDB;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public String getCurrentUserFromDB() {
        return currentUserFromDB;
    }

    public void setCurrentUserFromDB(String currentUserFromDB) {
        this.currentUserFromDB = currentUserFromDB;
    }


}