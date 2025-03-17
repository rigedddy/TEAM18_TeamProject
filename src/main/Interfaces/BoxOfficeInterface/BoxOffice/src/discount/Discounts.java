package discount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// discount.Discounts Implementation
public class Discounts implements DiscountsInterface {

    @Override
    public List<String> getAvailableDiscounts() {
        return new ArrayList<>(); // Retrieve active discounts
    }

    @Override
    public Map<String, Double> getAdjustedPrices(int eventID) {
        return new HashMap<>(); // Retrieve adjusted prices
    }

}