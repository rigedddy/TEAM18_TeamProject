package discount;

import java.util.List;
import java.util.Map;

// Discounts Interface
public interface DiscountsInterface {
    List<String> getAvailableDiscounts(); // Retrieves the ongoing active discounts for shows
    Map<String, Double> getAdjustedPrices(int eventID); // Retrieves prices based on availability
}

