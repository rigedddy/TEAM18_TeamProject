package lancastermusichall.booking;

import java.time.LocalDate;

public class BookingInfo {
    private final int eventId;
    private int holdDuration = 0;
    private String specialRequirements = "No requirements";  // DEFAULT IS NO REQUIREMENTS
    private String restrictions = "No restrictions"; // DEFAULT IS NO RESTRICTIONS
    private LocalDate reservationDeadline = null;

    public BookingInfo(int eventId) {
        this.eventId = eventId;
    }
    // GETTER METHODS
    public int getEventId() {
        return eventId;
    }
    public int getHoldDuration() {
        return holdDuration;
    }
    public String getSpecialRequirements() {
        return specialRequirements;
    }
    public String getRestrictions() {
        return restrictions;
    }
    public LocalDate getReservationDeadline() {
        return reservationDeadline;
    }
    // SETTER METHODS
    public void setHoldDuration(int holdDuration) {
        this.holdDuration = holdDuration;
    }
    public void setSpecialRequirements(String specialRequirements) {
        this.specialRequirements = specialRequirements;
    }
    public void setRestrictions(String restrictions) {
        this.restrictions = restrictions;
    }
    public void setReservationDeadline(LocalDate reservationDeadline) {
        this.reservationDeadline = reservationDeadline;
    }

    @Override
    public String toString() {
        return "BookingInfo{" +
                "eventId=" + eventId +
                ", holdDuration=" + holdDuration +
                ", specialRequirements='" + specialRequirements + '\'' +
                ", restrictions='" + restrictions + '\'' +
                ", reservationDeadline=" + reservationDeadline +
                '}';
    }
}
