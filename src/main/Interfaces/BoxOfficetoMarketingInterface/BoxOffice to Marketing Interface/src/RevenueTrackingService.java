import java.util.ArrayList;
import java.util.List;

public class RevenueTrackingService implements RevenueTrackingServiceInterface{

    @Override
    public double ExtractTicketRevenueData(Event event) {
        return event.getRevenue();
    }
}
