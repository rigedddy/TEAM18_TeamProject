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

    // Exchange rate for GBP to USD (as of early 2025, approximate)
    private static final double GBP_TO_USD = 1.3;

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

    // Method to fetch years for the FilmTicketYear ChoiceBox (hardcoded 2023-2024)
    public List<String> getFilmTicketYears() {
        return Arrays.asList("2024", "2023");
    }

    // Method to fetch films and their license costs for a given year
    private Map<String, Double> getFilmLicenseCosts(String year) {
        Map<String, Double> filmLicenseCosts = new LinkedHashMap<>();

        String query = "SELECT FilmID, Title, LicenseCost " +
                "FROM Film " +
                "WHERE YEAR(Date) = ? " +
                "ORDER BY FilmID";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, Integer.parseInt(year));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int filmId = rs.getInt("FilmID");
                double licenseCost = rs.getDouble("LicenseCost");
                filmLicenseCosts.put(String.valueOf(filmId), licenseCost);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching film license costs: " + e.getMessage());
        }

        return filmLicenseCosts;
    }

    // Method to fetch ticket sales revenue for each film in a given year
    private Map<String, Double> getFilmTicketSales(String year) {
        Map<String, Double> filmTicketSales = new LinkedHashMap<>();

        // First, get the films shown in the selected year to initialize the map
        String filmQuery = "SELECT FilmID " +
                "FROM Film " +
                "WHERE YEAR(Date) = ? " +
                "ORDER BY FilmID";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(filmQuery)) {

            stmt.setInt(1, Integer.parseInt(year));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int filmId = rs.getInt("FilmID");
                filmTicketSales.put(String.valueOf(filmId), 0.0); // Initialize to 0
            }

        } catch (SQLException e) {
            System.out.println("Error fetching films for ticket sales: " + e.getMessage());
        }

        // Now, fetch ticket sales revenue for these films
        String query = "SELECT ts.FilmID, SUM(ts.TicketPrice) as totalRevenue " +
                "FROM TicketSales ts " +
                "WHERE YEAR(ts.PurchaseDateTime) = ? AND ts.FilmID IS NOT NULL " +
                "GROUP BY ts.FilmID " +
                "ORDER BY ts.FilmID";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, Integer.parseInt(year));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int filmId = rs.getInt("FilmID");
                double totalRevenue = rs.getDouble("totalRevenue");
                filmTicketSales.put(String.valueOf(filmId), totalRevenue);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching ticket sales revenue: " + e.getMessage());
        }

        return filmTicketSales;
    }

    // Method to populate the LineChart with film license costs and ticket sales revenue
    public void populateFilmTicketGraph(LineChart<String, Number> lineChart, String year) {
        Map<String, Double> licenseCosts = getFilmLicenseCosts(year);
        Map<String, Double> ticketSales = getFilmTicketSales(year);

        // Clear existing data
        lineChart.getData().clear();

        // Create series for license costs
        XYChart.Series<String, Number> licenseSeries = new XYChart.Series<>();
        licenseSeries.setName("License Costs");

        // Create series for ticket sales revenue
        XYChart.Series<String, Number> salesSeries = new XYChart.Series<>();
        salesSeries.setName("Ticket Sales Revenue");

        // Add data points for each film
        for (String filmId : licenseCosts.keySet()) {
            licenseSeries.getData().add(new XYChart.Data<>(filmId, licenseCosts.get(filmId)));
            salesSeries.getData().add(new XYChart.Data<>(filmId, ticketSales.getOrDefault(filmId, 0.0)));
        }

        // Add the series to the chart
        lineChart.getData().addAll(licenseSeries, salesSeries);

        // Style the lines and add tooltips
        Platform.runLater(() -> {
            for (XYChart.Series<String, Number> series : lineChart.getData()) {
                for (XYChart.Data<String, Number> data : series.getData()) {
                    // Add tooltip
                    javafx.scene.control.Tooltip tooltip = new javafx.scene.control.Tooltip(
                            "Film ID: " + data.getXValue() + "\n" +
                                    series.getName() + ": $" + String.format("%.2f", data.getYValue())
                    );
                    javafx.scene.control.Tooltip.install(data.getNode(), tooltip);

                    // Style the data points
                    data.getNode().setStyle("-fx-background-color: " +
                            (series.getName().equals("License Costs") ? "#FF0000" : "#00FF00") + ";");
                }
                // Style the lines
                if (series.getName().equals("License Costs")) {
                    series.getNode().setStyle("-fx-stroke: #FF0000; -fx-stroke-width: 2px;");
                } else if (series.getName().equals("Ticket Sales Revenue")) {
                    series.getNode().setStyle("-fx-stroke: #00FF00; -fx-stroke-width: 2px;");
                }
            }
        });

        // Set the chart title
        lineChart.setTitle("Film Costs vs Ticket Sales (" + year + ")");

        // Rotate x-axis labels to avoid overlap
        lineChart.getXAxis().setTickLabelRotation(45);

        // Adjust y-axis scale dynamically
        double maxValue = Math.max(
                licenseCosts.values().stream().mapToDouble(Double::doubleValue).max().orElse(0.0),
                ticketSales.values().stream().mapToDouble(Double::doubleValue).max().orElse(0.0)
        );
        ((javafx.scene.chart.NumberAxis) lineChart.getYAxis()).setUpperBound(Math.ceil(maxValue / 1000) * 1000);
        ((javafx.scene.chart.NumberAxis) lineChart.getYAxis()).setTickUnit(Math.ceil(maxValue / 5000) * 1000);
    }

    // Method to get the total number of films shown in a given year
    public int getTotalFilms(String year) {
        int totalFilms = 0;
        String query = "SELECT COUNT(*) as count FROM Film WHERE YEAR(Date) = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, Integer.parseInt(year));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                totalFilms = rs.getInt("count");
            }

        } catch (SQLException e) {
            System.out.println("Error fetching total films: " + e.getMessage());
        }

        return totalFilms;
    }

    // Method to get the total revenue from ticket sales in a given year
    public double getTotalTicketRevenue(String year) {
        double totalRevenue = 0.0;
        String query = "SELECT SUM(TicketPrice) as total FROM TicketSales WHERE YEAR(PurchaseDateTime) = ? AND FilmID IS NOT NULL";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, Integer.parseInt(year));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                totalRevenue = rs.getDouble("total");
            }

        } catch (SQLException e) {
            System.out.println("Error fetching total ticket revenue: " + e.getMessage());
        }

        return totalRevenue;
    }

    // Methods for Marketing General Metrics

    // Method to fetch distinct years for the general metrics (based on TicketSales, MeetingRoomBooking, etc.)
    public List<String> getGeneralMetricYears() {
        List<String> years = new ArrayList<>();
        String query = "SELECT DISTINCT YEAR(PurchaseDateTime) as year FROM TicketSales " +
                "UNION " +
                "SELECT DISTINCT YEAR(BookingDate) as year FROM MeetingRoomBooking " +
                "UNION " +
                "SELECT DISTINCT YEAR(JoinYear) as year FROM FriendsOfLancaster " +
                "ORDER BY year DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int year = rs.getInt("year");
                years.add(String.valueOf(year));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching general metric years: " + e.getMessage());
        }

        return years;
    }

    // Method to calculate total revenue by year
    private Map<String, Double> getTotalRevenueByYear() {
        Map<String, Double> revenueByYear = new TreeMap<>();

        // Initialize years with 0 revenue
        for (String year : getGeneralMetricYears()) {
            revenueByYear.put(year, 0.0);
        }

        // 1. Ticket Sales Revenue
        String ticketSalesQuery = "SELECT YEAR(PurchaseDateTime) as year, SUM(TicketPrice) as total " +
                "FROM TicketSales GROUP BY YEAR(PurchaseDateTime)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(ticketSalesQuery);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String year = String.valueOf(rs.getInt("year"));
                double revenue = rs.getDouble("total");
                revenueByYear.put(year, revenueByYear.getOrDefault(year, 0.0) + revenue);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching ticket sales revenue by year: " + e.getMessage());
        }

        // 2. Meeting Room Booking Revenue
        String meetingRoomQuery = "SELECT YEAR(BookingDate) as year, Price " +
                "FROM MeetingRoomBooking";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(meetingRoomQuery);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String year = String.valueOf(rs.getInt("year"));
                String priceStr = rs.getString("Price").replace("£", "");
                double priceInGBP = Double.parseDouble(priceStr);
                double priceInUSD = priceInGBP * GBP_TO_USD; // Convert to USD
                revenueByYear.put(year, revenueByYear.getOrDefault(year, 0.0) + priceInUSD);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching meeting room revenue by year: " + e.getMessage());
        }

        // 3. Friends of Lancaster Subscription Revenue (Assume $50 per active subscriber)
        String subscriptionQuery = "SELECT YEAR(JoinYear) as year, COUNT(*) as count " +
                "FROM FriendsOfLancaster " +
                "WHERE SubscriptionStatus = 1 " +
                "GROUP BY YEAR(JoinYear)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(subscriptionQuery);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String year = String.valueOf(rs.getInt("year"));
                int activeSubscribers = rs.getInt("count");
                double subscriptionRevenue = activeSubscribers * 50.0; // $50 per subscriber
                revenueByYear.put(year, revenueByYear.getOrDefault(year, 0.0) + subscriptionRevenue);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching subscription revenue by year: " + e.getMessage());
        }

        return revenueByYear;
    }

    // Method to calculate total costs by year
    private Map<String, Double> getTotalCostsByYear() {
        Map<String, Double> costsByYear = new TreeMap<>();

        // Initialize years with 0 costs
        for (String year : getGeneralMetricYears()) {
            costsByYear.put(year, 0.0);
        }

        // 1. Film License Costs
        String filmCostQuery = "SELECT YEAR(Date) as year, SUM(LicenseCost) as total " +
                "FROM Film GROUP BY YEAR(Date)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(filmCostQuery);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String year = String.valueOf(rs.getInt("year"));
                double cost = rs.getDouble("total");
                costsByYear.put(year, costsByYear.getOrDefault(year, 0.0) + cost);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching film license costs by year: " + e.getMessage());
        }

        // 2. Marketing Campaign Costs (Assume $500 per campaign)
        String marketingCostQuery = "SELECT YEAR(StartDate) as year, COUNT(*) as count " +
                "FROM MarketingCampaign GROUP BY YEAR(StartDate)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(marketingCostQuery);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String year = String.valueOf(rs.getInt("year"));
                int campaignCount = rs.getInt("count");
                double marketingCost = campaignCount * 500.0; // $500 per campaign
                costsByYear.put(year, costsByYear.getOrDefault(year, 0.0) + marketingCost);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching marketing campaign costs by year: " + e.getMessage());
        }

        // 3. Event Costs (Assume $200 per event)
        String eventCostQuery = "SELECT YEAR(EventDate) as year, COUNT(*) as count " +
                "FROM Events GROUP BY YEAR(EventDate)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(eventCostQuery);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String year = String.valueOf(rs.getInt("year"));
                int eventCount = rs.getInt("count");
                double eventCost = eventCount * 200.0; // $200 per event
                costsByYear.put(year, costsByYear.getOrDefault(year, 0.0) + eventCost);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching event costs by year: " + e.getMessage());
        }

        return costsByYear;
    }

    // Method to calculate total profits by year
    private Map<String, Double> getTotalProfitsByYear() {
        Map<String, Double> revenueByYear = getTotalRevenueByYear();
        Map<String, Double> costsByYear = getTotalCostsByYear();
        Map<String, Double> profitsByYear = new TreeMap<>();

        for (String year : getGeneralMetricYears()) {
            double revenue = revenueByYear.getOrDefault(year, 0.0);
            double costs = costsByYear.getOrDefault(year, 0.0);
            profitsByYear.put(year, revenue - costs);
        }

        return profitsByYear;
    }

    // Method to calculate total revenue (for the labels, all years combined)
    public double getTotalRevenue() {
        return getTotalRevenueByYear().values().stream().mapToDouble(Double::doubleValue).sum();
    }

    // Method to calculate total costs (for the labels, all years combined)
    public double getTotalCosts() {
        return getTotalCostsByYear().values().stream().mapToDouble(Double::doubleValue).sum();
    }

    // Method to calculate total profits (for the labels, all years combined)
    public double getTotalProfits() {
        return getTotalProfitsByYear().values().stream().mapToDouble(Double::doubleValue).sum();
    }

    // Method to populate the generalGraph with revenue, costs, and profits by year
    public void populateGeneralGraph(LineChart<String, Number> lineChart) {
        Map<String, Double> revenueByYear = getTotalRevenueByYear();
        Map<String, Double> costsByYear = getTotalCostsByYear();
        Map<String, Double> profitsByYear = getTotalProfitsByYear();

        // Clear existing data
        lineChart.getData().clear();

        // Create series for revenue
        XYChart.Series<String, Number> revenueSeries = new XYChart.Series<>();
        revenueSeries.setName("Total Revenue");

        // Create series for costs
        XYChart.Series<String, Number> costsSeries = new XYChart.Series<>();
        costsSeries.setName("Total Costs");

        // Create series for profits
        XYChart.Series<String, Number> profitsSeries = new XYChart.Series<>();
        profitsSeries.setName("Total Profits");

        // Get the years and reverse them to plot in ascending order (2020 to 2025)
        List<String> years = getGeneralMetricYears();
        Collections.reverse(years); // Reverse the list to go from oldest to newest

        // Add data points for each year
        for (String year : years) {
            Double revenue = revenueByYear.getOrDefault(year, 0.0);
            Double costs = costsByYear.getOrDefault(year, 0.0);
            Double profits = profitsByYear.getOrDefault(year, 0.0);

            revenueSeries.getData().add(new XYChart.Data<>(year, revenue));
            costsSeries.getData().add(new XYChart.Data<>(year, costs));
            profitsSeries.getData().add(new XYChart.Data<>(year, profits));
        }

        // Add the series to the chart
        lineChart.getData().addAll(revenueSeries, costsSeries, profitsSeries);

        // Style the lines and add tooltips
        Platform.runLater(() -> {
            for (XYChart.Series<String, Number> series : lineChart.getData()) {
                for (XYChart.Data<String, Number> data : series.getData()) {
                    // Add tooltip
                    javafx.scene.control.Tooltip tooltip = new javafx.scene.control.Tooltip(
                            "Year: " + data.getXValue() + "\n" +
                                    series.getName() + ": $" + String.format("%.2f", data.getYValue())
                    );
                    javafx.scene.control.Tooltip.install(data.getNode(), tooltip);

                    // Style the data points
                    String color = series.getName().equals("Total Revenue") ? "#00FF00" :
                            series.getName().equals("Total Costs") ? "#FF0000" :
                                    "#0000FF"; // Green for revenue, Red for costs, Blue for profits
                    data.getNode().setStyle("-fx-background-color: " + color + ";");
                }
                // Style the lines
                if (series.getName().equals("Total Revenue")) {
                    series.getNode().setStyle("-fx-stroke: #00FF00; -fx-stroke-width: 2px;");
                } else if (series.getName().equals("Total Costs")) {
                    series.getNode().setStyle("-fx-stroke: #FF0000; -fx-stroke-width: 2px;");
                } else if (series.getName().equals("Total Profits")) {
                    series.getNode().setStyle("-fx-stroke: #0000FF; -fx-stroke-width: 2px;");
                }
            }
        });

        // Set the chart title
        lineChart.setTitle("Marketing General Metrics by Year");

        // Adjust y-axis scale dynamically
        double maxValue = Math.max(
                revenueByYear.values().stream().mapToDouble(Double::doubleValue).max().orElse(0.0),
                costsByYear.values().stream().mapToDouble(Double::doubleValue).max().orElse(0.0)
        );
        double minValue = profitsByYear.values().stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
        minValue = Math.min(minValue, 0); // Ensure the y-axis goes below 0 if profits are negative
        ((javafx.scene.chart.NumberAxis) lineChart.getYAxis()).setLowerBound(Math.floor(minValue / 1000) * 1000);
        ((javafx.scene.chart.NumberAxis) lineChart.getYAxis()).setUpperBound(Math.ceil(maxValue / 1000) * 1000);
        ((javafx.scene.chart.NumberAxis) lineChart.getYAxis()).setTickUnit(Math.ceil(maxValue / 5000) * 1000);
    }
}