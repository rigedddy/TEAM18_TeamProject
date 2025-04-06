package com.example.myjavafx;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GroupBooking {

    private TextField numOfPeople;
    private TextField name;
    private TextField email;
    private ChoiceBox<String> institutionChoice;
    private ChoiceBox<String> groupEvent;

    private final String[] institutionChoices = {"NULL", "Primary School", "Secondary School", "College", "University"};

    // Constructor
    public GroupBooking(TextField numOfPeople, TextField name, TextField email,
                        ChoiceBox<String> institutionChoice, ChoiceBox<String> groupEvent) {
        this.numOfPeople = numOfPeople;
        this.name = name;
        this.email = email;
        this.institutionChoice = institutionChoice;
        this.groupEvent = groupEvent;

        // Populate the ChoiceBox components
        initializeChoiceBoxes();
    }

    // Initialize the ChoiceBox components
    private void initializeChoiceBoxes() {
        institutionChoice.getItems().addAll(institutionChoices);
        // Populate groupEvent with event names from the Events table
        fetchEventNames();
    }

    // Fetch event names from the Events table
    private void fetchEventNames() {
        String query = "SELECT EventName FROM Events";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             java.sql.ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                groupEvent.getItems().add(rs.getString("EventName"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error fetching event names: " + e.getMessage());
        }
    }

    // Method to create a new group booking
    public void createNewGroupBooking() {
        try {
            // Get and validate input values
            String numOfPeopleString = numOfPeople.getText() != null ? numOfPeople.getText().trim() : null;
            String nameString = name.getText() != null ? name.getText().trim() : null;
            String emailString = email.getText() != null ? email.getText().trim() : null;
            String institutionChoiceString = institutionChoice.getValue();
            String eventName = groupEvent.getValue();

            // Reset border styles for validation feedback
            numOfPeople.setStyle("");
            name.setStyle("");
            email.setStyle("");
            institutionChoice.setStyle("");
            groupEvent.setStyle("");

            // Validate inputs
            boolean isValid = true;

            if (numOfPeopleString == null || numOfPeopleString.isEmpty()) {
                numOfPeople.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                isValid = false;
            }
            if (nameString == null || nameString.isEmpty()) {
                name.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                isValid = false;
            }
            if (emailString == null || emailString.isEmpty()) {
                email.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                isValid = false;
            }
            if (institutionChoiceString == null) {
                institutionChoice.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                isValid = false;
            }
            if (eventName == null || eventName.isEmpty()) {
                groupEvent.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                isValid = false;
            }

            // If all fields are valid, proceed with booking
            if (isValid) {
                // Log the data for debugging
                String[] data = {numOfPeopleString, nameString, emailString, institutionChoiceString, eventName};
                System.out.println("Group Booking Data registered: " + java.util.Arrays.toString(data));

                // Fetch the FilmID based on the selected event name
                Integer filmID = getFilmIDForEvent(eventName);

                // Insert into the GroupBooking table
                String insertQuery = "INSERT INTO GroupBookings (PrimaryContactName, PrimaryContactEmail, NumberOfPeople, status, FilmID, Institution) VALUES (?, ?, ?, ?, ?, ?)";
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {

                    pstmt.setString(1, nameString);
                    pstmt.setString(2, emailString);
                    pstmt.setInt(3, Integer.parseInt(numOfPeopleString));
                    pstmt.setString(4, "pending"); // Default status
                    if (filmID != null) {
                        pstmt.setInt(5, filmID);
                    } else {
                        pstmt.setNull(5, java.sql.Types.INTEGER);
                    }
                    pstmt.setString(6, institutionChoiceString.equals("NULL") ? null : institutionChoiceString);

                    int rowsInserted = pstmt.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("Group booking inserted successfully!");
                    } else {
                        System.out.println("No group booking was inserted.");
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Error inserting group booking: " + e.getMessage());
                }
            } else {
                System.out.println("Please fill in all required fields for Group Booking.");
            }
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    // Method to fetch the FilmID based on the event name
    private Integer getFilmIDForEvent(String eventName) {
        String query = "SELECT EventID FROM Events WHERE EventName = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, eventName);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("EventID");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error fetching FilmID for event: " + e.getMessage());
        }
        return null; // Return null if no matching event is found
    }
}