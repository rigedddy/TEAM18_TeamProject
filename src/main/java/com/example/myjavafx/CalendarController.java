package com.example.myjavafx;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
/**
 * Controller class for the Calendar view in the application.
 * Handles user interactions related to viewing and managing weekly schedules
 * and daily events, and enables navigation between different sections.
 */
public class CalendarController implements Initializable {

    @FXML
    private ImageView profileimg;

    @FXML
    private Label time;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button viewScheduleButton;

    @FXML
    private Label weekLabel;

    @FXML
    private TableView<WeeklySchedule> weeklyCalendar;

    @FXML
    private TableColumn<WeeklySchedule, String> mondayColumn;

    @FXML
    private TableColumn<WeeklySchedule, String> tuesdayColumn;

    @FXML
    private TableColumn<WeeklySchedule, String> wednesdayColumn;

    @FXML
    private TableColumn<WeeklySchedule, String> thursdayColumn;

    @FXML
    private TableColumn<WeeklySchedule, String> fridayColumn;

    @FXML
    private TableColumn<WeeklySchedule, String> saturdayColumn;

    @FXML
    private TableColumn<WeeklySchedule, String> sundayColumn;

    @FXML
    private TableView<EventEntry> eventList;

    @FXML
    private TableColumn<EventEntry, String> dateColumn;

    @FXML
    private TableColumn<EventEntry, String> startTimeColumn;

    @FXML
    private TableColumn<EventEntry, String> endTimeColumn;

    @FXML
    private TableColumn<EventEntry, String> eventColumn;

    @FXML
    private TableColumn<EventEntry, String> venueColumn;

    private ActionEvent event;

    private ObservableList<WeeklySchedule> weeklyScheduleData = FXCollections.observableArrayList();
    private ObservableList<EventEntry> eventListData = FXCollections.observableArrayList();
    /**
     * Called to initialise the controller after the FXML is loaded.
     * Sets the current date, initialises table views and populates data.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // time
        time.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        // TableView
        weeklyCalendar.setItems(weeklyScheduleData);
        eventList.setItems(eventListData);

        mondayColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        tuesdayColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        wednesdayColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        thursdayColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        fridayColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        saturdayColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        sundayColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        // Set default date to today
        datePicker.setValue(LocalDate.now());

        updateWeeklyCalendar(LocalDate.now());
        updateEventList(LocalDate.now());
    }
    /**
     * Updates the schedule and events shown based on the selected date.
     *
     * @param event The action event triggered by clicking the view schedule button.
     */
    @FXML
    void viewSchedule(ActionEvent event) {
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate == null) {
            datePicker.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            return;
        }

        datePicker.setStyle("");

        updateWeeklyCalendar(selectedDate);

