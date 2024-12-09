package edu.cs213.mortgage;

import java.util.List;

public class MortgageCalculator {
    private static final double BASE_RATE = 2.33;

    public static double calculateRate(List<Mortgage> mortgages) {
        double weightedSum = 0;
        double totalLoanAmount = 0;

        for (Mortgage m : mortgages) {
            double rate = m.getInterestRate() == 0 ? BASE_RATE : m.getInterestRate();
            double loanAmount = m.getLoanAmount();
            weightedSum += rate * loanAmount;
            totalLoanAmount += loanAmount;
        }
        return totalLoanAmount > 0 ? weightedSum / totalLoanAmount : BASE_RATE;
    }
}
