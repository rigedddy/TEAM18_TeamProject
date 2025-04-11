package com.example.myjavafx;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
/**
 * Represents an event entry for display on the dashboard.
 * Contains the name, date, and time of the event using JavaFX properties.
 */
public class Dashboard {
    private final StringProperty eventName;
    private final StringProperty eventDate;
    private final StringProperty eventTime;
    /**
     * Constructs a new Dashboard event entry with the specified details.
     *
     * @param eventName The name of the event.
     * @param eventDate The date of the event.
     * @param eventTime The time of the event.
     */
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

    public void setEventName(String value) {
        eventName.set(value);
    }

    public void setEventDate(String value) {
        eventDate.set(value);
    }

    public void setEventTime(String value) {
        eventTime.set(value);
    }

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