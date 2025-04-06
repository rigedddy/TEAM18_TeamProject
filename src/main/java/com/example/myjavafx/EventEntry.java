package com.example.myjavafx;

public class EventEntry {
    private String date;
    private String startTime;
    private String endTime;
    private String event;
    private String venue;
    private String eventType;

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