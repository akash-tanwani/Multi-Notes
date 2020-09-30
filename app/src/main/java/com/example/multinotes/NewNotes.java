package com.example.multinotes;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

public class NewNotes implements Serializable {
    private String name;
    private String description;
    private String dateTime;

    public NewNotes(){
        this.name="";
        this.description="";
        this.dateTime="";
    }

    String getDescription() {return description;}

    void setDescription(String description){ this.description=description; }

    public String getName(){return name;}

    public void setName(String name){this.name=name;}

    void setDateTime(String dateTime){this.dateTime=dateTime;};

    public String getDateTime() {
        //dateTime=System.currentTimeMillis();
        return dateTime;}

    @NonNull
    public String toString() {return name + ": " + description;}
}
