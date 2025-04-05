package com.example.myjavafx;

import javafx.application.Platform;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Reports {

    // Define a list of colors to match the screenshot
    private static final String[] COLORS = {
            "#FF8C00", // Orange (College)
            "#FFD700", // Yellow (Not Specified)
            "#32CD32", // Green (Primary School)
            "#00CED1", // Cyan (Secondary School)
            "#4169E1"  // Blue (University)
    };

    // Method to fetch institution counts from both VenueTour and GroupBookings
    public Map<String, Integer> getInstitutionTourCounts() {
        Map<String, Integer> institutionCounts = new HashMap<>();

        // Query 1: Fetch counts from VenueTour
        String venueTourQuery = "SELECT Institution, COUNT(*) as count FROM VenueTour GROUP BY Institution";
        fetchInstitutionCounts(venueTourQuery, institutionCounts);

        // Query 2: Fetch counts from GroupBookings
        String groupBookingsQuery = "SELECT Institution, COUNT(*) as count FROM GroupBookings GROUP BY Institution";
        fetchInstitutionCounts(groupBookingsQuery, institutionCounts);

        return institutionCounts;
    }

    // Helper method to execute a query and update the institution counts map
    private void fetchInstitutionCounts(String query, Map<String, Integer> institutionCounts) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String institution = rs.getString("Institution");
                int count = rs.getInt("count");

                // Handle NULL values by labeling them as "Not Specified"
                if (institution == null) {
                    institution = "Not Specified";
                }

                // Add to the existing count if the institution already exists in the map
                institutionCounts.put(institution, institutionCounts.getOrDefault(institution, 0) + count);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching institution data: " + e.getMessage());
        }
    }

    // Method to populate the PieChart with combined institution data
    public void populateInstitutionsPieChart(PieChart pieChart) {
        Map<String, Integer> institutionCounts = getInstitutionTourCounts();

        // Clear any existing data in the pie chart
        pieChart.getData().clear();

        // Sort the entries alphabetically by institution name for consistent coloring
        institutionCounts.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    PieChart.Data data = new PieChart.Data(entry.getKey() + " (" + entry.getValue() + ")", entry.getValue());
                    pieChart.getData().add(data);
                });

        // Disable labels on the pie chart slices to avoid clutter
        pieChart.setLabelsVisible(false);

        // Apply colors to each slice and legend
        Platform.runLater(() -> {
            int colorIndex = 0;
            for (PieChart.Data data : pieChart.getData()) {
                // Apply color to the pie chart slice
                Node node = data.getNode();
                if (node != null) {
                    String color = COLORS[colorIndex % COLORS.length];
                    node.setStyle("-fx-pie-color: " + color + ";");
                }
                colorIndex++;
            }

            // Apply colors to the legend items
            colorIndex = 0;
            for (Node legendItem : pieChart.lookupAll(".chart-legend-item")) {
                if (legendItem instanceof Label) {
                    Label label = (Label) legendItem;
                    Node symbol = label.lookup(".chart-legend-item-symbol");
                    if (symbol != null) {
                        String color = COLORS[colorIndex % COLORS.length];
                        symbol.setStyle("-fx-background-color: " + color + ";");
                    }
                    colorIndex++;
                }
            }
        });

        // Ensure the legend is visible and positioned
        pieChart.setLegendVisible(true);
        pieChart.setLegendSide(Side.RIGHT);
    }

    // Method to get the total number of distinct institutions (excluding "Not Specified")
    public int getTotalInstitutions() {
        Map<String, Integer> institutionCounts = getInstitutionTourCounts();
        return (int) institutionCounts.keySet().stream()
                .filter(key -> !key.equals("Not Specified"))
                .count();
    }

    // Method to get the total number of tours/bookings (total rows from both tables)
    public int getTotalTours() {
        Map<String, Integer> institutionCounts = getInstitutionTourCounts();
        return institutionCounts.values().stream().mapToInt(Integer::intValue).sum();
    }
}