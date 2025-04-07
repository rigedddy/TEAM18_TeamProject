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
    private ChoiceBox<String> groupEvent; // Updated to ChoiceBox<String>

    // Meeting room fields (to be passed to MeetingRoom class)
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

    // Venue tour fields (to be passed to VenueTour class)
    @FXML
    private ChoiceBox<String> institutionTour;
    @FXML
    private TextField studentsTour;
    @FXML
    private TextField timeTour;
    @FXML
    private DatePicker dateTour;

    private ActionEvent event;

    // Instance of MeetingRoom class
    private MeetingRoom meetingRoom;

    // Instance of VenueTour class
    private VenueTour venueTour;

    // Instance of GroupBooking class
    private GroupBooking groupBooking;

    // Delegate venue tour booking creation to the VenueTour class
    @FXML
    void createNewTourBooking(ActionEvent event) {
        venueTour.createNewTourBooking();
    }

    // Delegate group booking creation to the GroupBooking class
    @FXML
    void createNewGroupBooking(ActionEvent event) {
        groupBooking.createNewGroupBooking();
    }

    // Delegate meeting room booking creation to the MeetingRoom class
    @FXML
    void createNewMeetingRoomBooking(ActionEvent event) {
        meetingRoom.createNewMeetingRoomBooking();
    }



    @FXML
    void goToDashboard(ActionEvent event) throws IOException {
        LoginApplication.moveToDashboard();
    }

    @FXML
    void goToProfile(MouseEvent event) throws IOException {
        LoginApplication.moveToProfile();
    }

    @FXML
    void goToReports(ActionEvent event) throws IOException {
        LoginApplication.moveToReports();
    }

    @FXML
    void goToFilms(ActionEvent event) throws IOException {
        LoginApplication.moveToFilms();
    }

    @FXML
    void goToBooking(ActionEvent event) throws IOException {
        LoginApplication.moveToBooking();
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // time
        time.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        // Initialize the GroupBooking instance and pass the required UI components
        groupBooking = new GroupBooking(NumOfPeople, Name, Email, InstitutionChoice, groupEvent);

        // Initialize the MeetingRoom instance and pass the required UI components
        meetingRoom = new MeetingRoom(RoomName, ClientName, TimeSlot, Date, MeetingPrice);

        // Initialize the VenueTour instance and pass the required UI components
        venueTour = new VenueTour(institutionTour, studentsTour, timeTour, dateTour);
    }
}