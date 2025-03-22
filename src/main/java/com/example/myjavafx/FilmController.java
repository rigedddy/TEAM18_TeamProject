package com.example.myjavafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.Arrays;

public class FilmController {

    @FXML
    private ImageView profileimg;

    @FXML
    private Label time;
    @FXML
    private TextField filmIDTextField;
    @FXML
    private TextField TitleTextField;
    int filmid;
    String registeredFilmID;
    String filmTitle;
    @FXML
    private TextField LicenseTextField;
    @FXML
    private TextField DurationTextField;
    @FXML
    private TextField VenueTextField;
    @FXML
    private TextField DateTextField;

    String license;
    String duration;
    String venue;
    String date;
    private ActionEvent event;


    //get the data that was inputted by user, we can export this to sql
    @FXML
    void createNewFilm(ActionEvent event) {

        registeredFilmID = filmIDTextField.getText();
        filmTitle = TitleTextField.getText();
        license = LicenseTextField.getText();
        duration = DurationTextField.getText();
        venue = VenueTextField.getText();
        date = DateTextField.getText();
        String[] data = {registeredFilmID, filmTitle, license, duration, venue, date};
        System.out.println("the registered data: "+ Arrays.toString(data));
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
    
}
