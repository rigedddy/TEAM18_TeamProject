package Interfaces.OperationsInterface.Operations.src.eventScheduling;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EventScheduling implements Operations_EventScheduling_Interface {
    // List to store all events
    private List<Event> eventList;

    // Constructor to initialize the list of events
    public EventScheduling() {
        this.eventList = new ArrayList<>();
        // Populate with some events for testing, if necessary
    }

    @Override
    public List<Event> getEventsByDate(LocalDate date) {
        // Filter events based on the given date
        return eventList.stream()
                .filter(event -> event.getEventDate().equals(date))
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> getEventsByDateRange(LocalDate startDate, LocalDate endDate) {
        // Filter events based on the date range
        return eventList.stream()
                .filter(event -> !event.getEventDate().isBefore(startDate) && !event.getEventDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    @Override
    public Event getEvent(String eventId) {
        // Find the event by ID
        return eventList.stream()
                .filter(event -> event.getEventId().equals(eventId))
                .findFirst()
                .orElse(null); // return null if not found
    }

    @Override
    public void notifyEventUpdate() {
        // Notify operations of event update or cancellation
        System.out.println("Event update or cancellation has been notified.");
        // Actual implementation would likely involve notifying a system, sending alerts, etc.
    }

    // Additional helper methods (e.g., addEvent, removeEvent) can be added for managing events.

    public void addEvent(Event event) {
        eventList.add(event);
    }

    public void removeEvent(String eventId) {
        eventList.removeIf(event -> event.getEventId().equals(eventId));
    }
}
