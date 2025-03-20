public interface FilmAndGroupServiceInterface {
    void sellFilmTicket(int EventID, int SeatNumber);
    void addGroupBooking(int bookingID);
    void registerDiscount(int bookingID, int PriceAfterDiscount);
}
