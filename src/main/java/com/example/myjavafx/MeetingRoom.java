package com.example.myjavafx;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MeetingRoom {

    private ChoiceBox<String> roomName;
    private ChoiceBox<String> clientName;
    private ChoiceBox<String> timeSlot;
    private DatePicker date;
    private TextField meetingPrice;

    private final String[] timeChoices = {"1Hour", "MorningAndAfternoon", "AllDay", "AllWeek"};
    private final String[] roomNameChoices;
    private final String[] clientNameChoices;

    // Constructor
    public MeetingRoom(ChoiceBox<String> roomName, ChoiceBox<String> clientName, ChoiceBox<String> timeSlot,
                       DatePicker date, TextField meetingPrice) {
        this.roomName = roomName;
        this.clientName = clientName;
        this.timeSlot = timeSlot;
        this.date = date;
        this.meetingPrice = meetingPrice;

        // Fetch room names and client names during initialization
        this.roomNameChoices = fetchRoomNames();
        this.clientNameChoices = fetchClientNames();

        // Populate the ChoiceBox components
        initializeChoiceBoxes();
    }

    // Fetch room names from the database
    private String[] fetchRoomNames() {
        ArrayList<String> roomNamesList = new ArrayList<>();

        String query = "SELECT RoomName FROM MeetingRooms";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                roomNamesList.add(rs.getString("RoomName"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roomNamesList.toArray(new String[0]);
    }

    // Fetch client names from the database
    private String[] fetchClientNames() {
        ArrayList<String> clientNamesList = new ArrayList<>();

        String query = "SELECT company_name FROM CompanyDetails";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                clientNamesList.add(rs.getString("company_name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clientNamesList.toArray(new String[0]);
    }

    // Initialize the ChoiceBox components
    private void initializeChoiceBoxes() {
        roomName.getItems().addAll(roomNameChoices);
        clientName.getItems().addAll(clientNameChoices);
        timeSlot.getItems().addAll(timeChoices);
    }

    // Method to create a new meeting room booking
    public void createNewMeetingRoomBooking() {
        try {
            String client = clientName.getValue() != null ? clientName.getValue().strip() : null;
            String room = roomName.getValue() != null ? roomName.getValue().strip() : null;
            String dateValue = date.getValue() != null ? date.getValue().toString() : null;
            String timeSlotValue = timeSlot.getValue() != null ? timeSlot.getValue().strip() : null;

            if (client != null && !client.isEmpty() && room != null && !room.isEmpty() &&
                    dateValue != null && !dateValue.isEmpty() && timeSlotValue != null && !timeSlotValue.isEmpty()) {

                // Determine the correct rate column based on the time slot
                String SQLSearch = switch (timeSlotValue) {
                    case "1Hour" -> "RateFor1Hour";
                    case "MorningAndAfternoon" -> "RateForMorningAndAfternoon";
                    case "AllDay" -> "AllDayRate";
                    case "AllWeek" -> "WeekRate";
                    default -> "RateFor1Hour";
                };

                // Fetch the price for the selected room and time slot
                String query = "SELECT " + SQLSearch + " FROM MeetingRooms WHERE RoomName = ?";
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(query)) {

                    stmt.setString(1, room);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        String price = rs.getString(SQLSearch);
                        meetingPrice.setText(price);

                        // Log the data
                        String[] data = {client, room, dateValue, timeSlotValue, price};
                        System.out.println("Data registered: " + java.util.Arrays.toString(data));

                        // Insert the booking into the database
                        String insertQuery = "INSERT INTO MeetingRoomBooking (RoomName, ClientName, BookingDate, LengthOfBooking, Price) VALUES (?, ?, ?, ?, ?)";
                        try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                            pstmt.setString(1, room);
                            pstmt.setString(2, client);
                            pstmt.setString(3, dateValue);
                            pstmt.setString(4, timeSlotValue);
                            pstmt.setString(5, price);

                            int rowsInserted = pstmt.executeUpdate();
                            if (rowsInserted > 0) {
                                System.out.println("Booking inserted successfully!");
                            } else {
                                System.out.println("No booking was inserted.");
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Please make sure all fields are filled out correctly.");
            }
        } catch (Exception e) {
            System.out.println("Please fill in all required fields.");
        }
    }
}