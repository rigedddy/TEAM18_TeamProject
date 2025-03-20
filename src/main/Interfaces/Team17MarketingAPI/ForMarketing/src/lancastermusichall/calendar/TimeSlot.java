package lancastermusichall.calendar;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class TimeSlot {         // USED TO REPRESENT A PERIOD OF TIME, USED FOR AVAILABILITY AND RESTRICTED PERIODS
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    public TimeSlot(LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    // Getter methods
    public LocalDate getDate() {
        return date;
    }
    public LocalTime getTime() {
        return startTime;
    }
    public LocalTime getEndTime() {
        return endTime;
    }
    // Setter methods
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public void setTime(LocalTime time) {
        this.startTime = time;
    }
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void printDetails() {
        System.out.println(date + ": Available slot from " + startTime + " to " + endTime);
    }

    @Override
    public String toString() {
        return "TimeSlot{" +
                "date=" + date +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
