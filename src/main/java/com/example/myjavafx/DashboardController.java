package com.example.myjavafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class DashboardController {

    @FXML
    private Label time;

    @FXML
    private ImageView profileimg;

    @FXML
    void goToDashboard(ActionEvent event) {
        System.out.println("Hello World");
        time.setText("Time: " + time.getText());
    }

    @FXML
    void goToProfile(MouseEvent event) {
        System.out.println("clicked");
    }
}
