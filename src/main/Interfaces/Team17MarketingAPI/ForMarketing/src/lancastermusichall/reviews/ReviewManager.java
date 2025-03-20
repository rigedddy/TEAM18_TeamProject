package lancastermusichall.reviews;

import lancastermusichall.calendar.Event;

import java.util.ArrayList;
import java.util.List;

public class ReviewManager implements ReviewAPI{

    private List<Review> reviews;
    private List<Insight> insights;

    public ReviewManager() {
        reviews = new ArrayList<>();
        insights = new ArrayList<>();
    };
    @Override
    public List<Review> getCustomerReviews() {
        return reviews;
    }

    @Override
    public List<Double> getOnlineRatings() {
        List<Double> ratings = new ArrayList<>();
        for (Review review : reviews) {
            ratings.add(review.getRating());
        }
        return ratings;
    }

    @Override
    public List<Insight> getInsights() {
        return insights;
    }

    @Override
    public Review getReview(int eventId) {
        for (Review review : reviews) {
            if (review.getEventId() == eventId) {
                return review;
            }
        }
        return null;
    }

    @Override
    public List<Review> getReviewsBelowRating(double rating) {
        List<Review> reviewsByRating = new ArrayList<>();
        for (Review review : reviews) {
            if (review.getRating() <= rating) {
                reviewsByRating.add(review);       // GETS REVIEWS WITH A RATING BELOW A SPECIFIC DOUBLE
            }
        }
        return reviewsByRating;
    }

    @Override
    public Insight getInsight(int eventId) {
        for (Insight insight : insights) {
            if (insight.getEventId() == eventId) {
                return insight;
            }
        }
        return null;
    }
    // DATABASE ISSUES SO ADD MANUALLY JUST TO SHOW HOW IT WORKS, FIX LATER
    public void addReview(Review review) {
        reviews.add(review);
    }
    public void addInsight(Insight insight) {
        insights.add(insight);
    }
}
