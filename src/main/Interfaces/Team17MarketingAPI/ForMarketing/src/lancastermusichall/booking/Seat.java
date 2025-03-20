package lancastermusichall.booking;

public class Seat {
    private int seatId;
    private String location;
    private boolean isWheelchairAccessible;
    private boolean isRestrictedView;

    public Seat(int seatId, String location, boolean isWheelchairAccessible, boolean isRestrictedView) {
        this.seatId = seatId;
        this.location = location;
        this.isWheelchairAccessible = isWheelchairAccessible;
        this.isRestrictedView = isRestrictedView;
    }
    // GETTERS
    public int getSeatId() {
        return seatId;
    }
    public String getLocation() {
        return location;
    }
    public boolean isWheelchairAccessible() {
        return isWheelchairAccessible;
    }
    public boolean isRestrictedView() {
        return isRestrictedView;
    }
    // SETTERS
    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public void setWheelchairAccessible(boolean isWheelchairAccessible) {
        this.isWheelchairAccessible = isWheelchairAccessible;
    }
    public void setRestrictedView(boolean isRestrictedView) {
        this.isRestrictedView = isRestrictedView;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "seatId=" + seatId +
                ", location='" + location + '\'' +
                ", isWheelchairAccessible=" + isWheelchairAccessible +
                ", isRestrictedView=" + isRestrictedView +
                '}';
    }
}
