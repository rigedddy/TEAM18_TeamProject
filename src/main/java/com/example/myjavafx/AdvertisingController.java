package com.example.myjavafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class AdvertisingController implements Initializable {
    @FXML
    private ChoiceBox<String> FOLMember;

    @FXML
    private ImageView profileimg;
    @FXML
    private ChoiceBox<String> ShowType;
    private final String[] ShowTypeChoices = {"Music", "Film", "Other"};
    @FXML
    private ChoiceBox<String> ShowChoice;
    @FXML
    private ChoiceBox<String> SeatNumber;
    private final String[] SeatNumberChoices = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15", "16","17","18","19"};

    @FXML
    private Label time;
    private ActionEvent event;
    @FXML
    private ChoiceBox<String> SeatChoiceLetter;
    private final String[] SeatChoiceLetterChoices = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",};
    @FXML
    void goToBooking(ActionEvent event) throws IOException {
        LoginApplication.moveToBooking();
    }

    @FXML
    void goToDashboard(ActionEvent event) throws IOException {
        this.event = event;
        LoginApplication.moveToDashboard();
    }

    @FXML
    void goToProfile(MouseEvent event) throws IOException {
        LoginApplication.moveToProfile();
    }

    private void getFOLNames() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT Name FROM FriendsOfLancaster";

            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    FOLMember.getItems().add(rs.getString("Name"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateShowChoiceBox() {
        ShowChoice.getItems().clear();

        String selectedType = ShowType.getValue();
        if (selectedType == null) return;
       //test
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "";

            if (selectedType.equals("Film")) {
                query = "SELECT Title FROM Film";
            } else if (selectedType.equals("Music")) {
                query = "SELECT EventName FROM Events";
            } else {
                return;
            }

            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    ShowChoice.getItems().add(rs.getString(1));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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
    @FXML
    void SubmitFOLReservation() {
        String selectedShowType = ShowType.getValue();
        String selectedShow = ShowChoice.getValue();
        String selectedSeatLetter = SeatChoiceLetter.getValue();
        String selectedSeatNumber = SeatNumber.getValue();
        String selectedFriendName = FOLMember.getValue();


        if (selectedShowType == null || selectedShow == null || selectedSeatLetter == null
                || selectedSeatNumber == null || selectedFriendName == null) {
            System.out.println("Please select all fields before submitting.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            Integer filmID = null;
            Integer eventID = null;
            Integer friendID = null;


            if (selectedShowType.equals("Film")) {
                String filmQuery = "SELECT FilmID FROM Film WHERE Title = ?";
                try (PreparedStatement stmt = conn.prepareStatement(filmQuery)) {
                    stmt.setString(1, selectedShow);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        filmID = rs.getInt("FilmID");
                    }
                }
            } else if (selectedShowType.equals("Music")) {
                String eventQuery = "SELECT EventID FROM Events WHERE EventName = ?";
                try (PreparedStatement stmt = conn.prepareStatement(eventQuery)) {
                    stmt.setString(1, selectedShow);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        eventID = rs.getInt("EventID");
                    }
                }
            } else {
                System.out.println("Invalid show type.");
                return;
            }

            // Get FriendID
            String friendQuery = "SELECT FriendID FROM FriendsOfLancaster WHERE Name = ?";
            try (PreparedStatement stmt = conn.prepareStatement(friendQuery)) {
                stmt.setString(1, selectedFriendName);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    friendID = rs.getInt("FriendID");
                }
            }

            if ((filmID == null && eventID == null) || friendID == null) {
                System.out.println("Could not find necessary IDs.");
                return;
            }


            String insertQuery = "INSERT INTO FriendsOfLancasterTicketReservation (FilmID, EventID, SeatLetter, SeatNumber, FriendID) " +
                    "VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                if (filmID != null) {
                    insertStmt.setInt(1, filmID);
                } else {
                    insertStmt.setNull(1, java.sql.Types.INTEGER);
                }

                if (eventID != null) {
                    insertStmt.setInt(2, eventID);
                } else {
                    insertStmt.setNull(2, java.sql.Types.INTEGER);
                }

                insertStmt.setString(3, selectedSeatLetter);
                insertStmt.setString(4, selectedSeatNumber);
                insertStmt.setInt(5, friendID);

                int rowsAffected = insertStmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Reservation submitted successfully!");
                } else {
                    System.out.println("Failed to submit reservation.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SeatNumber.getItems().addAll(SeatNumberChoices);
        SeatChoiceLetter.getItems().addAll(SeatChoiceLetterChoices);
        ShowType.getItems().addAll(ShowTypeChoices);
        ShowType.setOnAction(e -> updateShowChoiceBox());


        FOLMember.setOnShowing(event -> {
            FOLMember.getItems().clear();
            getFOLNames();
        });
    }
}
