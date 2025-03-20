package lancastermusichall.reviews;

public class Review {
    private final int eventId;
    private final String username;
    private final String info;
    private final double rating;

    public Review(int eventId, String info, double rating, String username) {
        this.eventId = eventId;
        this.username = username;
        this.info = info;
        this.rating = rating;
    }
    public Review(int eventId, String info, double rating) {
        this.eventId = eventId;
        this.info = info;
        this.rating = rating;
        this.username = "Anonymous";
    }
    public int getEventId() {
        return eventId;
    }
    public String getUsername() {
        return username;
    }
    public String getInfo() {
        return info;
    }
    public double getRating() {
        return rating;
    }

    @Override
    public String toString() {
        return "Review{" +
                "eventId=" + eventId +
                ", username='" + username + '\'' +
                ", info='" + info + '\'' +
                ", rating=" + rating +
                '}';
    }
}
