package com.example.myjavafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;


import java.io.IOException;
import java.sql.*;


public class LoginController {

    private ActionEvent event;

    @FXML
    private TextField username;
    @FXML
    private TextField password;

    @FXML
    void signIn(ActionEvent event) throws IOException {
        this.event = event;

        String enteredUserName = username.getText();
        String enteredPassword = password.getText();

        if (userExists(enteredUserName, enteredPassword)) {
            System.out.println("Login successful!");
            Session.currentUsername = enteredUserName;
            LoginApplication.moveToDashboard(); // change screen
        }
        else {
            System.out.println("Incorrect username! Try again."); // debugging message
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
