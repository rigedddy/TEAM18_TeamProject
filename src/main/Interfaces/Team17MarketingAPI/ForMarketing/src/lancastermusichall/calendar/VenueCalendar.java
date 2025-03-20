package lancastermusichall.calendar;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface VenueCalendar {
    List<Event> getShowSchedule();
    List<Event> getShowScheduleByDate(LocalDate date);
    List<Event> getShowScheduleByDate(LocalDate startDate, LocalDate endDate);
    List<TimeSlot> getAvailableGaps();
    List<TimeSlot> getRestrictedBookings();
    List<TimeSlot> getMeetingRoomAvailability(String room); //  WITHIN THREE WEEKS
}
