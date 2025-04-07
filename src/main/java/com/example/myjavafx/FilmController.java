package com.example.myjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class FilmController {

    @FXML
    private ImageView profileimg;

    @FXML
    private Label time;

    @FXML
    private TextField filmNameTextField;

    @FXML
    private TextField LicenseTextField;
    @FXML
    private TextField StartTimeField;

    @FXML
    private TextField EndTimeField;

    @FXML
    private TextField DateTextField;

    @FXML
    private TableView<Film> tableView;
    @FXML
    private TableView<EmptySpace> EmptySpace;

    @FXML
    private TableColumn<EmptySpace, String> StartTime2;
    @FXML
    private TableColumn<EmptySpace, String> EndTime2;
    @FXML
    private TableColumn<EmptySpace, String> Date2;
    @FXML
    private TableColumn<EmptySpace, String> Venue;

    @FXML
    private TableColumn<Film, String> FilmName;
    @FXML
    private TableColumn<Film, String> StartTime;
    @FXML
    private TableColumn<Film, String> EndTime;
    @FXML
    private TableColumn<Film, String> Date;

    private ObservableList<Film> getFilmsFromDatabase() {
        ObservableList<Film> filmList = FXCollections.observableArrayList();

        String query = "SELECT Title, StartTime, EndTime, Date FROM Film";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                filmList.add(new Film(
                        rs.getString("Title"),
                        rs.getString("StartTime"),
                        rs.getString("EndTime"),
                        rs.getString("Date")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filmList;
    }

    // ObservableList for EmptySpace data
    private ObservableList<EmptySpace> emptySpaceList;

    private ObservableList<EmptySpace> getEmptySpacesFromDatabase() {
        emptySpaceList = FXCollections.observableArrayList();
        String query = "SELECT StartTime, EndTime, Date, Venue FROM EmptyCalendarSpaces WHERE Venue = 'Main Hall'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                emptySpaceList.add(new EmptySpace(
                        rs.getString("StartTime"),
                        rs.getString("EndTime"),
                        rs.getString("Date"),
                        rs.getString("Venue")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return emptySpaceList;
    }

    @FXML
    public void initialize() {
        // time
        time.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        // Set Film table view cell value factories
        FilmName.setCellValueFactory(new PropertyValueFactory<>("Title"));
        StartTime.setCellValueFactory(new PropertyValueFactory<>("StartTime"));
        EndTime.setCellValueFactory(new PropertyValueFactory<>("EndTime"));
        Date.setCellValueFactory(new PropertyValueFactory<>("Date"));

        // Set EmptySpace table view cell value factories
        StartTime2.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        EndTime2.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        Date2.setCellValueFactory(new PropertyValueFactory<>("date"));
        Venue.setCellValueFactory(new PropertyValueFactory<>("venue"));

        // Set items for both tables
        tableView.setItems(getFilmsFromDatabase());
        EmptySpace.setItems(getEmptySpacesFromDatabase());
    }

    @FXML
    void createNewFilm(ActionEvent event) {
        boolean isValid = true;

        String registeredFilmName = filmNameTextField.getText().trim();
        String StartTime = StartTimeField.getText().trim();
        String license = LicenseTextField.getText().trim();
        String EndTime = EndTimeField.getText().trim();
        String date = DateTextField.getText().trim();

        // Reset text field styles
        filmNameTextField.setStyle("");
        StartTimeField.setStyle("");
        LicenseTextField.setStyle("");
        EndTimeField.setStyle("");
        DateTextField.setStyle("");

        // Validate each field
        if (registeredFilmName.isEmpty()) {
            filmNameTextField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }
        if (StartTime.isEmpty()) {
            StartTimeField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }
        if (license.isEmpty()) {
            LicenseTextField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }
        if (EndTime.isEmpty()) {
            EndTimeField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }
        if (date.isEmpty()) {
            DateTextField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }

        if (isValid) {
            String[] data = {registeredFilmName, StartTime, license, EndTime, date};
            System.out.println("The registered data: " + Arrays.toString(data));

            String insertSQL = "INSERT INTO Film (Title, StartTime, LicenseDuration, EndTime, Date) VALUES (?, ?, ?, ?, ?)";

            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

                preparedStatement.setString(1, registeredFilmName);
                preparedStatement.setString(2, StartTime);
                preparedStatement.setString(3, license);
                preparedStatement.setString(4, EndTime);
                preparedStatement.setString(5, date);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Film registered successfully.");
                } else {
                    System.out.println("Error: Unable to register the film.");
                }

            } catch (SQLException e) {
                System.out.println("Error inserting data into database: " + e.getMessage());
            }
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

        LoginApplication.moveToCalendar();
    }

    @FXML
    void goToAdvertising(ActionEvent event) throws IOException {

        LoginApplication.moveToAdvertising();
    }
}
