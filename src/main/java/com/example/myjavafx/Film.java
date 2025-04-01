package com.example.myjavafx;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Film {

    private final StringProperty title;
    private final StringProperty StartTime;

    private final StringProperty EndTime;
    private final StringProperty date;

    public Film(String title, String StartTime,String endTime, String date) {

        this.title = new SimpleStringProperty(title);
        this.StartTime = new SimpleStringProperty(StartTime);


        this.EndTime = new SimpleStringProperty(endTime);
        this.date = new SimpleStringProperty(date);
    }


    public String getTitle() { return title.get(); }
    public String getStartTime() { return StartTime.get(); }

    public String getEndTime() { return EndTime.get(); }
    public String getDate() { return date.get(); }


    public void setTitle(String value) { title.set(value); }
    public void setStartTime(String value) { StartTime.set(value); }

    public void setEndTime(String value) { EndTime.set(value); }
    public void setDate(String value) { date.set(value); }


    public StringProperty titleProperty() { return title; }
    public StringProperty StartTimeProperty() { return StartTime; }

    public StringProperty EndTimeProperty() { return EndTime; }
    public StringProperty dateProperty() { return date; }
}
