package edu.cs213.mortgage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
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
                case 4 -> calculateAndDisplayRate();
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
        System.out.println("\nAvailable Filters:");
        System.out.println("1. MSAMD");
        System.out.println("2. County Name");
        System.out.println("3. Loan Type (1-4)");
        System.out.println("4. Loan Purpose (1-3)");
        System.out.println("5. Property Type (1-3)");
        System.out.println("6. Applicant Income Range (Min & Max)");
        System.out.println("7. Tract to MSAMD Income (Min & Max)");
    
        System.out.print("Enter filter number: ");
        int filterChoice = scanner.nextInt();
        scanner.nextLine();  // Consume newline
    
        String filterName, sqlCondition;
        List<Object> params = new ArrayList<>();
    
        switch (filterChoice) {
            case 1 -> {
                System.out.print("Enter MSAMD (ID or Name): ");
                String msamd = scanner.nextLine();
                filterName = "MSAMD";
                sqlCondition = "(a.msamd = ? OR m.msamd_name ILIKE ?)";
                params.add(msamd);
                params.add("%" + msamd + "%");
            }
            case 2 -> {
                System.out.print("Enter County Name: ");
                String county = scanner.nextLine();
                filterName = "County";
                sqlCondition = "l.county_name ILIKE ?";
                params.add("%" + county + "%");
            }
            case 3 -> {
                System.out.print("Enter Loan Type (1-4): ");
                int loanType = scanner.nextInt();
                filterName = "Loan Type";
                sqlCondition = "a.loan_type = ?";
                params.add(loanType);
            }
            case 4 -> {
                System.out.print("Enter Loan Purpose (1-3): ");
                int loanPurpose = scanner.nextInt();
                filterName = "Loan Purpose";
                sqlCondition = "a.loan_purpose = ?";
                params.add(loanPurpose);
            }
            case 5 -> {
                System.out.print("Enter Property Type (1-3): ");
                int propertyType = scanner.nextInt();
                filterName = "Property Type";
                sqlCondition = "a.property_type = ?";
                params.add(propertyType);
            }
            case 6 -> {
                System.out.print("Enter Minimum Applicant Income: ");
                int minIncome = scanner.nextInt();
                System.out.print("Enter Maximum Applicant Income: ");
                int maxIncome = scanner.nextInt();
                filterName = "Applicant Income";
                sqlCondition = "a.applicant_income_000s BETWEEN ? AND ?";
                params.add(minIncome);
                params.add(maxIncome);
            }
            case 7 -> {
                System.out.print("Enter Minimum Tract to MSAMD Income: ");
                double minTractIncome = scanner.nextDouble();
                System.out.print("Enter Maximum Tract to MSAMD Income: ");
                double maxTractIncome = scanner.nextDouble();
                filterName = "Tract to MSAMD Income";
                sqlCondition = "l.tract_to_msamd_income BETWEEN ? AND ?";
                params.add(minTractIncome);
                params.add(maxTractIncome);
            }
            default -> {
                System.out.println("Invalid filter choice.");
                return;
            }
        }
    
        // Add filter to FilterManager
        filterManager.addFilter(new Filter(filterName, sqlCondition, params));
        System.out.println("Filter added successfully.");
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
/**
 * Calculates the expected rate and displays loan details before packaging.
 */
/**
 * Calculates and displays the expected loan rate with total loans and amount.
 */
private void calculateAndDisplayRate() {
    List<Mortgage> mortgages = mortgageDAO.getFilteredMortgages(filterManager.getFilters());

    if (mortgages.isEmpty()) {
        System.out.println("No mortgages found based on current filters.");
        return;
    }

    // Total Loan Amount Calculation
    double totalLoanAmount = mortgages.stream()
            .mapToDouble(Mortgage::getLoanAmount)
            .sum();

    // Calculate Expected Rate
    double expectedRate = MortgageCalculator.calculateRate(mortgages);

    // Display Results
    System.out.printf("Number of Matching Mortgages: %d%n", mortgages.size());
    System.out.printf("Total Loan Amount: $%.2f%n", totalLoanAmount * 1000);  // Amount in dollars
    System.out.printf("Calculated Expected Rate: %.2f%%%n", expectedRate);

    // Proceed to Packaging Prompt
    System.out.print("Do you accept this rate and proceed to package loans? (yes/no): ");
    String response = scanner.nextLine().trim().toLowerCase();

    if (response.equals("yes")) {
        packageMortgages();  // Proceed with loan packaging
    } else {
        System.out.println("Rate rejected. Returning to the main menu.");
    }
}

    /**
 * Packages loans by updating their purchaser_type to 9 (Private Securitization).
 */
private void packageMortgages() {
    List<Mortgage> mortgages = mortgageDAO.getFilteredMortgages(filterManager.getFilters());

    if (mortgages.isEmpty()) {
        System.out.println("No mortgages found for packaging.");
        return;
    }

    System.out.printf("Found %d mortgages ready for packaging.%n", mortgages.size());
    System.out.printf("Total Loan Amount: $%.2f%n",
            mortgages.stream().mapToDouble(Mortgage::getLoanAmount).sum() * 1000);  // Amount in dollars

    // Confirm Loan Packaging
    System.out.print("Do you want to package these loans? (yes/no): ");
    String response = scanner.nextLine().trim().toLowerCase();

    if (!response.equals("yes")) {
        System.out.println("Packaging operation canceled.");
        return;
    }

    // Start Database Transaction
    try (Connection conn = DatabaseCon.connect()) {
        TransactionManager.beginTransaction(conn);

        String updateQuery = "UPDATE application SET purchaser_type = 9 WHERE application_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            for (Mortgage mortgage : mortgages) {
                stmt.setInt(1, mortgage.getApplicationId());
                stmt.executeUpdate();
            }

            TransactionManager.commitTransaction(conn);
            System.out.println("Mortgages successfully packaged!");
        } catch (SQLException e) {
            System.out.println("Error occurred during loan packaging. Rolling back changes.");
            TransactionManager.rollbackTransaction(conn);
            e.printStackTrace();
        }
    } catch (SQLException e) {
        System.out.println("Database connection error.");
        e.printStackTrace();
    }
}

}
