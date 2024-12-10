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
            List<Filter> displayFilters = filterManager.getFilters();
            System.out.println("ACTIVE FILTERS: ");
            if (displayFilters.size() == 0)
            {
                System.out.println("-\tNone");
            }
            for (int i = 0; i < displayFilters.size(); i++) 
            {
                System.out.println("-\t" + displayFilters.get(i));
            }   
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
                System.out.println("""
                        ACCEPTABLE MSAMDs:

                        1.  Allentown, Bethlehem, Easton - PA, NJ
                        2.  Atlantic City, Hammonton - NJ
                        3.  Camden - NJ
                        4.  Newark - NJ, PA
                        5.  New York, Jersey City, White Plains - NY, NJ
                        6.  35620
                        7.  Ocean City - NJ
                        8.  37980
                        9.  Trenton - NJ
                        10. Vineland, Bridgeton - NJ
                        11. Wilmington - DE, MD, NJ
                        """);
                System.out.print("Enter choice #: ");
                int choice = scanner.nextInt();
                String msamd = "";
                switch (choice) 
                {
                    case 1 -> 
                    {
                        msamd = "Allentown, Bethlehem, Easton - PA, NJ";
                    }
                    case 2 -> 
                    {
                        msamd = "Atlantic City, Hammonton - NJ";

                    }
                    case 3 -> 
                    {
                        msamd = "Camden - NJ";

                    }
                    case 4 -> 
                    {
                        msamd = "Newark - NJ, PA";
                    }
                    case 5 -> 
                    {
                        msamd = "New York, Jersey City, White Plains - NY, NJ";
                    }
                    case 6 -> 
                    {
                        msamd = "35620";
                    }
                    case 7 -> 
                    {
                        msamd = "Ocean City - NJ";
                    }
                    case 8 -> 
                    {
                        msamd = "37980";
                    }
                    case 9 -> 
                    {
                        msamd = "Trenton - NJ";
                    }
                    case 10 -> 
                    {
                        msamd = "Vineland, Bridgeton - NJ";
                    }
                    case 11 -> 
                    {
                        msamd = "Wilmington - DE, MD, NJ";
                    }    
                    default -> 
                    {
                        System.out.println("Invalid choice.");
                        return;    
                    }
                }
                filterName = "MSAMD = " + msamd;
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
                System.out.println("""
                        ACCEPTABLE LOAN TYPES:

                        1.  Conventional
                        2.  FHA-insured
                        3.  VA-guaranteed
                        4.  FSA/RHS-guaranteed
                        """);
                System.out.print("Enter choice #: ");
                int choice = scanner.nextInt();
                String loanType = "";
                switch (choice) 
                {
                    case 1 -> 
                    {
                        loanType = "Conventional";
                    }
                    case 2 -> 
                    {
                        loanType = "FHA-insured";

                    }
                    case 3 -> 
                    {
                        loanType = "VA-guaranteed";

                    }
                    case 4 -> 
                    {
                        loanType = "FSA/RHS-guaranteed";
                    }
                    default -> 
                    {
                        System.out.println("Invalid choice.");
                        return;    
                    }
                }
                filterName = "Loan Type = " + loanType;
                sqlCondition = "a.loan_type = ?";
                params = List.of(choice);
            }
            case 4 -> {
                System.out.println("""
                        ACCEPTABLE LOAN PURPOSES:

                        1.  Home purchase
                        2.  Home improvement
                        3.  Refinancing
                        """);
                System.out.print("Enter choice #: ");
                int choice = scanner.nextInt();
                String loanPurpose = "";
                switch (choice) 
                {
                    case 1 -> 
                    {
                        loanPurpose = "Home purchase";
                    }
                    case 2 -> 
                    {
                        loanPurpose = "Home improvement";

                    }
                    case 3 -> 
                    {
                        loanPurpose = "Refinancing";

                    }
                    default -> 
                    {
                        System.out.println("Invalid choice.");
                        return;    
                    }
                }
                filterName = "Loan Purpose = " + loanPurpose;
                sqlCondition = "a.loan_purpose = ?";
                params = List.of(choice);
            }
            case 5 -> {
                System.out.println("""
                        ACCEPTABLE LOAN PURPOSES:

                        1.  One-to-four family dwelling (other than manufactured housing)
                        2.  Manufactured housing
                        3.  Multifamily dwelling
                        """);
                System.out.print("Enter choice #: ");
                int choice = scanner.nextInt();
                String propertyType = "";
                switch (choice) 
                {
                    case 1 -> 
                    {
                        propertyType = "One-to-four family dwelling (other than manufactured housing)";
                    }
                    case 2 -> 
                    {
                        propertyType = "Manufactured housing";

                    }
                    case 3 -> 
                    {
                        propertyType = "Multifamily dwelling";

                    }
                    default -> 
                    {
                        System.out.println("Invalid choice.");
                        return;    
                    }
                }
                filterName = "Property Type = " + propertyType;
                sqlCondition = "a.property_type = ?";
                params = List.of(choice);
            }
            case 6 -> {
                System.out.print("Enter Minimum Applicant Income: ");
                int minIncome = scanner.nextInt();
                System.out.print("Enter Maximum Applicant Income: ");
                int maxIncome = scanner.nextInt();
                filterName = "Applicant Income Range: " + minIncome + " - " + maxIncome;
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
