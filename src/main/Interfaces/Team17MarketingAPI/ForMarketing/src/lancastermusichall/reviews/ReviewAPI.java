package lancastermusichall.reviews;

import lancastermusichall.calendar.Event;

import java.util.List;

public interface ReviewAPI {
    List<Review> getCustomerReviews();
    List<Double> getOnlineRatings();
    List<Insight> getInsights();
    Review getReview(int eventId);
    List<Review> getReviewsBelowRating(double rating);
    Insight getInsight(int eventId);
}
