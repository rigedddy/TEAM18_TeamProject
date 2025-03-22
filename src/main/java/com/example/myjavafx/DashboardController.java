package com.example.myjavafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class DashboardController {

    @FXML
    private Label time;

    @FXML
    private ImageView profileimg;
    private ActionEvent event;

    @FXML
    void goToDashboard(ActionEvent event) throws IOException {
        this.event = event;
        LoginApplication.moveToDashboard();
    }

    @FXML
    void goToProfile(MouseEvent event) throws IOException {
        System.out.println("clicked");
        LoginApplication.moveToProfile();

    }
    @FXML
    void goToBooking(ActionEvent event) throws IOException {
        LoginApplication.moveToBooking();
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
    void goToMarketing(ActionEvent event) throws IOException {
        this.event = event;
        LoginApplication.moveToMarketing();
    }


}
