package com.almost.peruibemelhor.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "message_table")
public class Message {

    @PrimaryKey(autoGenerate = true)
    private int id_message;

    private int id_group;
    private int id_user;

    private String message;

    private String date;

    public Message(int id_message, int id_group, int id_user, String message, String date) {
        this.id_message = id_message;
        this.id_group = id_group;
        this.id_user = id_user;
        this.message = message;
        this.date = date;
    }

    public int getId_message() {
        return id_message;
    }

    public void setId_message(int id_message) {
        this.id_message = id_message;
    }

    public int getId_group() {
        return id_group;
    }

    public void setId_group(int id_group) {
        this.id_group = id_group;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
