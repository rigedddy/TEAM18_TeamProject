import java.util.ArrayList;
import java.util.List;

public class ReserveSeatService implements ReserveSeatServiceInterface{
    List<Event> allEvents = new ArrayList<>();
    @Override
    public void ReserveSeats(int eventID, List<Integer> seatNumbers) {
        for (Event event: allEvents){
            if (event.getID() == eventID){
                for(Integer num: seatNumbers){
                    event.getSeatOfID(num).setReserved(true);
                }
            }
        }
    }

    @Override
    public void releaseHeldSeats(int eventID) {
        for (Event event: allEvents){
            if (event.getID() == eventID){
                for (Seat seat: event.getSeatList()){
                    seat.setReserved(false);
                }
            }
        }
    }

    @Override
    public boolean checkSeatAvailability(int eventID, int seatNumber) {
        for (Event event: allEvents){
            if (event.getID() == eventID){
                return event.getSeatOfID(seatNumber).isAvailable();
            }
        } return false;
    }
}
