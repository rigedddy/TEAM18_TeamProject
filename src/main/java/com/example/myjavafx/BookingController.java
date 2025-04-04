package com.example.myjavafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class BookingController implements Initializable {

    @FXML
    private ImageView profileimg;

    // group meeting
    @FXML
    private Label time;
    @FXML
    private TextField BookingID;
    @FXML
    private TextField NumOfPeople;
    @FXML
    private TextField Name;
    @FXML
    private TextField Email;
    @FXML
    private ChoiceBox<String> TimeSlot;
    private final String[] TimeChoices = {"1Hour", "MorningAndAfternoon", "AllDay", "AllWeek"};

    @FXML
    private TextField MeetingPrice;
    @FXML
    private ChoiceBox<String> RoomName;
    @FXML
    private ChoiceBox<String> ClientName;
    @FXML
    private DatePicker Date;
    @FXML
    private DatePicker DateGroupBooking;


    @FXML
    private ChoiceBox<String> InstitutionChoice;

    private String[] fetchClientNames() {
        ArrayList<String> roomNamesList = new ArrayList<>();

        String query = "SELECT company_name FROM CompanyDetails";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                roomNamesList.add(rs.getString("company_name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return roomNamesList.toArray(new String[0]);
    }
    private String[] fetchRoomNames() {
        ArrayList<String> roomNamesList = new ArrayList<>();

        String query = "SELECT RoomName FROM MeetingRooms";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                roomNamesList.add(rs.getString("RoomName"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return roomNamesList.toArray(new String[0]);
    }
    private final String[] institutionChoices = {"Primary School", "Secondary School", "College", "University"};
    private ActionEvent event;
    private final String[] RoomNameChoices = fetchRoomNames();
    private final String[] ClientNameChoices = fetchClientNames();
    // Get the data that was inputted by the user and ensure validation
    @FXML
    void createNewBooking(ActionEvent event) {
        boolean isValid = true;

        //String bookingIDString = BookingID.getText().trim();
        String numOfPeopleString = NumOfPeople.getText().trim();
        String nameString = Name.getText().trim();
        String emailString = Email.getText().trim();
        String institutionChoiceString = InstitutionChoice.getValue();
        LocalDate startDate = DateGroupBooking.getValue();
        String DateForGroup = startDate.toString();

        // Reset border color for all fields

        NumOfPeople.setStyle("");
        Name.setStyle("");
        Email.setStyle("");

        InstitutionChoice.setStyle("");

        // Validate each field

        if (numOfPeopleString.isEmpty()) {
            NumOfPeople.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }
        if (nameString.isEmpty()) {
            Name.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }
        if (emailString.isEmpty()) {
            Email.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }

        if (institutionChoiceString == null) {
            InstitutionChoice.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
            isValid = false;
        }
        if (DateGroupBooking== null) {
            isValid = false;
        }

        // Only create data if all fields are filled
        if (isValid) {
            String[] data = {numOfPeopleString, nameString, emailString, institutionChoiceString, DateForGroup};
            System.out.println("Data registered: " + Arrays.toString(data));
        } else {
            System.out.println("Error: Please fill in all required fields.");
        }
    }
    @FXML
    void createNewMeetingRoomBooking(ActionEvent event) {
    try{
        String Client = ClientName.getValue().strip();
        String Room = RoomName.getValue().strip();
        String date = Date.getValue().toString();
        String timeSlot = TimeSlot.getValue().strip();
        if (Client != null && !Client.isEmpty() && Room != null && !Room.isEmpty() && date != null && !date.isEmpty() && timeSlot != null && !timeSlot.isEmpty()) {


            String SQLSearch = switch (timeSlot) {
                case "1Hour" -> "RateFor1Hour";
                case "MorningAndAfternoon" -> "RateForMorningAndAfternoon";
                case "AllDay" -> "AllDayRate";
                case "AllWeek" -> "WeekRate";
                default -> "RateFor1Hour";
            };
            String query = "SELECT " + SQLSearch + " FROM MeetingRooms WHERE RoomName = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, Room);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String price = rs.getString(SQLSearch);

                    MeetingPrice.setText(price);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {

            System.out.println("Please make sure all fields are filled out correctly.");
        }
    } catch (Exception e) {
        System.out.println("Please fill in all required fields.");
    }}


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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        InstitutionChoice.getItems().addAll(institutionChoices);
        RoomName.getItems().addAll(RoomNameChoices);
        ClientName.getItems().addAll(ClientNameChoices);
        TimeSlot.getItems().addAll(TimeChoices);
    }
}
