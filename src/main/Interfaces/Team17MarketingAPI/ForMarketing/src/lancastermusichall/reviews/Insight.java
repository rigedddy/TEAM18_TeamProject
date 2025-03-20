package lancastermusichall.reviews;

public class Insight {
    private final String info;
    private final int eventId;

    public Insight(int eventId, String info) {
        this.info = info;
        this.eventId = eventId;
    }
    public int getEventId() {
        return eventId;
    }
    public String getInfo() {
        return info;
    }

    @Override
    public String toString() {
        return "Insight{" +
                "info='" + info + '\'' +
                ", eventId=" + eventId +
                '}';
    }
}
