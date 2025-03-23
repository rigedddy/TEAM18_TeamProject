package com.example.myjavafx;

import javafx.animation.Animation;
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


    @FXML
    void createNewFilm(ActionEvent event) {
        boolean isValid = true;

        String registeredFilmID = filmIDTextField.getText().trim();
        String filmTitle = TitleTextField.getText().trim();
        String license = LicenseTextField.getText().trim();
        String duration = DurationTextField.getText().trim();
        String venue = VenueTextField.getText().trim();
        String date = DateTextField.getText().trim();


        filmIDTextField.setStyle("");
        TitleTextField.setStyle("");
        LicenseTextField.setStyle("");
        DurationTextField.setStyle("");
        VenueTextField.setStyle("");
        DateTextField.setStyle("");

        // Validate each field
        if (registeredFilmID.isEmpty()) {
            filmIDTextField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }
        if (filmTitle.isEmpty()) {
            TitleTextField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }
        if (license.isEmpty()) {
            LicenseTextField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }
        if (duration.isEmpty()) {
            DurationTextField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }
        if (venue.isEmpty()) {
            VenueTextField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }
        if (date.isEmpty()) {
            DateTextField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }


        if (isValid) {
            String[] data = {registeredFilmID, filmTitle, license, duration, venue, date};
            System.out.println("The registered data: " + Arrays.toString(data));
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
    
}
