package com.example.sytemy_mobilne_3;

import java.util.Date;
import java.util.UUID;

public class Task {
    private UUID id;
    private String name;
    private Date date;
    private boolean done;
    private Category category;

    public Task() {
        id = UUID.randomUUID();
        date = new Date();
        category=Category.HOME;
    }

    public void setName(String name){this.name=name;}
    public String getName(){return name;}
    public void setDate(Date date){this.date=date;}
    public Date getDate(){return date;}
    public void setDone(boolean isChecked){done=isChecked;}
    public boolean isDone() { return done; }
    public UUID getId(){return id;}
    public void setCategory(Category category) { this.category = category; }
    public Category getCategory() {return category; }
}