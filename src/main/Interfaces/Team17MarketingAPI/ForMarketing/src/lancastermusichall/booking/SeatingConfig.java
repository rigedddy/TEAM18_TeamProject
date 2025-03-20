package lancastermusichall.booking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeatingConfig {
    private final int eventId;
    private Map<Integer,Integer> seats;

    public SeatingConfig(int eventId){
        this.eventId = eventId;
        this.seats = new HashMap<>();
    }
    // GETTER
    public int getEventId() {
        return eventId;
    }
    public Map<Integer, Integer> getSeats() {
        return seats;
    }
    // SETTER
    public void setSeats(HashMap<Integer, Integer> seats) {
        this.seats = seats;
    }
}
