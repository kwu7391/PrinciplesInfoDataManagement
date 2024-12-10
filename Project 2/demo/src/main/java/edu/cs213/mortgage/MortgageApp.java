package edu.cs213.mortgage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MortgageApp {
    private final MortgageDAO mortgageDAO = new MortgageDAO();
    private final FilterManager filterManager = new FilterManager();
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Main Program Execution Loop
     */
    public void run() {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Mortgage Backed Securities System ---");
            System.out.println("1. Add Filter");
            System.out.println("2. Delete Filter");
            System.out.println("3. View Matching Mortgages");
            System.out.println("4. Calculate and Package Mortgages");
            System.out.println("5. Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1 -> addFilter();
                case 2 -> deleteFilter();
                case 3 -> viewMatchingMortgages();
                case 4 -> calculateAndPackage();
                case 5 -> running = false;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
        System.out.println("Thank you for using the system!");
    }

    /**
     * Allows the user to define a filter for mortgage search.
     */
    private void addFilter() {
        System.out.println("Available Filters:");
        System.out.println("1. MSAMD (ID or Name)");
        System.out.println("2. County Name");
        System.out.println("3. Loan Type (1-4)");
        System.out.println("4. Loan Purpose (1-3)");
        System.out.println("5. Property Type (1-3)");
        System.out.println("6. Applicant Income Range");

        System.out.print("Enter filter number: ");
        int filterChoice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        String filterName, sqlCondition;
        List<Object> params;

        switch (filterChoice) {
            case 1 -> {
                System.out.print("Enter MSAMD (ID or Name): ");
                String msamd = scanner.nextLine();
                filterName = "MSAMD";
                sqlCondition = "(a.msamd = ? OR m.msamd_name ILIKE ?)";
                params = List.of(msamd, "%" + msamd + "%");
            }
            case 2 -> {
                System.out.print("Enter County Name: ");
                String county = scanner.nextLine();
                filterName = "County Name";
                sqlCondition = "l.county_name ILIKE ?";
                params = List.of("%" + county + "%");
            }
            case 3 -> {
                System.out.print("Enter Loan Type (1-4): ");
                int loanType = scanner.nextInt();
                filterName = "Loan Type";
                sqlCondition = "a.loan_type = ?";
                params = List.of(loanType);
            }
            case 4 -> {
                System.out.print("Enter Loan Purpose (1-3): ");
                int loanPurpose = scanner.nextInt();
                filterName = "Loan Purpose";
                sqlCondition = "a.loan_purpose = ?";
                params = List.of(loanPurpose);
            }
            case 5 -> {
                System.out.print("Enter Property Type (1-3): ");
                int propertyType = scanner.nextInt();
                filterName = "Property Type";
                sqlCondition = "a.property_type = ?";
                params = List.of(propertyType);
            }
            case 6 -> {
                System.out.print("Enter Minimum Applicant Income: ");
                int minIncome = scanner.nextInt();
                System.out.print("Enter Maximum Applicant Income: ");
                int maxIncome = scanner.nextInt();
                filterName = "Applicant Income Range";
                sqlCondition = "a.applicant_income_000s BETWEEN ? AND ?";
                params = List.of(minIncome, maxIncome);
            }
            default -> {
                System.out.println("Invalid filter choice.");
                return;
            }
        }

        filterManager.addFilter(new Filter(filterName, sqlCondition, params));
        System.out.println("Filter added: " + filterName);
    }

    /**
     * Deletes an existing filter or clears all.
     */
    private void deleteFilter() {
        List<Filter> filters = filterManager.getFilters();
        if (filters.isEmpty()) {
            System.out.println("No active filters.");
            return;
        }

        System.out.println("Current Filters:");
        for (int i = 0; i < filters.size(); i++) {
            System.out.println((i + 1) + ". " + filters.get(i).getName());
        }

        System.out.print("Enter filter number to delete or 0 to clear all: ");
        int choice = scanner.nextInt();

        if (choice == 0) {
            filterManager.clearFilters();
            System.out.println("All filters cleared.");
        } else if (choice > 0 && choice <= filters.size()) {
            filterManager.removeFilter(filters.get(choice - 1));
            System.out.println("Filter removed.");
        } else {
            System.out.println("Invalid choice.");
        }
    }

    /**
     * Displays matching mortgage records based on filters.
     */
    private void viewMatchingMortgages() {
        List<Mortgage> mortgages = mortgageDAO.getFilteredMortgages(filterManager.getFilters());

        if (mortgages.isEmpty()) {
            System.out.println("No matching mortgages found.");
            return;
        }

        double totalAmount = mortgages.stream().mapToDouble(Mortgage::getLoanAmount).sum();

        System.out.println("Number of matching mortgages: " + mortgages.size());
        System.out.printf("Total Loan Amount: $%.2f%n", totalAmount * 1000);  // Amount in dollars
    }

    /**
     * Calculates the rate and packages mortgages if accepted.
     */
    private void calculateAndPackage() {
        List<Mortgage> mortgages = mortgageDAO.getFilteredMortgages(filterManager.getFilters());

        if (mortgages.isEmpty()) {
            System.out.println("No mortgages found.");
            return;
        }

        double expectedRate = MortgageCalculator.calculateRate(mortgages);
        System.out.printf("Calculated Expected Rate: %.2f%%%n", expectedRate);

        System.out.print("Accept this rate and package loans? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();

        if ("yes".equals(response)) {
            try (Connection conn = DatabaseCon.connect()) {
                String updateQuery = "UPDATE application SET purchaser_type = 9 WHERE application_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                    for (Mortgage mortgage : mortgages) {
                        stmt.setInt(1, mortgage.getApplicationId());
                        stmt.executeUpdate();
                    }
                    System.out.println("Mortgages successfully packaged!");
                }
            } catch (SQLException e) {
                System.out.println("Error packaging mortgages.");
                e.printStackTrace();
            }
        } else {
            System.out.println("Rate rejected. Returning to the main menu.");
        }
    }
}
