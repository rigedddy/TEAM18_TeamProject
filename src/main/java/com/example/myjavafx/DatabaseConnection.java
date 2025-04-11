package com.example.myjavafx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Provides a method to establish a connection to a MySQL database.
 * <p>
 * This class connects to the Team 18 MySQL database. A VPN connection may be required
 * for external access.
 * </p>
 *
 * <p><strong>Usage:</strong></p>
 * <pre>
 *     Connection conn = DatabaseConnection.getConnection();
 *     if (conn != null) {
 *         // Use the connection
 *     }
 * </pre>
 */
public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://sst-stuproj.city.ac.uk:3306/in2033t18";
    private static final String USER = "in2033t18_a";
    private static final String PASSWORD = "V89p57AXf8M";

    /**
     * Attempts to establish a connection to the database using the provided URL,
     * username, and password.
     *
     * @return a {@link Connection} object if successful; {@code null} otherwise.
     *         Prints an error message to the console if the connection fails.
     */
    public static Connection getConnection() {
        try {
            // Try to establish a connection within 3 seconds
            DriverManager.setLoginTimeout(3);
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Database connection failed. Ensure VPN is active.");
            return null;
        }
    }


    public static void main(String[] args) {
        Connection conn = getConnection();
        if (conn != null) {
            System.out.println("Connected to the database successfully!");
        } else {
            System.out.println("Failed to connect. Check VPN.");
        }
    }
}
