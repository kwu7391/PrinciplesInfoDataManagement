package edu.cs213.mortgage;

public class Mortgage {
    private int mortgageId;
    private double loanAmount;
    private double interestRate;
    private String purchaserType;
    private String county;
    private String msamd;

    public Mortgage(int mortgageId, double loanAmount, double interestRate, String purchaserType, String county, String msamd) {
        this.mortgageId = mortgageId;
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        this.purchaserType = purchaserType;
        this.county = county;
        this.msamd = msamd;
    }

    // Getters and setters
    public int getMortgageId() { return mortgageId; }
    public double getLoanAmount() { return loanAmount; }
    public double getInterestRate() { return interestRate; }
    public String getPurchaserType() { return purchaserType; }
    public String getCounty() { return county; }
    public String getMsamd() { return msamd; }
}
