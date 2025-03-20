package lancastermusichall.calendar;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class OperationsCalendar implements VenueCalendar{ // FOR READING ONLY
    private final List<Event> events;                // LIST OF EVENTS
    private final List<TimeSlot> restricted;         // LIST OF RESTRICTED PERIODS
    private static final LocalTime START_TIME = LocalTime.of(9, 0);
    private static final LocalTime END_TIME = LocalTime.of(22, 0);

    public OperationsCalendar() {
        this.events = new ArrayList<>();
        this.restricted = new ArrayList<>();
    }
    @Override
    public List<Event> getShowSchedule() {
        return events;
    }
    @Override
    public List<Event> getShowScheduleByDate(LocalDate date) {
        List<Event> result = new ArrayList<>();
        for (Event event : events) {
            if (event.getDate().equals(date)) {
                result.add(event);
            }
        }
        return result;
    }
    @Override
    public List<Event> getShowScheduleByDate(LocalDate startDate, LocalDate endDate) {
        List<Event> result = new ArrayList<>();
        for (Event event : events) {
            LocalDate date = event.getDate();
            if (date.equals(startDate) || date.isAfter(startDate) && date.equals(endDate) || date.isBefore(endDate)) {
                result.add(event);
            }
        }
        return result;
    }
    @Override
    public List<TimeSlot> getAvailableGaps() {               // CHECKS FOR AVAILABLE GAPS IN THE MAIN HALL FOR FILMS
        List<TimeSlot> availableGaps = new ArrayList<>();
        List<Event> mainHallEvents = new ArrayList<>();

        for (Event event : events) {
            if (event.getRoom().equals("Main Hall")) {
                mainHallEvents.add(event);
            }
        }
        mainHallEvents.sort(Comparator.comparing(Event::getStartTime));

        if (!mainHallEvents.isEmpty()) {
            Event firstEvent = mainHallEvents.getFirst();
            if (firstEvent.getStartTime().isAfter(START_TIME)) {
                availableGaps.add(new TimeSlot(firstEvent.getDate(),START_TIME,firstEvent.getStartTime()));
            }

            for (int i= 0; i < mainHallEvents.size() - 1; i++) {
                Event currentEvent = mainHallEvents.get(i);
                Event nextEvent = mainHallEvents.get(i+1);

                LocalTime gapStart = currentEvent.getEndTime();
                LocalTime gapEnd = nextEvent.getStartTime();

                if (gapEnd.isAfter(gapStart)) {
                    availableGaps.add(new TimeSlot(currentEvent.getDate(),gapStart, gapEnd));
                }
            }
            Event lastEvent = mainHallEvents.getLast();
            if (lastEvent.getEndTime().isBefore(END_TIME)) {
                availableGaps.add(new TimeSlot(lastEvent.getDate(), lastEvent.getEndTime(), END_TIME));
            }
        }
        return availableGaps;
    }

    @Override
    public List<TimeSlot> getRestrictedBookings() {      // RETURNS THE LIST OF RESTRICTED PERIODS
        return restricted;
    }

    @Override
    public List<TimeSlot> getMeetingRoomAvailability(String room) {       // CHECKS IF A SPECIFIC MEETING ROOM
        List<TimeSlot> meetingRoomAvailability = new ArrayList<>();       // IS AVAILABLE IN THE NEXT THREE-WEEKS
        List<Event> meetings = new ArrayList<>();

        LocalDate today = LocalDate.now();
        LocalDate range = today.plusWeeks(3);

        for (Event event : events) {
            if (event.getRoom().equals(room) && !event.getDate().isBefore(today) && !event.getDate().isAfter(range)) {
                meetings.add(event);
            }
        }
        Map<LocalDate, List<Event>> meetingsByDate = new TreeMap<>();
        for (Event meeting : meetings) {
            meetingsByDate.computeIfAbsent(meeting.getDate(), k -> new ArrayList<>()).add(meeting);
        }
        for (List<Event> meetingsList : meetingsByDate.values()){
            meetingsList.sort(Comparator.comparing(Event::getStartTime));
        }
        for (LocalDate date = today; !date.isAfter(range); date = date.plusDays(1)) {
            List<Event> meetingsForDate = meetingsByDate.getOrDefault(date, new ArrayList<>());

            if (meetingsForDate.isEmpty() || meetingsForDate.getFirst().getStartTime().isAfter(START_TIME)){
                meetingRoomAvailability.add(new TimeSlot(date, START_TIME, meetingsForDate.isEmpty() ? END_TIME : meetingsForDate.getFirst().getStartTime()));
            }
            for (int i = 0; i < meetingsForDate.size() - 1; i++) {
                Event currentMeeting = meetingsForDate.get(i);
                Event nextMeeting = meetingsForDate.get(i+1);
                LocalTime gapStart = currentMeeting.getEndTime();
                LocalTime gapEnd = nextMeeting.getStartTime();

                if (gapEnd.isAfter(gapStart)) {
                    meetingRoomAvailability.add(new TimeSlot(date, gapStart, gapEnd));
                }
            }
            if (!meetingsForDate.isEmpty() && meetingsForDate.getLast().getEndTime().isBefore(END_TIME)) {
                meetingRoomAvailability.add(new TimeSlot(date, meetingsForDate.getLast().getEndTime(), END_TIME));
            }
        }
        return meetingRoomAvailability;
    }


    // USE THIS TO ADD TO THE CALENDAR COS DATABASE CONNECTION IS HAVING ISSUES,
    // IT SHOULD BE THAT THE CALENDAR CREATES A LIST FROM THE DATABASE EVENTS
    public void addEvent(Event newEvent) {
        events.add(newEvent);
    }
    public void addRestrictedPeriod(TimeSlot t){
        restricted.add(t);
    }
}
