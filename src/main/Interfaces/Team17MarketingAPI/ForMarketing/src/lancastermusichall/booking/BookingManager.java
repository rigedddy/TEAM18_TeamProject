package lancastermusichall.booking;

import lancastermusichall.calendar.Event;
import lancastermusichall.calendar.OperationsCalendar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingManager implements VenueBooking{
    private final OperationsCalendar calendar;

    public BookingManager(OperationsCalendar calendar) {
        this.calendar = calendar;
    }

    @Override
    public List<SeatingConfig> getSeatingConfigurations() {
        List<Event> events = calendar.getShowSchedule();
        List<SeatingConfig> seatingConfigs = new ArrayList<>();
        for (Event event : events) {
            seatingConfigs.add(event.getSeatingConfig());
        }
        return seatingConfigs;
    }

    @Override
    public SeatingConfig getSeatingConfigById(int eventId) {
        List<Event> events = calendar.getShowSchedule();
        for (Event event : events) {
            if (event.getEventId() == eventId) {
                return event.getSeatingConfig();
            }
        }
        return null;
    }

    @Override
    public String getSpecialRequirements(int eventId) {
        List<Event> events = calendar.getShowSchedule();
        for (Event event : events) {
            if (event.getEventId() == eventId) {
                return event.getBookingInfo().getSpecialRequirements();
            }
        }
        return "";
    }

    @Override
    public String getRestrictions(int eventId) {
        List<Event> events = calendar.getShowSchedule();
        for (Event event : events) {
            if (event.getEventId() == eventId) {
                return event.getBookingInfo().getRestrictions();
            }
        }
        return "";
    }

    @Override
    public LocalDate getBookingDeadline(int eventId) {
        List<Event> events = calendar.getShowSchedule();
        for (Event event : events) {
            if (event.getEventId() == eventId) {
                if (event.getBookingInfo().getReservationDeadline() == null) return event.getDate().minusDays(1);  // IF DEADLINE HASN'T BEEN SET THEN THE DAY BEFORE IS THE DEADLINE BY DEFAULT
                else return null;
            }
        }
        return null;
    }
}
