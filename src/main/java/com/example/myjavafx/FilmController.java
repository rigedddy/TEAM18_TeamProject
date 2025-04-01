package com.example.myjavafx;

import javafx.animation.Animation;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Arrays;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.ResultSet;


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
    private TableColumn<Film, String> FilmName;

    @FXML
    private TableColumn<Film, String> StartTime;
    @FXML
    private TableColumn<Film, String> EndTime;
    @FXML
    private TableColumn<Film, String> Date;
    private ActionEvent event;

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
    @FXML
    public void initialize() {
        FilmName.setCellValueFactory(new PropertyValueFactory<>("Title"));
        StartTime.setCellValueFactory(new PropertyValueFactory<>("StartTime"));
        EndTime.setCellValueFactory(new PropertyValueFactory<>("EndTime"));
        Date.setCellValueFactory(new PropertyValueFactory<>("Date"));
        tableView.setItems(getFilmsFromDatabase());
    }


    @FXML
    void createNewFilm(ActionEvent event) {
        boolean isValid = true;

        String registeredFilmName = filmNameTextField.getText().trim();
        String StartTime = StartTimeField.getText().trim();
        String license = LicenseTextField.getText().trim();
        String EndTime = EndTimeField.getText().trim();
        String date = DateTextField.getText().trim();


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
