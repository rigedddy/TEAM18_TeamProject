package com.example.myjavafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class LoginController {

    private ActionEvent event;

    @FXML
    void signIn(ActionEvent event) throws IOException {
        this.event = event;
        LoginApplication.moveToDashboard();
    }


}
