package com.example.myjavafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ReportsController implements Initializable {

    @FXML
    private ImageView profileimg;

    @FXML
    private ChoiceBox<String> FoLYear;

    @FXML
    private LineChart<String, Number> FOLGraph;

    @FXML
    private Label TotalFOL;

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

        // Load the Friends of Lancaster's section
        loadFOLSection();
    }

    private void loadInstitutionsPieChart() {
        // Populate the pie chart using the Reports class
        reports.populateInstitutionsPieChart(InstitutionsPie);

        // Update the labels
        totalInstitutionsLabel.setText("Total Institutions: " + reports.getTotalInstitutions());
        totalToursLabel.setText("Total Tours: " + reports.getTotalTours());
    }

    private void loadFOLSection() {
        // Populate the ChoiceBox with years
        List<String> years = reports.getFOLYears();
        FoLYear.getItems().addAll(years);

        // Set the default year to the most recent (first in the list, since it's sorted DESC)
        if (!years.isEmpty()) {
            FoLYear.setValue(years.get(0));
            updateFOLGraph(years.get(0));
        }

        // Add a listener to update the graph and label when the year changes
        FoLYear.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateFOLGraph(newValue);
            }
        });
    }

    private void updateFOLGraph(String year) {
        // Update the line chart
        reports.populateFOLGraph(FOLGraph, year);

        // Update the total subscribers label
        int totalSubscribers = reports.getTotalSubscribers(year);
        TotalFOL.setText("Total Subscribers: " + totalSubscribers);
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