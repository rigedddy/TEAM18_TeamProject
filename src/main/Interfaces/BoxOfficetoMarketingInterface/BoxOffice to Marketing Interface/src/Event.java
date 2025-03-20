import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Event {
    private int ID;
    private LocalDateTime DateTime;
    private String Name;
    private float Price;
    private double Revenue;
    private double VIPRevenue;
    private double DiscountedRevenue;
    private int ticketsSold;
    private int VIPTicketsSold;
    private int DiscountedTicketsSold;
    private double Refunds;
    private List<Seat> seatList;

    public Event(int id,  LocalDateTime date, String name, float price) {
        ID = id; DateTime = date; Name = name; Price = price;
        seatList = new ArrayList<>();
        //Regular constructor
        VIPRevenue = 0;
        DiscountedRevenue = 0;
        Revenue = 0;
        Refunds = 0;
    }

    // Getters and setters
    public int getID() {
        return ID;
    }

    public void setID(int id) {
        this.ID = id;
    }


    public LocalDateTime getDate() {
        return DateTime;
    }

    public void setDate(LocalDateTime date) {
        DateTime = date;
    }

    public String getName() {
        return Name;
    }

    public void setName(String eventName) {
        Name = eventName;
    }
    public void setPrice(float price) {
        Price = price;
    }

    public float getPrice() {
        return Price;
    }

    //The following are here because we don't know the layout of the seat yet.
    //In the future, this can initialise with the standard layout.
    public List<Seat> getSeatList() {
        return seatList;
    }

    public void setSeatList(List<Seat> seatList) {
        this.seatList = seatList;
    }

    public Seat getSeatOfID(int id) {
        Seat found = null;
        for (Seat seat : seatList) {
            if (seat.getID() == id) {
                found = seat;
            }
        }
        return found;
    }

    public String buySeat(int id) {
        Seat found = getSeatOfID(id);

        if (found == null) {return "Seat doesn't exist";}

        found.setAvailable(false);

        Revenue += found.getPrice();

        //Clunky but does the job
        String type = found.getType();
        if (Objects.equals(type, "VIP")) {VIPRevenue += found.getPrice(); VIPTicketsSold++;}
        if (Objects.equals(type, "DISCOUNTED")) {DiscountedRevenue += found.getPrice(); DiscountedTicketsSold++;}

        ticketsSold++;

        return "Bought";
    }

    //Simple get revenues
    public double getRevenue() {
        return Revenue;
    }

    public double getDiscountedRevenue() {
        return DiscountedRevenue;
    }

    public double getVIPRevenue() {
        return VIPRevenue;
    }

    //Refund functions

    public String RefundSeat(int id) {
        Seat found = getSeatOfID(id);
        if (found == null) {return "Seat doesn't exist";}

        found.setAvailable(true);

        Refunds += found.getPrice();

        return "Refunded";
    }

    public double getRefunds() {
        return Refunds;
    }

    //Getting tickets sold

    public int getDiscountedTicketsSold() {
        return DiscountedTicketsSold;
    }

    public int getTicketsSold() {
        return ticketsSold;
    }
    public int getVIPTicketsSold() {
        return VIPTicketsSold;
    }
}