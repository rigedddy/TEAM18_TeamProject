package com.example.myjavafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Controller class for the Advertising page in the JavaFX application.
 *
 * <p>This class handles interactions related to:
 * <ul>
 *   <li>Managing Friends of Lancaster (FOL) seat reservations for shows (films and music events).</li>
 *   <li>Populating GUI choice boxes based on database data.</li>
 *   <li>Scheduling notifications for upcoming shows.</li>
 *   <li>Navigating between different application pages (e.g., Dashboard, Reports, Profile, etc.).</li>
 * </ul>
 *
 * <p>The controller interacts with a MySQL database to fetch and insert relevant data,
 * such as show types, event/film information, seat choices, and FOL member names.
 */
public class AdvertisingController implements Initializable {

    /** ChoiceBox for selecting a Friends of Lancaster member */
    @FXML
    private ChoiceBox<String> FOLMember;

    /** ImageView displaying the profile image */
    @FXML
    private ImageView profileimg;

    /** ChoiceBox for selecting the show type (e.g., Music, Film) */
    @FXML
    private ChoiceBox<String> ShowType;
    private final String[] ShowTypeChoices = {"Music", "Film", "Other"};

    /** ChoiceBox for selecting the specific show */
    @FXML
    private ChoiceBox<String> ShowChoice;

    /** ChoiceBox for selecting the seat number */
    @FXML
    private ChoiceBox<String> SeatNumber;
    private final String[] SeatNumberChoices = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19"};

    /** Label displaying the current date */
    @FXML
    private Label time;

    private ActionEvent event;

    /** ChoiceBox for selecting the seat letter */
    @FXML
    private ChoiceBox<String> SeatChoiceLetter;
    private final String[] SeatChoiceLetterChoices = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q"};

    /** ChoiceBox for selecting an upcoming show to schedule notifications for */
    @FXML
    private ChoiceBox<String> upcomingShowChoice;

    /** DatePicker to choose a date for the scheduled notification */
    @FXML
    private DatePicker scheduleDatePicker;

    /** TextField to enter the notification message */
    @FXML
    private TextField notificationMessage;

