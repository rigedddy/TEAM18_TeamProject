package com.example.myjavafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Controller class for the Profile screen.
 * Handles loading user profile data and navigating to other scenes.
 */
public class ProfileController {

    @FXML
    private Label employeeIdLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private ImageView profileimg;

    @FXML
    private ImageView profileimg1;

    @FXML
    private Label time;

    private ActionEvent event;

    /**
     * Called automatically when the FXML is loaded.
     * Loads profile data and sets the current date on the UI.
     */
    @FXML
    public void initialize() {
        loadProfileData(Session.currentUsername);
        time.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    /**
     * Loads the profile information for the given username from the database.
     *
     * @param username The username of the currently logged-in user.
     */
    private void loadProfileData(String username) {
        String query = "SELECT id, username, email FROM LoginDetails WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                employeeIdLabel.setText(rs.getString("id"));
                nameLabel.setText(rs.getString("username"));
                emailLabel.setText(rs.getString("email"));
            }

        } catch (SQLException e) {
            System.out.println("Error loading profile: " + e.getMessage());
        }
    }

    /**
     * Navigates to the Dashboard screen.
     *
     * @param event Triggered by a button click.
     * @throws IOException If FXML loading fails.
     */
    @FXML
    void goToDashboard(ActionEvent event) throws IOException {
        this.event = event;
        LoginApplication.moveToDashboard();
    }

    /**
     * Navigates to the Profile screen.
     *
     * @param event Triggered by clicking the profile image.
     * @throws IOException If FXML loading fails.
     */
    @FXML
    void goToProfile(MouseEvent event) throws IOException {
        System.out.println("clicked");
        LoginApplication.moveToProfile();
    }

    /**
     * Navigates to the Reports screen.
     *
     * @param event Triggered by a button click.
     * @throws IOException If FXML loading fails.
     */
    @FXML
    void goToReports(ActionEvent event) throws IOException {
        this.event = event;
        LoginApplication.moveToReports();
    }

    /**
     * Navigates to the Booking screen.
     *
     * @param event Triggered by a button click.
     * @throws IOException If FXML loading fails.
     */
    @FXML
    void goToBooking(ActionEvent event) throws IOException {
        LoginApplication.moveToBooking();
    }

    /**
     * Navigates to the Films screen.
     *
     * @param event Triggered by a button click.
     * @throws IOException If FXML loading fails.
     */
    @FXML
    void goToFilms(ActionEvent event) throws IOException {
        this.event = event;
        LoginApplication.moveToFilms();
    }

    /**
     * Navigates to the Calendar screen.
     *
     * @param event Triggered by a button click.
     * @throws IOException If FXML loading fails.
     */
    @FXML
    void goToCalendar(ActionEvent event) throws IOException {
        this.event = event;
        LoginApplication.moveToCalendar();
    }

    /**
     * Navigates to the Advertising screen.
     *
     * @param event Triggered by a button click.
     * @throws IOException If FXML loading fails.
     */
    @FXML
    void goToAdvertising(ActionEvent event) throws IOException {
        this.event = event;
        LoginApplication.moveToAdvertising();
    }

    /**
     * Signs out the current user and returns to the sign-in screen.
     *
     * @param event Triggered by a button click.
     * @throws IOException If FXML loading fails.
     */
    @FXML
    void signOut(ActionEvent event) throws IOException {
        LoginApplication.moveToSignIn();
    }
}
