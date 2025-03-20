

public class Seat {
    private String Type;
    private int ID;
    private boolean Available;
    private double Price;
    private boolean isWheelchairAccessible;
    private boolean isRestrictedView;
    private String Location;

    private boolean isReserved;


    public Seat(int id, String type, boolean available, double price, boolean accessible, boolean restricted, String location, Event event) {

        ID = id;
        Type = type;
        Available = available;
        Price = price;
        isWheelchairAccessible = accessible;
        isRestrictedView = restricted;
        Location = location;
    }

    public boolean isAvailable() {
        return Available;
    }

    public int getID() {
        return ID;
    }

    public String getType() {
        return Type;
    }

    public void setAvailable(boolean available) {
        Available = available;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public double getPrice() {
        return Price;
    }

    public boolean isAccessible() {
        return isWheelchairAccessible;
    }

    public boolean isRestricted() {
        return isRestrictedView;
    }

    public void setAccessible(boolean accessible) {
        isWheelchairAccessible = accessible;
    }

    public void setRestricted(boolean restricted) {
        isRestrictedView = restricted;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public void setReserved(boolean reserved){
        isReserved = reserved;
    }

    public String getLocation() {
        return Location;
    }
}
