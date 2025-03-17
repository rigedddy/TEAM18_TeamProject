package Interfaces.OperationsInterface.Operations.src.groupBookings;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GroupBookings implements Operations_GroupBookings_Interface {
    // List to store all group bookings
    private List<MarketingGroupBooking> groupBookingList;

    // Constructor to initialize the list of group bookings
    public GroupBookings() {
        this.groupBookingList = new ArrayList<>();
        // You can add some test data here if necessary
    }

    @Override
    public List<MarketingGroupBooking> getGroupBookingsByDate(LocalDate date) {
        // Filter group bookings based on the given date
        return groupBookingList.stream()
                .filter(groupBooking -> groupBooking.getBookingDate().equals(date))
                .collect(Collectors.toList());
    }

    @Override
    public List<MarketingGroupBooking> getGroupBookingsByDateRange(LocalDate startDate, LocalDate endDate) {
        // Filter group bookings based on the date range
        return groupBookingList.stream()
                .filter(groupBooking -> !groupBooking.getBookingDate().isBefore(startDate) && !groupBooking.getBookingDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    @Override
    public MarketingGroupBooking getGroupBookingDetails(String groupId) {
        // Find the group booking by groupId
        return groupBookingList.stream()
                .filter(groupBooking -> groupBooking.getGroupId().equals(groupId))
                .findFirst()
                .orElse(null); // Return null if not found
    }

    @Override
    public void notifyGroupBookingUpdate() {
        // Notify operations about group booking updates or cancellations
        System.out.println("Group booking update or cancellation has been notified.");
        // This could involve sending notifications, logging changes, etc.
    }

    // Additional helper methods to manage group bookings

    public void addGroupBooking(MarketingGroupBooking groupBooking) {
        groupBookingList.add(groupBooking);
    }

    public void removeGroupBooking(String groupId) {
        groupBookingList.removeIf(groupBooking -> groupBooking.getGroupId().equals(groupId));
    }
}
