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
import java.time.LocalDate;
import java.util.Arrays;
import java.util.ResourceBundle;

public class BookingController implements Initializable {

    @FXML
    private ImageView profileimg;

    // Group meeting fields
    @FXML
    private Label time;
    @FXML
    private TextField BookingID;
    @FXML
    private TextField NumOfPeople;
    @FXML
    private TextField Name;
    @FXML
    private TextField Email;
    @FXML
    private ChoiceBox<String> InstitutionChoice;
    @FXML
    private DatePicker DateGroupBooking;

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

    private final String[] institutionChoices = {"Primary School", "Secondary School", "College", "University"};
    private ActionEvent event;

    // Instance of MeetingRoom class
    private MeetingRoom meetingRoom;

    // Get the data that was inputted by the user and ensure validation for group booking
    @FXML
    void createNewBooking(ActionEvent event) {
        boolean isValid = true;

        String numOfPeopleString = NumOfPeople.getText().trim();
        String nameString = Name.getText().trim();
        String emailString = Email.getText().trim();
        String institutionChoiceString = InstitutionChoice.getValue();
        LocalDate startDate = DateGroupBooking.getValue();
        String DateForGroup = startDate != null ? startDate.toString() : null;

        // Reset border color for all fields
        NumOfPeople.setStyle("");
        Name.setStyle("");
        Email.setStyle("");
        InstitutionChoice.setStyle("");

        // Validate each field
        if (numOfPeopleString.isEmpty()) {
            NumOfPeople.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }
        if (nameString.isEmpty()) {
            Name.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }
        if (emailString.isEmpty()) {
            Email.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }
        if (institutionChoiceString == null) {
            InstitutionChoice.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }
        if (DateGroupBooking.getValue() == null) {
            isValid = false;
        }

        // Only create data if all fields are filled
        if (isValid) {
            String[] data = {numOfPeopleString, nameString, emailString, institutionChoiceString, DateForGroup};
            System.out.println("Data registered: " + Arrays.toString(data));
        } else {
            System.out.println("Error: Please fill in all required fields.");
        }
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
        // Initialize group booking components
        InstitutionChoice.getItems().addAll(institutionChoices);

        // Initialize the MeetingRoom instance and pass the required UI components
        meetingRoom = new MeetingRoom(RoomName, ClientName, TimeSlot, Date, MeetingPrice);
    }
}