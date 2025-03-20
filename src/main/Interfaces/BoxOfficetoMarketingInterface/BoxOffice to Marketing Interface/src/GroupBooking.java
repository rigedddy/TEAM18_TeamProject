import java.util.List;

public class GroupBooking {
    private List<Integer> seatIds;
    private double price;
    private int ID;
    public GroupBooking(List<Integer> seatIds, double price, int ID){
        this.seatIds = seatIds;
        this.price = price;
        this.ID = ID;
    }

    public List<Integer> getSeatIds() {
        return seatIds;
    }

    public double getPrice() {
        return price;
    }

    public int getID() {
        return ID;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setSeatIds(List<Integer> seatIds) {
        this.seatIds = seatIds;
    }
    public void setID(int ID){
        this.ID = ID;
    }
}