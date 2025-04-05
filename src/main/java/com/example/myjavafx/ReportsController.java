package com.example.myjavafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ReportsController implements Initializable {

    @FXML
    private ImageView profileimg;

    @FXML
    private ChoiceBox<?> FoLYear;

    @FXML
    private PieChart InstitutionsPie;

    @FXML
    private Label totalInstitutionsLabel;

    @FXML
    private Label totalToursLabel;

    @FXML
    private Label time;
    private ActionEvent event;

    private Reports reports; // Instance of the Reports class

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the Reports class
        reports = new Reports();

        // Load the pie chart and update labels
        loadInstitutionsPieChart();
    }

    private void loadInstitutionsPieChart() {
        // Populate the pie chart using the Reports class
        reports.populateInstitutionsPieChart(InstitutionsPie);

        // Update the labels
        totalInstitutionsLabel.setText("Total Institutions: " + reports.getTotalInstitutions());
        totalToursLabel.setText("Total Tours: " + reports.getTotalTours());
    }

    @FXML
    void goToBooking(ActionEvent event) throws IOException {
        LoginApplication.moveToBooking();
    }

    @FXML
    void goToDashboard(ActionEvent event) throws IOException {
        this.event = event;
        LoginApplication.moveToDashboard();
    }

    @FXML
    void goToProfile(MouseEvent event) throws IOException {
        LoginApplication.moveToProfile();
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
    void goToAdvertising(ActionEvent event) throws IOException {
        this.event = event;
        LoginApplication.moveToAdvertising();
    }
}