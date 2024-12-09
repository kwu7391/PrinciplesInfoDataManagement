package edu.cs213.mortgage;

import java.util.Scanner;

public class MortgageApp {
    private final MortgageDAO mortgageDAO = new MortgageDAO();
    private final FilterManager filterManager = new FilterManager();
    private final Scanner scanner = new Scanner(System.in);

    public void run() {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Mortgage Backed Securities System ---");
            System.out.println("1. Add Filter");
            System.out.println("2. Delete Filter");
            System.out.println("3. Calculate Rate");
            System.out.println("4. Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> addFilter();
                case 2 -> deleteFilter();
                case 3 -> calculateRate();
                case 4 -> running = false;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
        System.out.println("Thank you for using the system!");
    }

    private void addFilter() {
        System.out.println("Add Filter (Enter County or Loan Type): ");
        String filterType = scanner.next();
        System.out.println("Enter filter value: ");
        String filterValue = scanner.next();
        String condition = filterType + " = ?";
        filterManager.addFilter(new Filter(filterType, condition));
    }

    private void deleteFilter() {
        System.out.println("Delete Filter (Enter name): ");
        String filterName = scanner.next();
        filterManager.getFilters().removeIf(f -> f.getName().equalsIgnoreCase(filterName));
    }

    private void calculateRate() {
        List<Mortgage> filteredMortgages = mortgageDAO.getFilteredMortgages(
            "SELECT * FROM mortgages WHERE action_taken = 'Loan originated'",
            new ArrayList<>()
        );
        double rate = MortgageCalculator.calculateRate(filteredMortgages);
        System.out.println("Calculated Rate: " + rate + "%");
    }
}
