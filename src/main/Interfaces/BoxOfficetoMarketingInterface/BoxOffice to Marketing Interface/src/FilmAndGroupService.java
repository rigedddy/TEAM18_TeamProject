import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
public class FilmAndGroupService implements FilmAndGroupServiceInterface{

    List<GroupBooking> allGroupBookings = new ArrayList<>();
    List<Event> allEvents = new ArrayList<>();
    @Override
    public void sellFilmTicket(int EventID, int SeatNumber) {
        for (Event event: allEvents){
            if (event.getID() == EventID){
                event.buySeat(SeatNumber);
            }
        }
    }

    @Override
    public void addGroupBooking(int bookingID) {
        int price = 50;
        List<Integer> seatBookingIDs = new ArrayList<>();
        GroupBooking booking = new GroupBooking(seatBookingIDs, price, bookingID) ;
        allGroupBookings.add(booking);
    }

    @Override
    public void registerDiscount(int bookingID, int PriceAfterDiscount) {
        for (GroupBooking booking: allGroupBookings){
            if (booking.getID() == bookingID){
                booking.setPrice(PriceAfterDiscount);
            }
        }
    }
}
