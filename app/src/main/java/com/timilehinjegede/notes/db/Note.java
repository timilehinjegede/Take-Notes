package com.timilehinjegede.notes.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id ;
    private String title ;
    private String body ;
    private String date;

    public Note(String title, String body,String date) {
        this.title = title;
        this.body = body;
        this.date = date ;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getDate() {
        return date;
    }
}
