package com.example.myjavafx;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Dashboard {
    private final StringProperty eventName;
    private final StringProperty eventDate;
    private final StringProperty eventTime;

    public Dashboard(String eventName, String eventDate, String eventTime) {
        this.eventName = new SimpleStringProperty(eventName);
        this.eventDate = new SimpleStringProperty(eventDate);
        this.eventTime = new SimpleStringProperty(eventTime);
    }

    // Getters
    public String getEventName() {
        return eventName.get();
    }

    public String getEventDate() {
        return eventDate.get();
    }

    public String getEventTime() {
        return eventTime.get();
    }

    // Setters
    public void setEventName(String value) {
        eventName.set(value);
    }

    public void setEventDate(String value) {
        eventDate.set(value);
    }

    public void setEventTime(String value) {
        eventTime.set(value);
    }

    // Property getters for TableView binding
    public StringProperty eventNameProperty() {
        return eventName;
    }

    public StringProperty eventDateProperty() {
        return eventDate;
    }

    public StringProperty eventTimeProperty() {
        return eventTime;
    }
}