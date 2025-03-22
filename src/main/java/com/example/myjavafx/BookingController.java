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
    String BookingIDString;
    String NumOfPeopleString;
    String NameString;
    String EmailString;
    @FXML
    private ChoiceBox<String> BookingChoiceBox;
    private final String[] bookingChoices = {"Friend of Lancaster", "Regular GroupBooking"};
    String bookingChoice;
    @FXML
    private ChoiceBox<String> InstitutionChoice;
    String InstitutionChoiceString;
    private final String[] insitutionChoices = {"Primary School", "Secondary School", "College", "University"};
    private ActionEvent event;


    //get the data that was inputted by user, we can export this to sql
    @FXML
    void createNewBooking(ActionEvent event) {
        BookingIDString = BookingID.getText();
        NumOfPeopleString = NumOfPeople.getText();
        NameString = Name.getText();
        EmailString = Email.getText();
        bookingChoice = BookingChoiceBox.getValue();
        InstitutionChoiceString = InstitutionChoice.getValue();
        String[] data = {BookingIDString, NumOfPeopleString, NameString, EmailString, InstitutionChoiceString, bookingChoice};
        System.out.println("Data registered: "+ Arrays.toString(data));

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
        BookingChoiceBox.getItems().addAll(bookingChoices);
        InstitutionChoice.getItems().addAll(insitutionChoices);


    }

}
