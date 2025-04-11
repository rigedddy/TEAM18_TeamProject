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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.net.URL;
import java.util.ResourceBundle;
import java.io.IOException;
import java.util.List;

/**
 * Controller class for the Reports.fxml UI.
 * Displays various data-driven marketing metrics such as subscriber stats, tour counts,
 * film ticket revenue, and general revenue/cost/profit summaries.
 */
public class ReportsController implements Initializable {

    @FXML private ImageView profileimg;

    @FXML private ChoiceBox<String> FoLYear;
    @FXML private LineChart<String, Number> FOLGraph;
    @FXML private Label TotalFOL;

    @FXML private PieChart InstitutionsPie;
    @FXML private Label totalInstitutionsLabel;
    @FXML private Label totalToursLabel;

    @FXML private LineChart<String, Number> FilmTicketGraph;
    @FXML private ChoiceBox<String> FilmTicketYear;
    @FXML private Label totalFilms;
    @FXML private Label revenueTickets;

    @FXML private LineChart<String, Number> generalGraph;
    @FXML private Label totalCostsLabel;
    @FXML private Label totalProfitsLabel;
    @FXML private Label totalRevenueLabel;

    @FXML private Label time;

    private ActionEvent event;
    private Reports reports;

    /**
     * Initializes the controller after the root element has been completely processed.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        time.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        reports = new Reports();

        loadMarketingGeneralMetrics();
        loadInstitutionsPieChart();
        loadFOLSection();
        loadFilmTicketSection();
    }

    /**
     * Loads general financial metrics including revenue, costs, and profits.
     */
    private void loadMarketingGeneralMetrics() {
        double totalRevenue = reports.getTotalRevenue();
        double totalCosts = reports.getTotalCosts();
        double totalProfits = reports.getTotalProfits();

        totalRevenueLabel.setText("Total Revenue: £" + String.format("%.2f", totalRevenue));
        totalCostsLabel.setText("Total Costs: £" + String.format("%.2f", totalCosts));
        totalProfitsLabel.setText("Total Profits: £" + String.format("%.2f", totalProfits));

        reports.populateGeneralGraph(generalGraph);
    }

    /**
     * Loads institution-related booking data into a pie chart.
     */
    private void loadInstitutionsPieChart() {
        reports.populateInstitutionsPieChart(InstitutionsPie);

        totalInstitutionsLabel.setText("Total Institutions: " + reports.getTotalInstitutions());
        totalToursLabel.setText("Total Tours: " + reports.getTotalTours());
    }

    /**
     * Loads Friends of Lancaster subscriber data into the graph and dropdown.
     */
    private void loadFOLSection() {
        List<String> years = reports.getFOLYears();
        FoLYear.getItems().addAll(years);

        if (!years.isEmpty()) {
            FoLYear.setValue(years.get(0));
            updateFOLGraph(years.get(0));
        }

        FoLYear.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateFOLGraph(newValue);
            }
        });
    }

    /**
     * Updates the subscriber line chart and label for the selected year.
     */
    private void updateFOLGraph(String year) {
        reports.populateFOLGraph(FOLGraph, year);
        int totalSubscribers = reports.getTotalSubscribers(year);
        TotalFOL.setText("Total Subscribers: " + totalSubscribers);
    }

    /**
     * Loads the film ticket graph and year selector.
     */
    private void loadFilmTicketSection() {
        List<String> years = reports.getFilmTicketYears();
        FilmTicketYear.getItems().addAll(years);

        if (!years.isEmpty()) {
            FilmTicketYear.setValue("2024");
            updateFilmTicketGraph("2024");
        }

        FilmTicketYear.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateFilmTicketGraph(newValue);
            }
        });
    }

    /**
     * Updates the film ticket graph and financial summary for the selected year.
     */
    private void updateFilmTicketGraph(String year) {
        reports.populateFilmTicketGraph(FilmTicketGraph, year);

        int totalFilmsCount = reports.getTotalFilms(year);
        double totalRevenue = reports.getTotalTicketRevenue(year);
        totalFilms.setText("Total Films Shown: " + totalFilmsCount);
        revenueTickets.setText("Total Revenue from Tickets: £" + String.format("%.2f", totalRevenue));
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
