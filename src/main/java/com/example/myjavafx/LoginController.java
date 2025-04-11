package com.example.myjavafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.*;

/**
 * Controller class for handling login operations, including user credential
 * validation against the database and scene navigation upon successful login.
 */
public class LoginController {

    private ActionEvent event;

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    /**
     * Handles the sign-in button action. Validates user credentials and
     * navigates to the dashboard if login is successful.
     *
     * @param event The ActionEvent triggered by clicking the sign-in button.
     * @throws IOException If scene transition fails.
     */
    @FXML
    void signIn(ActionEvent event) throws IOException {
        this.event = event;

        String enteredUserName = username.getText();
        String enteredPassword = password.getText();

        if (userExists(enteredUserName, enteredPassword)) {
            System.out.println("Login successful!");
            Session.currentUsername = enteredUserName;
            LoginApplication.moveToDashboard();
        } else {
            System.out.println("Incorrect username! Try again.");
        }
    }

    /**
     * Checks if a user with the given username and password exists in the database.
     *
     * @param username The entered username.
     * @param password The entered password.
     * @return True if the user exists, false otherwise.
     */
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
            }
            return c > 0;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }
}
