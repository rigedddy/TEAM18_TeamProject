package com.example.myjavafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class FilmController {

    @FXML
    private ImageView profileimg;

    @FXML
    private Label time;
    @FXML
    private TextField filmIDTextField;
    int filmid;
    String registeredFilmID;





    @FXML
    void createNewFilm(ActionEvent event) {
        // Get the text from the Film ID field
        registeredFilmID = filmIDTextField.getText();

        // Print to console (for debugging)
        System.out.println("New Film Scheduled with ID: " + registeredFilmID.replaceAll("FilmID", ""));
    }
    @FXML
    void RegisterFilmID(ActionEvent event) {
        //vfhbdk
    }
    @FXML
    void goToDashboard(ActionEvent event) throws IOException {
        LoginApplication.moveToDashboard();
    }

    @FXML
    void goToProfile(MouseEvent event) throws IOException {
        System.out.println("clicked");
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


}