        updateEventList(selectedDate);
    }
    /**
     * Updates the weekly calendar table based on the selected date.
     * Retrieves events from the database for each day in the week.
     *
     * @param selectedDate The date selected by the user.
     */
    private void updateWeeklyCalendar(LocalDate selectedDate) {
        weeklyScheduleData.clear();

        // calculate the start of the week (Monday)
        LocalDate startOfWeek = selectedDate;
        while (startOfWeek.getDayOfWeek() != java.time.DayOfWeek.MONDAY) {
            startOfWeek = startOfWeek.minusDays(1);
        }
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        // update the week label
        String monthYear = startOfWeek.format(DateTimeFormatter.ofPattern("MMMM yyyy"));
        int weekNumber = startOfWeek.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        String dateRange = startOfWeek.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " to " +
                endOfWeek.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        weekLabel.setText(monthYear + ", Week " + weekNumber + ": " + dateRange);

        // fetch events for the week
        List<List<String>> eventsByDay = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            eventsByDay.add(new ArrayList<>());
            LocalDate currentDay = startOfWeek.plusDays(i);
            eventsByDay.get(i).add(currentDay.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

            // fetch events from CalendarEvents table
            String eventQuery = "SELECT EventName, EventTime, EventType FROM CalendarEvents WHERE EventDate = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(eventQuery)) {
                stmt.setString(1, currentDay.toString());
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String eventName = rs.getString("EventName");
                    String eventTime = rs.getString("EventTime");
                    String eventType = rs.getString("EventType");
                    eventsByDay.get(i).add(eventType + ": " + eventName + " at " + eventTime);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // fetch venue tours from VenueTour table
            String venueTourQuery = "SELECT Institution, Time FROM VenueTour WHERE Date = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(venueTourQuery)) {
                stmt.setString(1, currentDay.toString());
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    eventsByDay.get(i).add("VenueTour: " + rs.getString("Institution") + " at " + rs.getString("Time"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // fetch meeting room bookings from MeetingRoomBooking table
            String meetingRoomQuery = "SELECT RoomName, LengthOfBooking FROM MeetingRoomBooking WHERE BookingDate = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(meetingRoomQuery)) {
                stmt.setString(1, currentDay.toString());
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    eventsByDay.get(i).add("MeetingRoom: " + rs.getString("RoomName") + " - " + rs.getString("LengthOfBooking"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // determine the maximum number of events on any day and add event
        int maxEvents = eventsByDay.stream().mapToInt(List::size).max().orElse(1);

        for (int i = 0; i < maxEvents; i++) {
            String monday = i < eventsByDay.get(0).size() ? eventsByDay.get(0).get(i) : "";
            String tuesday = i < eventsByDay.get(1).size() ? eventsByDay.get(1).get(i) : "";
            String wednesday = i < eventsByDay.get(2).size() ? eventsByDay.get(2).get(i) : "";
            String thursday = i < eventsByDay.get(3).size() ? eventsByDay.get(3).get(i) : "";
            String friday = i < eventsByDay.get(4).size() ? eventsByDay.get(4).get(i) : "";
            String saturday = i < eventsByDay.get(5).size() ? eventsByDay.get(5).get(i) : "";
            String sunday = i < eventsByDay.get(6).size() ? eventsByDay.get(6).get(i) : "";
            weeklyScheduleData.add(new WeeklySchedule(monday, tuesday, wednesday, thursday, friday, saturday, sunday));
        }
    }
    /**
     * Updates the list of events for the selected day.
     *
     * @param selectedDate The selected date.
     */
    private void updateEventList(LocalDate selectedDate) {
        eventListData.clear();

        String eventQuery = "SELECT EventName, EventTime, EventRoomID, EventType FROM CalendarEvents WHERE EventDate = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(eventQuery)) {
            stmt.setString(1, selectedDate.toString());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String eventName = rs.getString("EventName");
                String startTime = rs.getString("EventTime");
                String endTime = calculateEndTime(startTime, "2:00:00"); // Assuming events are 2 hours long
                String eventType = rs.getString("EventType");
                int eventRoomID = rs.getInt("EventRoomID");
                String venue = getRoomName(eventRoomID);
                eventListData.add(new EventEntry(
                        selectedDate.toString(),
                        startTime,
                        endTime,
                        eventType + ": " + eventName,
                        venue != null ? venue : "Room " + eventRoomID,
                        eventType
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String venueTourQuery = "SELECT Institution, Time FROM VenueTour WHERE Date = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(venueTourQuery)) {
            stmt.setString(1, selectedDate.toString());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String institution = rs.getString("Institution");
                String startTime = rs.getString("Time");
                String endTime = calculateEndTime(startTime, "1:00:00");
                eventListData.add(new EventEntry(
                        selectedDate.toString(),
                        startTime,
                        endTime,
                        "VenueTour: " + institution,
                        "N/A",
                        "VenueTour"
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String meetingRoomQuery = "SELECT RoomName, LengthOfBooking, BookingDate FROM MeetingRoomBooking WHERE BookingDate = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(meetingRoomQuery)) {
            stmt.setString(1, selectedDate.toString());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String roomName = rs.getString("RoomName");
                String lengthOfBooking = rs.getString("LengthOfBooking");
                String startTime = "09:00:00"; // Default start time for meeting rooms
                String endTime = calculateEndTime(startTime, lengthOfBooking);
                eventListData.add(new EventEntry(
                        selectedDate.toString(),
                        startTime,
                        endTime,
                        "MeetingRoom: " + roomName,
                        roomName,
                        "MeetingRoom"
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Retrieves the name of a meeting room by its ID.
     *
     * @param roomID The ID of the room.
     * @return The room name or null if not found.
     */
    private String getRoomName(int roomID) {
        String query = "SELECT RoomName FROM MeetingRooms WHERE RoomID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, roomID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("RoomName");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Calculates the end time given a start time and duration.
     *
     * @param startTime The starting time in HH:mm:ss format.
     * @param duration  The duration in HH:mm:ss or a label such as "AllDay".
     * @return The calculated end time in HH:mm:ss format.
     */
    private String calculateEndTime(String startTime, String duration) {
        String[] startParts = startTime.split(":");
        int startHours = Integer.parseInt(startParts[0]);
        int startMinutes = Integer.parseInt(startParts[1]);
        int startSeconds = Integer.parseInt(startParts[2]);

        int durationHours = 0;
        int durationMinutes = 0;
        if (duration.contains(":")) {
            String[] durationParts = duration.split(":");
            durationHours = Integer.parseInt(durationParts[0]);
            durationMinutes = Integer.parseInt(durationParts[1]);
        } else {
            switch (duration) {
                case "1Hour":
                    durationHours = 1;
                    break;
                case "MorningAndAfternoon":
                    durationHours = 6;
                    break;
                case "AllDay":
                    durationHours = 12;
                    break;
                case "AllWeek":
                    durationHours = 24 * 7;
                    break;
                default:
                    durationHours = 1;
            }
        }

        int endHours = startHours + durationHours + (startMinutes + durationMinutes) / 60;
        int endMinutes = (startMinutes + durationMinutes) % 60;
        int endSeconds = startSeconds;

        // Format end time
        return String.format("%02d:%02d:%02d", endHours % 24, endMinutes, endSeconds);
    }

    @FXML
    void goToBooking(ActionEvent event) throws IOException {
        LoginApplication.moveToBooking();
    }

    @FXML
    void goToDashboard(ActionEvent event) throws IOException {
        this.event = event;
        LoginApplication.moveToDashboard();
    }

    @FXML
    void goToProfile(MouseEvent event) throws IOException {
        LoginApplication.moveToProfile();
    }

    @FXML
    void goToReports(ActionEvent event) throws IOException {
        this.event = event;
        LoginApplication.moveToReports();
    }

    @FXML
    void goToFilms(ActionEvent event) throws IOException {
        this.event = event;
        LoginApplication.moveToFilms();
    }

    @FXML
    void goToCalendar(ActionEvent event) throws IOException {
        this.event = event;
        LoginApplication.moveToCalendar();
    }

    @FXML
    void goToAdvertising(ActionEvent event) throws IOException {
        this.event = event;
        LoginApplication.moveToAdvertising();
    }
}