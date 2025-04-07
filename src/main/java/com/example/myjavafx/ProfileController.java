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

    @FXML
    public void initialize() {
        loadProfileData(Session.currentUsername);
    }

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
    void goToReports(ActionEvent event) throws IOException {
        this.event = event;
        LoginApplication.moveToReports();
    }
    @FXML
    void goToBooking(ActionEvent event) throws IOException {
        LoginApplication.moveToBooking();
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

    @FXML
    void signOut(ActionEvent event) throws IOException {
        LoginApplication.moveToSignIn(); // this must exist in LoginApplication
    }


}
