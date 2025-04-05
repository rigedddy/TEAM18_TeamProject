package com.example.myjavafx;

import javafx.application.Platform;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Reports {

    // Define a list of colors to match the screenshot
    private static final String[] COLORS = {
            "#FF8C00", // Orange (College)
            "#FFD700", // Yellow (Not Specified)
            "#32CD32", // Green (Primary School)
            "#00CED1", // Cyan (Secondary School)
            "#4169E1"  // Blue (University)
    };

    // Define month names for the x-axis
    private static final String[] MONTHS = {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
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

    // Method to fetch distinct years from the JoinYear column
    public List<String> getFOLYears() {
        List<String> years = new ArrayList<>();
        String query = "SELECT DISTINCT YEAR(JoinYear) as year FROM FriendsOfLancaster ORDER BY year DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int year = rs.getInt("year");
                years.add(String.valueOf(year));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching FOL years: " + e.getMessage());
        }

        return years;
    }

    // Method to fetch the number of active subscribers per month for a given year
    private Map<String, Integer> getSubscribersPerMonth(String year) {
        Map<String, Integer> subscribersPerMonth = new LinkedHashMap<>();
        // Initialize counts for each month
        for (String month : MONTHS) {
            subscribersPerMonth.put(month, 0);
        }

        String query = "SELECT JoinYear, ExpiryDate " +
                "FROM FriendsOfLancaster " +
                "WHERE SubscriptionStatus = 1 AND YEAR(JoinYear) <= ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, Integer.parseInt(year));
            ResultSet rs = stmt.executeQuery();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            while (rs.next()) {
                String joinYearStr = rs.getString("JoinYear");
                String expiryDateStr = rs.getString("ExpiryDate");

                // Parse dates
                LocalDate joinDate = LocalDate.parse(joinYearStr, formatter);
                LocalDate expiryDate = LocalDate.parse(expiryDateStr, formatter);

                // Check each month of the selected year
                for (int month = 1; month <= 12; month++) {
                    LocalDate monthStart = LocalDate.of(Integer.parseInt(year), month, 1);
                    LocalDate monthEnd = monthStart.withDayOfMonth(monthStart.lengthOfMonth());

                    // Subscriber is active in this month if:
                    // - JoinYear is on or before the end of the month
                    // - ExpiryDate is on or after the start of the month
                    if (!joinDate.isAfter(monthEnd) && !expiryDate.isBefore(monthStart)) {
                        String monthName = MONTHS[month - 1];
                        subscribersPerMonth.put(monthName, subscribersPerMonth.get(monthName) + 1);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error fetching subscribers per month: " + e.getMessage());
        }

        return subscribersPerMonth;
    }

    // Method to populate the LineChart with subscribers per month for a given year
    public void populateFOLGraph(LineChart<String, Number> lineChart, String year) {
        Map<String, Integer> subscribersPerMonth = getSubscribersPerMonth(year);

        // Clear existing data
        lineChart.getData().clear();

        // Create a series for the data
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Subscribers in " + year);

        // Add data points for each month
        for (Map.Entry<String, Integer> entry : subscribersPerMonth.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        // Add the series to the chart
        lineChart.getData().add(series);

        // Optional: Style the line chart
        lineChart.setTitle("Subscribers per Month (" + year + ")");
    }

    // Method to get the total number of active subscribers for a given year
    public int getTotalSubscribers(String year) {
        int totalSubscribers = 0;
        String query = "SELECT COUNT(*) as count " +
                "FROM FriendsOfLancaster " +
                "WHERE SubscriptionStatus = 1 AND YEAR(JoinYear) <= ? AND " +
                "(ExpiryDate >= ? OR ExpiryDate IS NULL)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            int yearInt = Integer.parseInt(year);
            stmt.setInt(1, yearInt);
            // Use the start of the next year to check if the subscription was active at any point in the selected year
            stmt.setString(2, (yearInt + 1) + "-01-01");

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                totalSubscribers = rs.getInt("count");
            }

        } catch (SQLException e) {
            System.out.println("Error fetching total subscribers: " + e.getMessage());
        }

        return totalSubscribers;
    }
}