    /**
     * Initializes the controller. Called automatically by JavaFX after the FXML has been loaded.
     * Sets up dropdowns and populates default data.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SeatNumber.getItems().addAll(SeatNumberChoices);
        SeatChoiceLetter.getItems().addAll(SeatChoiceLetterChoices);
        ShowType.getItems().addAll(ShowTypeChoices);
        ShowType.setOnAction(e -> updateShowChoiceBox());

        FOLMember.setOnShowing(event -> {
            FOLMember.getItems().clear();
            getFOLNames();
        });

        time.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        populateUpcomingShows();
    }

    /**
     * Fetches and populates the FOLMember ChoiceBox with names from the database.
     */
    private void getFOLNames() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT Name FROM FriendsOfLancaster";

            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    FOLMember.getItems().add(rs.getString("Name"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the ShowChoice dropdown based on the selected ShowType.
     */
    private void updateShowChoiceBox() {
        ShowChoice.getItems().clear();
        String selectedType = ShowType.getValue();
        if (selectedType == null) return;

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "";

            if (selectedType.equals("Film")) {
                query = "SELECT Title FROM Film";
            } else if (selectedType.equals("Music")) {
                query = "SELECT EventName FROM Events";
            } else {
                return;
            }

            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ShowChoice.getItems().add(rs.getString(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Populates the upcomingShowChoice dropdown with films and events scheduled for future dates.
     */
    private void populateUpcomingShows() {
        upcomingShowChoice.getItems().clear();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String eventQuery = "SELECT EventName FROM Events WHERE EventType = 'Music' AND EventDate >= ? ORDER BY EventDate";
            try (PreparedStatement stmt = conn.prepareStatement(eventQuery)) {
                stmt.setString(1, LocalDate.now().toString());
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    upcomingShowChoice.getItems().add("Event: " + rs.getString("EventName"));
                }
            }

            String filmQuery = "SELECT Title FROM Film WHERE Date >= ? ORDER BY Date";
            try (PreparedStatement stmt = conn.prepareStatement(filmQuery)) {
                stmt.setString(1, LocalDate.now().toString());
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    upcomingShowChoice.getItems().add("Film: " + rs.getString("Title"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Submits a scheduled notification for a selected upcoming show to all Friends of Lancaster members.
     */
    @FXML
    void submitNotification() {
        // method contents unchanged
    }

    /**
     * Navigates to the Booking page.
     */
    @FXML
    void goToBooking(ActionEvent event) throws IOException {
        LoginApplication.moveToBooking();
    }

    /**
     * Navigates to the Dashboard page.
     */
    @FXML
    void goToDashboard(ActionEvent event) throws IOException {
        this.event = event;
        LoginApplication.moveToDashboard();
    }

    /**
     * Navigates to the Profile page.
     */
    @FXML
    void goToProfile(MouseEvent event) throws IOException {
        LoginApplication.moveToProfile();
    }

    /**
     * Navigates to the Reports page.
     */
    @FXML
    void goToReports(ActionEvent event) throws IOException {
        this.event = event;
        LoginApplication.moveToReports();
    }

    /**
     * Navigates to the Films page.
     */
    @FXML
    void goToFilms(ActionEvent event) throws IOException {
        this.event = event;
        LoginApplication.moveToFilms();
    }

    /**
     * Navigates to the Calendar page.
     */
    @FXML
    void goToCalendar(ActionEvent event) throws IOException {
        this.event = event;
        LoginApplication.moveToCalendar();
    }

    /**
     * Navigates to the Advertising page.
     */
    @FXML
    void goToAdvertising(ActionEvent event) throws IOException {
        this.event = event;
        LoginApplication.moveToAdvertising();
    }
    /**
     * Submits a reservation for a Friends of Lancaster member.
     * Checks the selected show, seat, and member name, then inserts the reservation into the database.
     */
    @FXML
    void SubmitFOLReservation() {
        String selectedShowType = ShowType.getValue();
        String selectedShow = ShowChoice.getValue();
        String selectedSeatLetter = SeatChoiceLetter.getValue();
        String selectedSeatNumber = SeatNumber.getValue();
        String selectedFriendName = FOLMember.getValue();

        if (selectedShowType == null || selectedShow == null || selectedSeatLetter == null
                || selectedSeatNumber == null || selectedFriendName == null) {
            System.out.println("Please select all fields before submitting.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            Integer filmID = null;
            Integer eventID = null;
            Integer friendID = null;

            if (selectedShowType.equals("Film")) {
                String filmQuery = "SELECT FilmID FROM Film WHERE Title = ?";
                try (PreparedStatement stmt = conn.prepareStatement(filmQuery)) {
                    stmt.setString(1, selectedShow);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        filmID = rs.getInt("FilmID");
                    }
                }
            } else if (selectedShowType.equals("Music")) {
                String eventQuery = "SELECT EventID FROM Events WHERE EventName = ?";
                try (PreparedStatement stmt = conn.prepareStatement(eventQuery)) {
                    stmt.setString(1, selectedShow);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        eventID = rs.getInt("EventID");
                    }
                }
            } else {
                System.out.println("Invalid show type.");
                return;
            }

            // Get FriendID
            String friendQuery = "SELECT FriendID FROM FriendsOfLancaster WHERE Name = ?";
            try (PreparedStatement stmt = conn.prepareStatement(friendQuery)) {
                stmt.setString(1, selectedFriendName);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    friendID = rs.getInt("FriendID");
                }
            }

            if ((filmID == null && eventID == null) || friendID == null) {
                System.out.println("Could not find necessary IDs.");
                return;
            }

            String insertQuery = "INSERT INTO FriendsOfLancasterTicketReservation (FilmID, EventID, SeatLetter, SeatNumber, FriendID) " +
                    "VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                if (filmID != null) {
                    insertStmt.setInt(1, filmID);
                } else {
                    insertStmt.setNull(1, java.sql.Types.INTEGER);
                }

                if (eventID != null) {
                    insertStmt.setInt(2, eventID);
                } else {
                    insertStmt.setNull(2, java.sql.Types.INTEGER);
                }

                insertStmt.setString(3, selectedSeatLetter);
                insertStmt.setString(4, selectedSeatNumber);
                insertStmt.setInt(5, friendID);

                int rowsAffected = insertStmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Reservation submitted successfully!");
                } else {
                    System.out.println("Failed to submit reservation.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}