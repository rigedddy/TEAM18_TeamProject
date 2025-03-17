package com.example.myjavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginApplication extends Application {
    static Stage stage;
    @Override
    public void start(Stage stage) throws IOException {
        LoginApplication.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("signin.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("MARKETING TEAM");
        stage.setScene(scene);
        stage.show();
    }

    public static void moveToDashboard() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("dashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setScene(scene);
    }

    public static void main(String[] args) {
        launch();
    }
}