package com.example.assignment2;

public class Note {

    private String title;

    private String note;

    private String date;

    private String time;

    private String key;

    public Note(){



    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public String getNote() {
        return note;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }


    public String getKey(){
        return  key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
