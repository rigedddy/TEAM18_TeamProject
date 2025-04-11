package com.example.myjavafx;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Handles meeting room bookings by fetching available rooms, clients, time slots,
 * and inserting new bookings into the database with calculated pricing.
 */
public class MeetingRoom {

    private ChoiceBox<String> roomName;
    private ChoiceBox<String> clientName;
    private ChoiceBox<String> timeSlot;
    private DatePicker date;
    private TextField meetingPrice;

    private final String[] timeChoices = {"1Hour", "MorningAndAfternoon", "AllDay", "AllWeek"};
    private final String[] roomNameChoices;
    private final String[] clientNameChoices;

    /**
     * Constructor for MeetingRoom. Initializes UI components and fetches
     * choices for room names and client names from the database.
     *
     * @param roomName     ChoiceBox for room names.
     * @param clientName   ChoiceBox for client company names.
     * @param timeSlot     ChoiceBox for selecting the duration of booking.
     * @param date         DatePicker for selecting the booking date.
     * @param meetingPrice TextField to display the calculated price.
     */
    public MeetingRoom(ChoiceBox<String> roomName, ChoiceBox<String> clientName, ChoiceBox<String> timeSlot,
                       DatePicker date, TextField meetingPrice) {
        this.roomName = roomName;
        this.clientName = clientName;
        this.timeSlot = timeSlot;
        this.date = date;
        this.meetingPrice = meetingPrice;

        this.roomNameChoices = fetchRoomNames();
        this.clientNameChoices = fetchClientNames();

        initializeChoiceBoxes();
    }

    /**
     * Fetches available meeting room names from the database.
     *
     * @return Array of room names.
     */
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

    /**
     * Fetches client company names from the database.
     *
     * @return Array of client names.
     */
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

    /**
     * Initializes the ChoiceBox components with fetched values for rooms, clients, and time slots.
     */
    private void initializeChoiceBoxes() {
        roomName.getItems().addAll(roomNameChoices);
        clientName.getItems().addAll(clientNameChoices);
        timeSlot.getItems().addAll(timeChoices);
    }

    /**
     * Validates user input and inserts a new meeting room booking into the database.
     * Automatically calculates and displays the price based on the selected time slot.
     */
    public void createNewMeetingRoomBooking() {
        try {
            String client = clientName.getValue() != null ? clientName.getValue().strip() : null;
            String room = roomName.getValue() != null ? roomName.getValue().strip() : null;
            String dateValue = date.getValue() != null ? date.getValue().toString() : null;
            String timeSlotValue = timeSlot.getValue() != null ? timeSlot.getValue().strip() : null;

            roomName.setStyle("");
            clientName.setStyle("");
            timeSlot.setStyle("");
            date.setStyle("");

            boolean isValid = true;

            if (room == null || room.isEmpty()) {
                roomName.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                isValid = false;
            }
            if (client == null || client.isEmpty()) {
                clientName.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                isValid = false;
            }
            if (timeSlotValue == null || timeSlotValue.isEmpty()) {
                timeSlot.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                isValid = false;
            }
            if (dateValue == null) {
                date.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                isValid = false;
            }

            if (isValid) {
                String SQLSearch = switch (timeSlotValue) {
                    case "1Hour" -> "RateFor1Hour";
                    case "MorningAndAfternoon" -> "RateForMorningAndAfternoon";
                    case "AllDay" -> "AllDayRate";
                    case "AllWeek" -> "WeekRate";
                    default -> "RateFor1Hour";
                };

                String query = "SELECT " + SQLSearch + " FROM MeetingRooms WHERE RoomName = ?";
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(query)) {

                    stmt.setString(1, room);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        String price = rs.getString(SQLSearch);
                        meetingPrice.setText(price);

                        String[] data = {client, room, dateValue, timeSlotValue, price};
                        System.out.println("Meeting Room Booking Data registered: " + java.util.Arrays.toString(data));

                        String insertQuery = "INSERT INTO MeetingRoomBooking (RoomName, ClientName, BookingDate, LengthOfBooking, Price) VALUES (?, ?, ?, ?, ?)";
                        try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                            pstmt.setString(1, room);
                            pstmt.setString(2, client);
                            pstmt.setString(3, dateValue);
                            pstmt.setString(4, timeSlotValue);
                            pstmt.setString(5, price);

                            int rowsInserted = pstmt.executeUpdate();
                            if (rowsInserted > 0) {
                                System.out.println("Meeting room booking inserted successfully!");
                            } else {
                                System.out.println("No meeting room booking was inserted.");
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                            System.out.println("Error inserting meeting room booking: " + e.getMessage());
                        }
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Error fetching price for meeting room: " + e.getMessage());
                }
            } else {
                System.out.println("Please fill in all required fields for Meeting Room Booking.");
            }
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }
}
