package com.example.myjavafx;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
/**
 * Represents an empty time slot in the schedule, including start and end times,
 * the date, and the venue where the slot is available.
 * This class is typically used to display available gaps in the event calendar or booking system.
 */
public class EmptySpace {


    private final StringProperty startTime;
    private final StringProperty endTime;
    private final StringProperty date;
    private final StringProperty venue;
    /**
     * Constructs an {@code EmptySpace} instance with the specified time range, date, and venue.
     *
     * @param startTime the start time of the empty slot (e.g., "14:00")
     * @param endTime   the end time of the empty slot (e.g., "15:30")
     * @param date      the date of the empty slot (e.g., "2025-04-11")
     * @param venue     the venue where the slot is available (e.g., "Main Hall")
     */
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
