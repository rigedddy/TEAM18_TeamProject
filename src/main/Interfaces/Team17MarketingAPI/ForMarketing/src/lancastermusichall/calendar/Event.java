package lancastermusichall.calendar;

import lancastermusichall.booking.BookingInfo;
import lancastermusichall.booking.SeatingConfig;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class Event {
    private int eventId;
    private String eventName;
    private String eventType;
    private String room;
    private LocalDate date;
    private LocalTime startTime;
    private Duration duration;
    private BookingInfo bookingInfo;        // CONTAINS BOOKING INFO e.g REQUIREMENTS, RESTRICTIONS
    private SeatingConfig seatingConfig;    // CONTAINS THE SEATING CONFIGURATION ALONG WITH SEAT INFO

    public Event(int eventId, String eventName, String eventType, String room, LocalDate date, LocalTime startTime, Duration duration) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventType = eventType;
        this.room = room;
        this.date = date;
        this.startTime = startTime;
        this.duration = duration;
        bookingInfo = new BookingInfo(eventId);       // BY DEFAULT AN EVENT DOESN'T HAVE BOOKING INFO, SET AFTER
        seatingConfig = new SeatingConfig(eventId);   // BY DEFAULT AN EVENT DOESN'T HAVE A SEATING CONFIGURATION, SET AFTER
    }
    // Getter methods
    public int getEventId() {
        return eventId;
    }
    public String getEventName(){
        return eventName;
    }
    public String getEventType(){
        return eventType;
    }
    public String getRoom(){
        return room;
    }
    public LocalDate getDate(){
        return date;
    }
    public LocalTime getStartTime(){
        return startTime;
    }
    public Duration getDuration(){
        return duration;
    }
    public LocalTime getEndTime(){
        return startTime.plus(duration);
    }
    public BookingInfo getBookingInfo(){
        return bookingInfo;
    }
    public SeatingConfig getSeatingConfig(){
        return seatingConfig;
    }

    // Setter methods
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
    public void setRoom(String room) {
        this.room = room;
    }
    public void setStartDate(LocalDate date) {
        this.date = date;
    }
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    public void setDuration(Duration duration) {
        this.duration = duration;
    }
    public void setBookingInfo(BookingInfo bookingInfo) {
        this.bookingInfo = bookingInfo;
    }
    public void setSeatingConfig(SeatingConfig seatingConfig) {
        this.seatingConfig = seatingConfig;
    }

    // View
    public void printDetails() {
        System.out.println("Event ID: " + eventId);
        System.out.println("Event Name: " + eventName);
        System.out.println("Event Type: " + eventType);
        System.out.println("Room: " + room);
        System.out.println("Date: " + date);
        System.out.println("Start: " + startTime + " - " + getEndTime());;
        System.out.println("Duration: " + duration);
    }
    @Override
    public String toString() {
        return "Event{" +
                "eventId=" + eventId +
                ", eventName='" + eventName + '\'' +
                ", eventType='" + eventType + '\'' +
                ", room='" + room + '\'' +
                ", date=" + date +
                ", startTime=" + startTime +
                ", duration=" + duration +
                ", bookingInfo=" + bookingInfo +
                ", seatingConfig=" + seatingConfig +
                '}';
    }
}
