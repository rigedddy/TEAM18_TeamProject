package Interfaces.OperationsInterface.Operations.src.eventScheduling;

import java.time.LocalDate;
import java.util.List;

public interface Operations_EventScheduling_Interface {
    // Retrieve events based on date or range
    List<Event> getEventsByDate(LocalDate date);
    List<Event> getEventsByDateRange(LocalDate startDate, LocalDate endDate);

    // Retrieve event details
    Event getEvent(String eventId);

    // Notify Operations of event updates or cancellations
    void notifyEventUpdate();
}
