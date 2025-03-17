package com.example.myjavafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {

    @FXML
    private Label time;

    @FXML
    void goToDashboard(ActionEvent event) {
        System.out.println("Hello World");
        time.setText("Time: " + time.getText());
    }

}
