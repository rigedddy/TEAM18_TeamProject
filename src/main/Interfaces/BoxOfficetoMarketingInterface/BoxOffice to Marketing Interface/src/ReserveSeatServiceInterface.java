import java.util.List;
public interface ReserveSeatServiceInterface {

    void ReserveSeats(int eventID, List<Integer> seatNumbers);
    void releaseHeldSeats(int eventID);
    boolean checkSeatAvailability(int eventID, int seatNumber);
}
