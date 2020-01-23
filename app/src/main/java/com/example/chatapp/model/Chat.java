package com.example.chatapp.model;

import java.io.Serializable;

public class Chat implements Serializable {

    private String sender;
    private String room;
    private String message;

    public Chat(String sender, String receiver, String message) {
        this.sender = sender;
        this.room = receiver;
        this.message = message;
    }

    public Chat() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
