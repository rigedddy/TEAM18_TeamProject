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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for handling all booking-related views and logic within the JavaFX application.
 * This includes bookings for meeting rooms, group bookings, and venue tours.
 */
public class BookingController implements Initializable {

    @FXML
    private ImageView profileimg;

    @FXML
    private Label time;

    // Group meeting fields
    @FXML
    private TextField NumOfPeople;
    @FXML
    private TextField Name;
    @FXML
    private TextField Email;
    @FXML
    private ChoiceBox<String> InstitutionChoice;
    @FXML
    private ChoiceBox<String> groupEvent;

    // Meeting room fields
    @FXML
    private ChoiceBox<String> TimeSlot;
    @FXML
    private TextField MeetingPrice;
    @FXML
    private ChoiceBox<String> RoomName;
    @FXML
    private ChoiceBox<String> ClientName;
    @FXML
    private DatePicker Date;

    // Venue tour fields
    @FXML
    private ChoiceBox<String> institutionTour;
    @FXML
    private TextField studentsTour;
    @FXML
    private TextField timeTour;
    @FXML
    private DatePicker dateTour;

    private ActionEvent event;

    private MeetingRoom meetingRoom;
    private VenueTour venueTour;
    private GroupBooking groupBooking;

    /**
     * Triggers creation of a new venue tour booking.
     *
     * @param event the event triggered by the user
     */
    @FXML
    void createNewTourBooking(ActionEvent event) {
        venueTour.createNewTourBooking();
    }

    /**
     * Triggers creation of a new group booking.
     *
     * @param event the event triggered by the user
     */
    @FXML
    void createNewGroupBooking(ActionEvent event) {
        groupBooking.createNewGroupBooking();
    }

    /**
     * Triggers creation of a new meeting room booking.
     *
     * @param event the event triggered by the user
     */
    @FXML
    void createNewMeetingRoomBooking(ActionEvent event) {
        meetingRoom.createNewMeetingRoomBooking();
    }

    /**
     * Navigates to the dashboard view.
     *
     * @param event the event triggered by the user
     * @throws IOException if FXML loading fails
     */
    @FXML
    void goToDashboard(ActionEvent event) throws IOException {
        LoginApplication.moveToDashboard();
    }

    /**
     * Navigates to the profile view.
     *
     * @param event the event triggered by the user
     * @throws IOException if FXML loading fails
     */
    @FXML
    void goToProfile(MouseEvent event) throws IOException {
        LoginApplication.moveToProfile();
    }

    /**
     * Navigates to the reports view.
     *
     * @param event the event triggered by the user
     * @throws IOException if FXML loading fails
     */
    @FXML
    void goToReports(ActionEvent event) throws IOException {
        LoginApplication.moveToReports();
    }

    /**
     * Navigates to the films view.
     *
     * @param event the event triggered by the user
     * @throws IOException if FXML loading fails
     */
    @FXML
    void goToFilms(ActionEvent event) throws IOException {
        LoginApplication.moveToFilms();
    }

    /**
     * Navigates to the booking view.
     *
     * @param event the event triggered by the user
     * @throws IOException if FXML loading fails
     */
    @FXML
    void goToBooking(ActionEvent event) throws IOException {
        LoginApplication.moveToBooking();
    }

    /**
     * Navigates to the calendar view.
     *
     * @param event the event triggered by the user
     * @throws IOException if FXML loading fails
     */
    @FXML
    void goToCalendar(ActionEvent event) throws IOException {
        this.event = event;
        LoginApplication.moveToCalendar();
    }

    /**
     * Navigates to the advertising view.
     *
     * @param event the event triggered by the user
     * @throws IOException if FXML loading fails
     */
    @FXML
    void goToAdvertising(ActionEvent event) throws IOException {
        this.event = event;
        LoginApplication.moveToAdvertising();
    }

    /**
     * Initialises the controller by setting the current date and preparing instances of booking classes.
     *
     * @param url            built in functionality from initialize interface
     * @param resourceBundle built in functionality from initialize interface
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        time.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        groupBooking = new GroupBooking(NumOfPeople, Name, Email, InstitutionChoice, groupEvent);
        meetingRoom = new MeetingRoom(RoomName, ClientName, TimeSlot, Date, MeetingPrice);
        venueTour = new VenueTour(institutionTour, studentsTour, timeTour, dateTour);
    }
}
