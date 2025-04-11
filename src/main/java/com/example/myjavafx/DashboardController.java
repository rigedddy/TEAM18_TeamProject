package com.example.myjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the Dashboard view.
 * Handles UI initialisation, loading upcoming events, and displaying ticket sales data
 * for both events and films based on user selection.
 */
public class DashboardController implements Initializable {

    @FXML
    private Label time;

    @FXML
    private TableColumn<Dashboard, String> dateColumn;

    @FXML
    private TableColumn<Dashboard, String> eventColumn;

    @FXML
    private TableColumn<Dashboard, String> timeColumn;

    @FXML
    private TableView<Dashboard> eventView;

    @FXML
    private ImageView profileimg;

    @FXML
    private LineChart<String, Number> ticketsGraph;

    @FXML
    private ChoiceBox<Integer> chooseYear;

    @FXML
    private ChoiceBox<String> eventType;

    private ActionEvent event;
    /**
     * Initialises the dashboard by populating tables and setting up event handlers.
     *
     * @param url            The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources used to localise the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        eventColumn.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("eventDate"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("eventTime"));

        // tableView upcoming events
        eventView.setItems(getUpcomingEventsFromDatabase());

        // set the current date in the time label
        time.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        // set up the ChoiceBox items for eventType
        eventType.getItems().addAll("Events", "Films");

        // set up the ChoiceBox items for chooseYear (only 2023 and 2024)
        List<Integer> years = new ArrayList<>();
        years.add(2023);
        years.add(2024);
        chooseYear.getItems().addAll(years);

        // set up the LineChart
        ticketsGraph.setTitle("Ticket Sales Per Event/Film");
        ticketsGraph.getXAxis().setLabel(""); // X-axis label removed
        ticketsGraph.getYAxis().setLabel("Tickets Sold");
        ticketsGraph.setLegendSide(javafx.geometry.Side.TOP); // Legend at the top

        chooseYear.valueProperty().addListener((obs, oldValue, newValue) -> updateTicketsGraph());
        eventType.valueProperty().addListener((obs, oldValue, newValue) -> updateTicketsGraph());

        // default year and event type
        if (chooseYear.getValue() == null) {
            chooseYear.setValue(2024);
        }
        if (eventType.getValue() == null) {
            eventType.setValue("Films"); // Default to Films
        }

        // initial population of the graph
        updateTicketsGraph();
    }
    /**
     * Fetches upcoming events from the database and returns them as an observable list.
     *
     * @return List of Dashboard entries for events happening today or in the future.
     */
    private ObservableList<Dashboard> getUpcomingEventsFromDatabase() {
        ObservableList<Dashboard> eventList = FXCollections.observableArrayList();

        // query to fetch events on or after the current date
        String query = "SELECT EventName, EventDate, EventTime FROM Events WHERE EventDate >= ? ORDER BY EventDate, EventTime";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            // Set the current date as the parameter
            stmt.setString(1, LocalDate.now().toString());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                eventList.add(new Dashboard(
                        rs.getString("EventName"),
                        rs.getString("EventDate"),
                        rs.getString("EventTime")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventList;
    }
    /**
     * Updates the line chart with ticket sales data based on the selected year and event type.
     */
    private void updateTicketsGraph() {
        // clear the existing data in the graph
        ticketsGraph.getData().clear();

        // get the selected year and event type
        Integer selectedYear = chooseYear.getValue();
        if (selectedYear == null) {
            System.out.println("No year selected.");
            return; // No year selected, do nothing
        }
        String selectedType = eventType.getValue();
        if (selectedType == null) {
            System.out.println("No event type selected.");
            return; // No type selected, do nothing
        }

        // create a new series for the graph
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Tickets Sold in " + selectedYear);

        if (selectedType.equals("Events")) {
            // Fetch ticket sales for events in the selected year
            String query = "SELECT e.EventName, COUNT(t.TicketSaleID) as TicketsSold " +
                    "FROM Events e " +
                    "LEFT JOIN TicketSales t ON e.EventID = t.EventID " +
                    "WHERE YEAR(e.EventDate) = ? " +
                    "GROUP BY e.EventID, e.EventName";

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, selectedYear);
                ResultSet rs = stmt.executeQuery();

                int dataCount = 0;
                while (rs.next()) {
                    String eventName = rs.getString("EventName");
                    int ticketsSold = rs.getInt("TicketsSold");
                    //System.out.println("Event: " + eventName + ", Tickets Sold: " + ticketsSold);
                    series.getData().add(new XYChart.Data<>(eventName, ticketsSold));
                    dataCount++;
                }
                System.out.println("Total events with ticket sales in " + selectedYear + ": " + dataCount);
            } catch (SQLException e) {
                System.err.println("SQL Error: " + e.getMessage());
                e.printStackTrace();
            }
        } else if (selectedType.equals("Films")) {
            // fetch ticket sales for films in the selected year
            String query = "SELECT f.Title, f.TicketsSold " +
                    "FROM Film f " +
                    "WHERE YEAR(f.Date) = ?";

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, selectedYear);
                ResultSet rs = stmt.executeQuery();

                int dataCount = 0;
                while (rs.next()) {
                    String filmTitle = rs.getString("Title");
                    int ticketsSold = rs.getInt("TicketsSold");
                    //System.out.println("Film: " + filmTitle + ", Tickets Sold: " + ticketsSold);
                    series.getData().add(new XYChart.Data<>(filmTitle, ticketsSold));
                    dataCount++;
                }
                System.out.println("Total films with ticket sales in " + selectedYear + ": " + dataCount);
            } catch (SQLException e) {
                System.err.println("SQL Error: " + e.getMessage());
                e.printStackTrace();
            }
        }

        // add the series to the graph
        ticketsGraph.getData().add(series);
    }

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
    void goToAdvertising(ActionEvent event) throws IOException {
        this.event = event;
        LoginApplication.moveToAdvertising();
    }
}