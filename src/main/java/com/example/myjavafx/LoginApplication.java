package com.example.myjavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main application class that launches the JavaFX GUI and handles
 * navigation between different FXML scenes for the marketing team.
 */
public class LoginApplication extends Application {
    static Stage stage;

    /**
     * Starts the JavaFX application and loads the initial sign-in scene.
     *
     * @param stage The primary stage for this application.
     * @throws IOException If the FXML file cannot be loaded.
     */
    @Override
    public void start(Stage stage) throws IOException {
        LoginApplication.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("signin.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("MARKETING TEAM");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Navigates to the Sign In scene.
     *
     * @throws IOException If the FXML file cannot be loaded.
     */
    public static void moveToSignIn() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("signin.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Navigates to the Dashboard scene.
     *
     * @throws IOException If the FXML file cannot be loaded.
     */
    public static void moveToDashboard() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("dashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setScene(scene);
    }

    /**
     * Navigates to the Reports scene.
     *
     * @throws IOException If the FXML file cannot be loaded.
     */
    public static void moveToReports() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("reports.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setScene(scene);
    }

    /**
     * Navigates to the Profile scene.
     *
     * @throws IOException If the FXML file cannot be loaded.
     */
    public static void moveToProfile() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("profile.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setScene(scene);
    }

    /**
     * Navigates to the Films scene.
     *
     * @throws IOException If the FXML file cannot be loaded.
     */
    public static void moveToFilms() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("films.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setScene(scene);
    }

    /**
     * Navigates to the Booking scene.
     *
     * @throws IOException If the FXML file cannot be loaded.
     */
    public static void moveToBooking() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("booking.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setScene(scene);
    }

    /**
     * Navigates to the Calendar scene.
     *
     * @throws IOException If the FXML file cannot be loaded.
     */
    public static void moveToCalendar() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("calendar.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setScene(scene);
    }

    /**
     * Navigates to the Advertising scene.
     *
     * @throws IOException If the FXML file cannot be loaded.
     */
    public static void moveToAdvertising() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("advertising.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setScene(scene);
    }

    /**
     * Launches the JavaFX application.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch();
    }
}
