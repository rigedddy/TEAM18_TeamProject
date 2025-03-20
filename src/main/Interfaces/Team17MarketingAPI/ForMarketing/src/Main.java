import lancastermusichall.booking.BookingInfo;
import lancastermusichall.booking.BookingManager;
import lancastermusichall.calendar.Event;
import lancastermusichall.calendar.OperationsCalendar;
import lancastermusichall.calendar.TimeSlot;
import lancastermusichall.reviews.Insight;
import lancastermusichall.reviews.Review;
import lancastermusichall.reviews.ReviewManager;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class Main {
    public static void main(String[] args) {

        OperationsCalendar calendar = new OperationsCalendar();
        int[] eventIds = {1, 2, 3, 4, 5, 6};
        String[] names = {"E1", "E2", "E3", "E4", "E5", "E6"};
        int[] day = {1, 1, 2, 3, 4, 6};
        int[] hour = {9, 10, 14, 16, 17, 19};
        String[] rooms = {"Main Hall", "Small Hall", "M1", "M1", "M2", "M3"};
        String[] types = {"Event", "Event", "Meeting", "Meeting", "Meeting", "Meeting"};
        for (int i = 0; i < eventIds.length; i++) {
            calendar.addEvent(new Event(
                    eventIds[i],
                    names[i],
                    types[i],
                    rooms[i],
                    LocalDate.of(2025, 3, day[i]),
                    LocalTime.of(hour[i], 0),
                    Duration.ofMinutes(60)
            ));
        }
        calendar.addRestrictedPeriod(new TimeSlot(LocalDate.of(2025, 4, 1), LocalTime.of(10, 30), LocalTime.of(19, 30)));

        // CALENDAR METHODS
        System.out.println("Get show schedule");
        for (Event e : calendar.getShowSchedule()) {
            System.out.println(e);
        }
        System.out.println();
        System.out.println("Get show schedule by date");
        for (Event e : calendar.getShowScheduleByDate(LocalDate.of(2025, 3, 1))) {
            System.out.println(e);
        }
        System.out.println();
        System.out.println("Get show schedule by date range");
        for (Event e : calendar.getShowScheduleByDate(LocalDate.of(2025, 3, 1), LocalDate.of(2025, 3, 4))) {
            System.out.println(e);
        }
        System.out.println();
        System.out.println("Get available gaps in the Main Hall");  // ONLY ON DAYS WHEN THERE ARE MAIN HALL EVENTS
        for (TimeSlot t : calendar.getAvailableGaps()) {
            System.out.println(t);
        }
        System.out.println();
        System.out.println("Check when Meeting Room M1 is available in the next three weeks");
        for (TimeSlot t : calendar.getMeetingRoomAvailability("M1")) {
            System.out.println(t);
        }
        System.out.println();
        System.out.println("Get restricted periods");
        for (TimeSlot t : calendar.getRestrictedBookings()) {
            System.out.println(t);
        }

        // BOOKING METHODS
        BookingManager bookingManager = new BookingManager(calendar);
        for (Event e : calendar.getShowSchedule()) {
            if (e.getEventId() == eventIds[0]) {
                e.getBookingInfo().setHoldDuration(90);
                e.getBookingInfo().setRestrictions("Some restrictions idk");
                e.getBookingInfo().setSpecialRequirements("Make sure something is idk");
            }
        }
        System.out.println("Print booking info of first two events");
        for (Event e : calendar.getShowSchedule()) {
            if (e.getEventId() <= eventIds[1]) {
                System.out.println(e.getBookingInfo());
            }
        }
        System.out.println();
        System.out.println("Getting seat configuration by ID...");
        bookingManager.getSeatingConfigById(1);

        System.out.println();
        System.out.println("Get requirements of Event 1");
        System.out.println(bookingManager.getSpecialRequirements(1));

        // REVIEW METHODS
        ReviewManager reviewManager = new ReviewManager();
        String[] info = {"It was good", "This was absolute trash", "I liked this event","This was straIgHT ASS!!"};
        double[] ratings = {9.8, 1.2, 7.9, 0.1};
        String[] usernames = {"CoolMan23", "idkusername", "yehyeh"};
        Review r1 = new Review(1,info[0],ratings[0],usernames[0]);
        Review r2 = new Review(1,info[1],ratings[1],usernames[1]);
        Review r3 = new Review(2,info[2],ratings[2],usernames[2]);
        Review r4 = new Review(3,info[3],ratings[3]);
        reviewManager.addReview(r1);
        reviewManager.addReview(r2);
        reviewManager.addReview(r3);
        reviewManager.addReview(r4);

        System.out.println("Get all reviews and then print details");
        for (Review r : reviewManager.getCustomerReviews()){
            System.out.println(r);
        }
        System.out.println();
        System.out.println("Get the reviews below 5");
        for (Review r : reviewManager.getReviewsBelowRating(5)){
            System.out.println(r);
        }
        Insight i1 = new Insight(1, "They should do...");
        Insight i2 = new Insight(2, "It would be better if...");
        reviewManager.addInsight(i1);
        reviewManager.addInsight(i2);

        System.out.println();
        System.out.println("Get and print insights");
        for (Insight i : reviewManager.getInsights()){
            System.out.println(i);
        }
    }
}
