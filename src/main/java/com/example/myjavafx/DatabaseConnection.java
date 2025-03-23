package com.example.myjavafx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://sst-stuproj.city.ac.uk:3306/in2033t18";
    private static final String USER = "in2033t18_a";
    private static final String PASSWORD = "V89p57AXf8M";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("Connected to the database successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

