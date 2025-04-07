package com.example.myjavafx;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EmptySpace {


    private final StringProperty startTime;
    private final StringProperty endTime;
    private final StringProperty date;
    private final StringProperty venue;

    public EmptySpace(String startTime, String endTime, String date, String venue) {

        this.startTime = new SimpleStringProperty(startTime);
        this.endTime = new SimpleStringProperty(endTime);
        this.date = new SimpleStringProperty(date);
        this.venue = new SimpleStringProperty(venue);
    }


    public String getStartTime() { return startTime.get(); }
    public String getEndTime() { return endTime.get(); }
    public String getDate() { return date.get(); }
    public String getVenue() { return venue.get(); }


    public void setStartTime(String value) { startTime.set(value); }
    public void setEndTime(String value) { endTime.set(value); }
    public void setDate(String value) { date.set(value); }
    public void setVenue(String value) { venue.set(value); }


    public StringProperty startTimeProperty() { return startTime; }
    public StringProperty endTimeProperty() { return endTime; }
    public StringProperty dateProperty() { return date; }
    public StringProperty venueProperty() { return venue; }
}
