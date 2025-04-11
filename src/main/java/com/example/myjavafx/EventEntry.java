package com.example.myjavafx;

/**
 * Represents an event entry with details including the date, start and end times,
 * event name, venue, and event type.
 *
 * This class is used to store and display event information
 */
public class EventEntry {
    private String date;
    private String startTime;
    private String endTime;
    private String event;
    private String venue;
    private String eventType;
    /**
     * Constructs an {@code EventEntry} instance with the specified event details.
     *
     * @param date      the date of the event (e.g., "2025-04-11")
     * @param startTime the start time of the event (e.g., "14:00")
     * @param endTime   the end time of the event (e.g., "15:30")
     * @param event     the name of the event (e.g., "Concert")
     * @param venue     the venue where the event is held (e.g., "Main Hall")
     * @param eventType the type of the event (e.g., "Music", "Film", etc.)
     */
    public EventEntry(String date, String startTime, String endTime, String event, String venue, String eventType) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.event = event;
        this.venue = venue;
        this.eventType = eventType;
    }

    // Getters
    public String getDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getEvent() {
        return event;
    }

    public String getVenue() {
        return venue;
    }

    public String getEventType() {
        return eventType;
    }
}