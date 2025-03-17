package Interfaces.OperationsInterface.Operations.src.groupBookings;

import java.time.LocalDate;

public class MarketingGroupBooking {
    private String groupId;
    private String groupName;
    private LocalDate bookingDate;

    // Constructor
    public MarketingGroupBooking(String groupId, String groupName, LocalDate bookingDate) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.bookingDate = bookingDate;
    }

    // Getters and setters
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    // Override toString for better representation
    @Override
    public String toString() {
        return "MarketingGroupBooking{id=" + groupId + ", name='" + groupName + "', date=" + bookingDate + "}";
    }
}
