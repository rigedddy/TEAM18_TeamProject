package com.example.myjavafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.*;

public class LoginController {

    private ActionEvent event;

    @FXML
    private TextField username; // Reference to the TextField in FXML
    @FXML
    private TextField password; // Reference to the TextField in FXML

    @FXML
    void signIn(ActionEvent event) throws IOException {
        this.event = event;

        String enteredUserName = username.getText(); // Get user input
        String enteredPassword = password.getText(); // Get user input

        if (userExists(enteredUserName, enteredPassword)) {
            System.out.println("Login successful!"); // Debugging message
            Session.currentUsername = enteredUserName;
            LoginApplication.moveToDashboard(); // Change screen
        }
        else {
            System.out.println("Incorrect username! Try again."); // Debugging message
        }
    }

    boolean userExists(String username, String password) {
        try {
            String query = "SELECT username,password FROM LoginDetails WHERE username = ? and password = ?";
            PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            int c = 0;
            while (resultSet.next()) {
                c++;
//                System.out.println(resultSet.getString(1) + " " + resultSet.getString(2));
            }
            return c > 0;
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }
}
