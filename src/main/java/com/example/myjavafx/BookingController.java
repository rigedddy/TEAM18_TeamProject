package com.example.myjavafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class BookingController implements Initializable {

    @FXML
    private ImageView profileimg;

    // group meeting
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

    // meeting room
    @FXML
    private TextField StartTimeMeeting;
    @FXML
    private TextField EndTimeMeeting;
    @FXML
    private TextField MeetingPrice;

    @FXML
    private ChoiceBox<String> InstitutionChoice;

    private final String[] institutionChoices = {"Primary School", "Secondary School", "College", "University"};
    private ActionEvent event;

    // Get the data that was inputted by the user and ensure validation
    @FXML
    void createNewBooking(ActionEvent event) {
        boolean isValid = true;

        String bookingIDString = BookingID.getText().trim();
        String numOfPeopleString = NumOfPeople.getText().trim();
        String nameString = Name.getText().trim();
        String emailString = Email.getText().trim();

        String institutionChoiceString = InstitutionChoice.getValue();

        // Reset border color for all fields
        BookingID.setStyle("");
        NumOfPeople.setStyle("");
        Name.setStyle("");
        Email.setStyle("");

        InstitutionChoice.setStyle("");

        // Validate each field
        if (bookingIDString.isEmpty()) {
            BookingID.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }
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

        // Only create data if all fields are filled
        if (isValid) {
            String[] data = {bookingIDString, numOfPeopleString, nameString, emailString, institutionChoiceString};
            System.out.println("Data registered: " + Arrays.toString(data));
        } else {
            System.out.println("Error: Please fill in all required fields.");
        }
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

        InstitutionChoice.getItems().addAll(institutionChoices);
    }
}
