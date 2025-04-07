package com.example.myjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
import java.util.ResourceBundle;

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

    private ActionEvent event;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set up the TableView columns
        eventColumn.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("eventDate"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("eventTime"));

        // Populate the TableView with upcoming events
        eventView.setItems(getUpcomingEventsFromDatabase());

        // Set the current date in the time label
        time.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    private ObservableList<Dashboard> getUpcomingEventsFromDatabase() {
        ObservableList<Dashboard> eventList = FXCollections.observableArrayList();

        // Query to fetch events on or after the current date
        String query = "SELECT EventName, EventDate, EventTime FROM CalendarEvents WHERE EventDate >= ? ORDER BY EventDate, EventTime";

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