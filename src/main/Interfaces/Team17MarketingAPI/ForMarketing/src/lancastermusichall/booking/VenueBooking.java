package lancastermusichall.booking;

import java.time.LocalDate;
import java.util.List;

public interface VenueBooking {
    List<SeatingConfig> getSeatingConfigurations();

    SeatingConfig getSeatingConfigById(int eventId);
    String getSpecialRequirements(int eventId);
    String getRestrictions(int eventId);
    LocalDate getBookingDeadline(int eventId);
}
// I PUT THE THREE-WEEK AVAILABILITY ONE IN THE CALENDAR API COS IT MADE MORE SENSE THERE
