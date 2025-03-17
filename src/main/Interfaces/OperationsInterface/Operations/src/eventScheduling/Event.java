package Interfaces.OperationsInterface.Operations.src.eventScheduling;

import java.time.LocalDate;

public class Event {
    private String eventId;
    private String eventName;
    private LocalDate eventDate;

    // Constructor
    public Event(String eventId, String eventName, LocalDate eventDate) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
    }

    // Getters and setters
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    // You could override toString() for better representation of an event
    @Override
    public String toString() {
        return "Event{id=" + eventId + ", name='" + eventName + "', date=" + eventDate + "}";
    }
}
