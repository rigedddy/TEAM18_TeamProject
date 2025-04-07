package com.example.myjavafx;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class VenueTour {

    private ChoiceBox<String> institutionTour;
    private TextField studentsTour;
    private TextField timeTour;
    private DatePicker dateTour;

    private final String[] institutionChoices = {"Primary School", "Secondary School", "College", "University"};

    public VenueTour(ChoiceBox<String> institutionTour, TextField studentsTour, TextField timeTour, DatePicker dateTour) {
        this.institutionTour = institutionTour;
        this.studentsTour = studentsTour;
        this.timeTour = timeTour;
        this.dateTour = dateTour;

        initializeChoiceBox();
    }

    private void initializeChoiceBox() {
        institutionTour.getItems().addAll(institutionChoices);
    }

    // method to create a new venue tour booking
    public void createNewTourBooking() {
        try {
            String institution = institutionTour.getValue() != null ? institutionTour.getValue().strip() : null;
            String numberOfPeople = studentsTour.getText() != null ? studentsTour.getText().trim() : null;
            String time = timeTour.getText() != null ? timeTour.getText().trim() : null;
            LocalDate dateValue = dateTour.getValue();
            String date = dateValue != null ? dateValue.toString() : null;

            institutionTour.setStyle("");
            studentsTour.setStyle("");
            timeTour.setStyle("");
            dateTour.setStyle("");

            // Validate inputs
            boolean isValid = true;

            if (institution == null || institution.isEmpty()) {
                institutionTour.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                isValid = false;
            }
            if (numberOfPeople == null || numberOfPeople.isEmpty()) {
                studentsTour.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                isValid = false;
            }
            if (time == null || time.isEmpty()) {
                timeTour.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                isValid = false;
            }
            if (date == null) {
                dateTour.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                isValid = false;
            }

            // If all fields are valid, proceed with booking
            if (isValid) {
                // Log the data for debugging
                String[] data = {institution, numberOfPeople, time, date};
                System.out.println("Venue Tour Data registered: " + java.util.Arrays.toString(data));

                // Insert into the VenueTour table
                String insertQuery = "INSERT INTO VenueTour (Date, Time, numberOfPeople, Institution) VALUES (?, ?, ?, ?)";
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {

                    pstmt.setString(1, date);
                    pstmt.setString(2, time);
                    pstmt.setString(3, numberOfPeople);
                    pstmt.setString(4, institution);

                    int rowsInserted = pstmt.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("Venue tour booking inserted successfully!");
                    } else {
                        System.out.println("No venue tour booking was inserted.");
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Error inserting venue tour booking: " + e.getMessage());
                }
            } else {
                System.out.println("Please fill in all required fields for Venue Tour booking.");
            }
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }
}