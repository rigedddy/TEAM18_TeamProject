package Interfaces.OperationsInterface.Operations.src.groupBookings;

import java.time.LocalDate;
import java.util.List;

public interface Operations_GroupBookings_Interface {
    // Retrieve group bookings by date or range
    List<MarketingGroupBooking> getGroupBookingsByDate(LocalDate date);
    List<MarketingGroupBooking> getGroupBookingsByDateRange(LocalDate startDate, LocalDate endDate);

    // Retrieve specific group booking details
    MarketingGroupBooking getGroupBookingDetails(String groupId);

    // Notify Operations about group booking updates or cancellations
    void notifyGroupBookingUpdate();
}
