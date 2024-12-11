package edu.cs213.mortgage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import static java.util.Comparator.comparing;


public class MortgageApp {
    private final MortgageDAO mortgageDAO = new MortgageDAO();
    private final FilterManager filterManager = new FilterManager();
    private final Scanner scanner = new Scanner(System.in);
    private boolean running = true;

    /**
     * Main Program Execution Loop
     */
    public void run() {
        while (running) {
            List<Filter> displayFilters = filterManager.getFilters();
            System.out.println("\nACTIVE FILTERS: ");
            if (displayFilters.size() == 0)
            {
                System.out.println("-\tNone");
            }
            displayFilters.sort(comparing(Filter::getChoice));
            StringBuilder display = new StringBuilder("");
            for (int i = 0; i < displayFilters.size(); i++) 
            {
                if (i == 0)
                {
                    if (i + 1 < displayFilters.size())
                    {
                        if (displayFilters.get(i+1).getChoice() == displayFilters.get(i).getChoice())
                        {
                            display.append("(").append(displayFilters.get(i).getName());
                        }
                        else
                        {
                            display.append(displayFilters.get(i).getName());
                        }
                    }
                    else
                    {
                
                        display.append(displayFilters.get(i).getName());
                    }
                }
                else
                {
                    if (displayFilters.get(i).getChoice() == displayFilters.get(i-1).getChoice())
                    {
                        if (i + 1 < displayFilters.size())
                        {
                            if (displayFilters.get(i+1).getChoice() == displayFilters.get(i).getChoice())
                            {
                                display.append(" OR ").append(displayFilters.get(i).getName());
                            }
                            else
                            {
                                display.append(" OR ").append(displayFilters.get(i).getName()).append(")");
                            }
                        }
                        else
                        {
                           
                            display.append(" OR ").append(displayFilters.get(i).getName()).append(")");
                        }
                    }
                    else
                    {
                        if (i + 1 < displayFilters.size())
                        {
                            if (displayFilters.get(i+1).getChoice() == displayFilters.get(i).getChoice())
                            {
                                display.append(" AND (").append(displayFilters.get(i).getName());
                            }
                            else
                            {
                                display.append(" AND ").append(displayFilters.get(i).getName());
                            }
                        }
                        else
                        {
                            
                            display.append(" AND ").append(displayFilters.get(i).getName());
                        }
                    }
                }
            } 
            System.out.println(display);
            System.out.println("\n--- Mortgage Backed Securities System ---");
            System.out.println("1. Add Filter");
            System.out.println("2. Delete Filter");
            System.out.println("3. View Matching Mortgages");
            System.out.println("4. Calculate and Package Mortgages");
            System.out.println("5. Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            System.out.println("\n");
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
        System.out.println("1. MSAMD ");
        //System.out.println("2. County Name");
        System.out.println("2. Loan Type ");
        System.out.println("3. Loan Purpose ");
        System.out.println("4. Property Type ");
        System.out.println("5. Applicant Income Range");

        System.out.print("Enter filter number: ");
        int filterChoice = scanner.nextInt();
        System.out.println("\n");
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
                System.out.println("\n");
                String msamd = "";
                int msamdVal = 0;
                switch (choice) 
                {
                    case 1 -> 
                    {
                        msamd = "Allentown, Bethlehem, Easton - PA, NJ";
                        msamdVal = 10900;
                    }
                    case 2 -> 
                    {
                        msamd = "Atlantic City, Hammonton - NJ";
                        msamdVal = 12100;
                    }
                    case 3 -> 
                    {
                        msamd = "Camden - NJ";
                        msamdVal = 15804;

                    }
                    case 4 -> 
                    {
                        msamd = "Newark - NJ, PA";
                        msamdVal = 35084;

                    }
                    case 5 -> 
                    {
                        msamd = "New York, Jersey City, White Plains - NY, NJ";
                        msamdVal = 35614;

                    }
                    case 6 -> 
                    {
                        msamdVal = 35620;
                    }
                    case 7 -> 
                    {
                        msamd = "Ocean City - NJ";
                        msamdVal = 36140;

                    }
                    case 8 -> 
                    {
                        msamdVal = 37980;
                    }
                    case 9 -> 
                    {
                        msamd = "Trenton - NJ";
                        msamdVal = 45940;

                    }
                    case 10 -> 
                    {
                        msamd = "Vineland, Bridgeton - NJ";
                        msamdVal = 47220;

                    }
                    case 11 -> 
                    {
                        msamd = "Wilmington - DE, MD, NJ";
                        msamdVal = 48864;

                    }    
                    default -> 
                    {
                        System.out.println("Invalid choice.");
                        return;    
                    }
                }
                if (choice == 6 || choice == 8)
                {
                    filterName = "MSAMD = " + msamdVal;
                    sqlCondition = "a.msamd = " + msamdVal;
                }
                else
                {
                    filterName = "MSAMD = " + msamd;
                    sqlCondition = "a.msamd = " + msamdVal;
                }
                params = List.of(msamd, "%" + msamd + "%");
            }
            /* 
            case 2 -> {
                System.out.println("""
                        ACCEPTABLE COUNTIES:

                        1. Atlantic County
                        2. Bergen County
                        3. Burlington County
                        4. Camden County
                        5. Cape May County
                        6. Cumberland County
                        7. Essex County
                        8. Gloucester County
                        9. Hudson County
                        10. Hunterdon County
                        11. Mercer County
                        12. Middlesex County
                        13. Monmouth County
                        14. Morris County
                        15. Ocean County
                        16. Passaic County
                        17. Salem County
                        18. Somerset County
                        19. Sussex County
                        20. Union County
                        21. Warren County
                        """);
                System.out.print("Enter choice #: ");
                int choice = scanner.nextInt();
                System.out.println("\n");
                String county = "";
                int code = 0;
                switch (choice) 
                {
                    case 1 -> 
                    {
                        county = "Atlantic County";
                        code = 1;
                    }
                    case 2 -> 
                    {
                        county = "Bergen County";
                        code = 3;
                    }
                    case 3 -> 
                    {
                        county = "Burlington County";
                        code = 5;
                    }
                    case 4 -> 
                    {
                        county = "Camden County";
                        code = 7;
                    }
                    case 5 -> 
                    {
                        county = "Cape May County";
                        code = 9;
                    }
                    case 6 -> 
                    {
                        county = "Cumberland County";
                        code = 11;
                    }
                    case 7 -> 
                    {
                        county = "Essex County";
                        code = 13;
                    }
                    case 8 -> 
                    {
                        county = "Gloucester County";
                        code = 15;
                    }
                    case 9 -> 
                    {
                        county = "Hudson County";
                        code = 17;
                    }
                    case 10 -> 
                    {
                        county = "Hunterdon County";
                        code = 19;
                    }
                    case 11 -> 
                    {
                        county = "Mercer County";
                        code = 21;
                    }    
                    case 12 -> 
                    {
                        county = "Middlesex County";
                        code = 23;
                    }  
                    case 13 -> 
                    {
                        county = "Monmouth County";
                        code = 25;
                    }  
                    case 14 -> 
                    {
                        county = "Morris County";
                        code = 27;
                    }  
                    case 15 -> 
                    {
                        county = "Ocean County";
                        code = 29;
                    }  
                    case 16 -> 
                    {
                        county = "Passaic County";
                        code = 31;
                    }  
                    case 17 -> 
                    {
                        county = "Salem County";
                        code = 33;
                    }  
                    case 18 -> 
                    {
                        county = "Somerset County";
                        code = 35;
                    }  
                    case 19 -> 
                    {
                        county = "Sussex County";
                        code = 37;
                    }  
                    case 20 -> 
                    {
                        county = "Union County";
                        code = 39;
                    }  
                    case 21 -> 
                    {
                        county = "Warren County";
                        code = 41;
                    }  
                    default -> 
                    {
                        System.out.println("Invalid choice.");
                        return;    
                    }
                }
                filterName = "County = " + county;
                sqlCondition = "a.county_code = " + code;
                params = List.of("%" + code + "%");
            }
                */
            case 2 -> {
                System.out.println("""
                        ACCEPTABLE LOAN TYPES:

                        1.  Conventional
                        2.  FHA-insured
                        3.  VA-guaranteed
                        4.  FSA/RHS-guaranteed
                        """);
                System.out.print("Enter choice #: ");
                int choice = scanner.nextInt();
                System.out.println("\n");
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
                sqlCondition = "a.loan_type = " + choice;
                params = List.of(choice);
            }
            case 3 -> {
                System.out.println("""
                        ACCEPTABLE LOAN PURPOSES:

                        1.  Home purchase
                        2.  Home improvement
                        3.  Refinancing
                        """);
                System.out.print("Enter choice #: ");
                int choice = scanner.nextInt();
                System.out.println("\n");
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
                sqlCondition = "a.loan_purpose = " + choice;
                params = List.of(choice);
            }
            case 4 -> {
                System.out.println("""
                        ACCEPTABLE PROPERTY TYPES:

                        1.  One-to-four family dwelling (other than manufactured housing)
                        2.  Manufactured housing
                        3.  Multifamily dwelling
                        """);
                System.out.print("Enter choice #: ");
                int choice = scanner.nextInt();
                System.out.println("\n");
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
                sqlCondition = "a.property_type = " + choice;
                params = List.of(choice);
            }
            case 5 -> {
                System.out.print("Enter Minimum Applicant Income: ");
                int minIncome = scanner.nextInt();
                System.out.print("Enter Maximum Applicant Income: ");
                int maxIncome = scanner.nextInt();
                filterName = "Applicant Income Range: " + minIncome + " - " + maxIncome;
                sqlCondition = "(a.applicant_income_000s BETWEEN " + minIncome + " AND " + maxIncome + ")";
                params = List.of(minIncome, maxIncome);
            }
            default -> {
                System.out.println("Invalid filter choice.");
                return;
            }
        }

        filterManager.addFilter(new Filter(filterName, sqlCondition, params, filterChoice));
        System.out.println("Filter added: " + filterName);
        System.out.println("\n");
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
                String updateQuery = "UPDATE application SET purchaser_type = 5 WHERE application_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                    for (Mortgage mortgage : mortgages) {
                        stmt.setInt(1, mortgage.getApplicationId());
                        stmt.executeUpdate();
                    }
                    System.out.println("Mortgages successfully packaged!");
                    running = false;
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