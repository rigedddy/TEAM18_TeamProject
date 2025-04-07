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

    private static final String[] COLORS = {
            "#FF8C00",
            "#FFD700",
            "#32CD32",
            "#00CED1",
            "#4169E1"
    };

    // month names for the x-axis
    private static final String[] MONTHS = {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    private static final double GBP_TO_USD = 1.3;

    public Map<String, Integer> getInstitutionTourCounts() {
        Map<String, Integer> institutionCounts = new HashMap<>();

        String venueTourQuery = "SELECT Institution, COUNT(*) as count FROM VenueTour GROUP BY Institution";
        fetchInstitutionCounts(venueTourQuery, institutionCounts);

        String groupBookingsQuery = "SELECT Institution, COUNT(*) as count FROM GroupBookings GROUP BY Institution";
        fetchInstitutionCounts(groupBookingsQuery, institutionCounts);

        return institutionCounts;
    }

    private void fetchInstitutionCounts(String query, Map<String, Integer> institutionCounts) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String institution = rs.getString("Institution");
                int count = rs.getInt("count");

                if (institution == null) {
                    institution = "Not Specified";
                }

                institutionCounts.put(institution, institutionCounts.getOrDefault(institution, 0) + count);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching institution data: " + e.getMessage());
        }
    }

    // method to populate the PieChart with combined institution data
    public void populateInstitutionsPieChart(PieChart pieChart) {
        Map<String, Integer> institutionCounts = getInstitutionTourCounts();

        pieChart.getData().clear();

        // sort the entries alphabetically by institution name for consistent coloring
        institutionCounts.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    PieChart.Data data = new PieChart.Data(entry.getKey() + " (" + entry.getValue() + ")", entry.getValue());
                    pieChart.getData().add(data);
                });

        pieChart.setLabelsVisible(false);

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

        pieChart.setLegendVisible(true);
        pieChart.setLegendSide(Side.RIGHT);
    }

    // method to get the total number of distinct institutions (excluding "Not Specified")
    public int getTotalInstitutions() {
        Map<String, Integer> institutionCounts = getInstitutionTourCounts();
        return (int) institutionCounts.keySet().stream()
                .filter(key -> !key.equals("Not Specified"))
                .count();
    }

    // method to get the total number of tours/bookings (total rows from both tables)
    public int getTotalTours() {
        Map<String, Integer> institutionCounts = getInstitutionTourCounts();
        return institutionCounts.values().stream().mapToInt(Integer::intValue).sum();
    }

    // method to fetch distinct years from the JoinYear column
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

    // method to fetch the number of active subscribers per month for a given year
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

                LocalDate joinDate = LocalDate.parse(joinYearStr, formatter);
                LocalDate expiryDate = LocalDate.parse(expiryDateStr, formatter);

                // check each month of the selected year
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

    // method to populate the LineChart with subscribers per month for a given year
    public void populateFOLGraph(LineChart<String, Number> lineChart, String year) {
        Map<String, Integer> subscribersPerMonth = getSubscribersPerMonth(year);

        lineChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Subscribers in " + year);

        for (Map.Entry<String, Integer> entry : subscribersPerMonth.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        lineChart.getData().add(series);

        lineChart.setTitle("Subscribers per Month (" + year + ")");
    }

    // method to get the total number of active subscribers for a given year
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

    // method to fetch years for the FilmTicketYear ChoiceBox (hardcoded 2023-2024)
    public List<String> getFilmTicketYears() {
        return Arrays.asList("2024", "2023");
    }

    // method to fetch films and their license costs for a given year
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

    // method to fetch ticket sales revenue for each film in a given year
    private Map<String, Double> getFilmTicketSales(String year) {
        Map<String, Double> filmTicketSales = new LinkedHashMap<>();

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

    // method to populate the LineChart with film license costs and ticket sales revenue
    public void populateFilmTicketGraph(LineChart<String, Number> lineChart, String year) {
        Map<String, Double> licenseCosts = getFilmLicenseCosts(year);
        Map<String, Double> ticketSales = getFilmTicketSales(year);

        lineChart.getData().clear();

        XYChart.Series<String, Number> licenseSeries = new XYChart.Series<>();
        licenseSeries.setName("License Costs");

        XYChart.Series<String, Number> salesSeries = new XYChart.Series<>();
        salesSeries.setName("Ticket Sales Revenue");

        for (String filmId : licenseCosts.keySet()) {
            licenseSeries.getData().add(new XYChart.Data<>(filmId, licenseCosts.get(filmId)));
            salesSeries.getData().add(new XYChart.Data<>(filmId, ticketSales.getOrDefault(filmId, 0.0)));
        }

        lineChart.getData().addAll(licenseSeries, salesSeries);

        Platform.runLater(() -> {
            for (XYChart.Series<String, Number> series : lineChart.getData()) {
                for (XYChart.Data<String, Number> data : series.getData()) {
                    // Add tooltip
                    javafx.scene.control.Tooltip tooltip = new javafx.scene.control.Tooltip(
                            "Film ID: " + data.getXValue() + "\n" +
                                    series.getName() + ": $" + String.format("%.2f", data.getYValue())
                    );
                    javafx.scene.control.Tooltip.install(data.getNode(), tooltip);

                    data.getNode().setStyle("-fx-background-color: " +
                            (series.getName().equals("License Costs") ? "#FF0000" : "#00FF00") + ";");
                }
                if (series.getName().equals("License Costs")) {
                    series.getNode().setStyle("-fx-stroke: #FF0000; -fx-stroke-width: 2px;");
                } else if (series.getName().equals("Ticket Sales Revenue")) {
                    series.getNode().setStyle("-fx-stroke: #00FF00; -fx-stroke-width: 2px;");
                }
            }
        });

        lineChart.setTitle("Film Costs vs Ticket Sales (" + year + ")");

        lineChart.getXAxis().setTickLabelRotation(45);

        double maxValue = Math.max(
                licenseCosts.values().stream().mapToDouble(Double::doubleValue).max().orElse(0.0),
                ticketSales.values().stream().mapToDouble(Double::doubleValue).max().orElse(0.0)
        );
        ((javafx.scene.chart.NumberAxis) lineChart.getYAxis()).setUpperBound(Math.ceil(maxValue / 1000) * 1000);
        ((javafx.scene.chart.NumberAxis) lineChart.getYAxis()).setTickUnit(Math.ceil(maxValue / 5000) * 1000);
    }

    // method to get the total number of films shown in a given year
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

    // method to get the total revenue from ticket sales in a given year
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

    // methods for Marketing General Metrics

    // method to fetch distinct years for the general metrics (based on TicketSales, MeetingRoomBooking, etc.)
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

    // method to calculate total revenue by year
    private Map<String, Double> getTotalRevenueByYear() {
        Map<String, Double> revenueByYear = new TreeMap<>();

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

    // method to calculate total costs by year
    private Map<String, Double> getTotalCostsByYear() {
        Map<String, Double> costsByYear = new TreeMap<>();

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

    // method to calculate total profits by year
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

    // method to calculate total revenue (for the labels, all years combined)
    public double getTotalRevenue() {
        return getTotalRevenueByYear().values().stream().mapToDouble(Double::doubleValue).sum();
    }

    // method to calculate total costs (for the labels, all years combined)
    public double getTotalCosts() {
        return getTotalCostsByYear().values().stream().mapToDouble(Double::doubleValue).sum();
    }

    // method to calculate total profits (for the labels, all years combined)
    public double getTotalProfits() {
        return getTotalProfitsByYear().values().stream().mapToDouble(Double::doubleValue).sum();
    }

    // method to populate the generalGraph with revenue, costs, and profits by year
    public void populateGeneralGraph(LineChart<String, Number> lineChart) {
        Map<String, Double> revenueByYear = getTotalRevenueByYear();
        Map<String, Double> costsByYear = getTotalCostsByYear();
        Map<String, Double> profitsByYear = getTotalProfitsByYear();

        lineChart.getData().clear();

        XYChart.Series<String, Number> revenueSeries = new XYChart.Series<>();
        revenueSeries.setName("Total Revenue");

        XYChart.Series<String, Number> costsSeries = new XYChart.Series<>();
        costsSeries.setName("Total Costs");

        XYChart.Series<String, Number> profitsSeries = new XYChart.Series<>();
        profitsSeries.setName("Total Profits");

        List<String> years = getGeneralMetricYears();
        Collections.reverse(years);

        for (String year : years) {
            Double revenue = revenueByYear.getOrDefault(year, 0.0);
            Double costs = costsByYear.getOrDefault(year, 0.0);
            Double profits = profitsByYear.getOrDefault(year, 0.0);

            revenueSeries.getData().add(new XYChart.Data<>(year, revenue));
            costsSeries.getData().add(new XYChart.Data<>(year, costs));
            profitsSeries.getData().add(new XYChart.Data<>(year, profits));
        }

        lineChart.getData().addAll(revenueSeries, costsSeries, profitsSeries);

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
                if (series.getName().equals("Total Revenue")) {
                    series.getNode().setStyle("-fx-stroke: #00FF00; -fx-stroke-width: 2px;");
                } else if (series.getName().equals("Total Costs")) {
                    series.getNode().setStyle("-fx-stroke: #FF0000; -fx-stroke-width: 2px;");
                } else if (series.getName().equals("Total Profits")) {
                    series.getNode().setStyle("-fx-stroke: #0000FF; -fx-stroke-width: 2px;");
                }
            }
        });

        lineChart.setTitle("Marketing General Metrics by Year");

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