package edu.cs213.mortgage;

public class Mortgage {
    private int applicationId;
    private String respondentId;
    private int loanType;
    private int loanAmount;
    private String actionTakenName;
    private int actionTaken;
    private String msamdName;
    private int msamd;
    private String countyName;
    private int applicantIncome;
    private double rateSpread;      
    private int purchaserType;
    private int lienStatus;

    public Mortgage(int applicationId, String respondentId, int loanType, int loanAmount,
                    String actionTakenName, int actionTaken, String msamdName, int msamd,
                    String countyName, int applicantIncome, double rateSpread, int purchaserType, int lienStatus) {
        this.applicationId = applicationId;
        this.respondentId = respondentId;
        this.loanType = loanType;
        this.loanAmount = loanAmount;
        this.actionTakenName = actionTakenName;
        this.actionTaken = actionTaken;
        this.msamdName = msamdName;
        this.msamd = msamd;
        this.countyName = countyName;
        this.applicantIncome = applicantIncome;
        this.rateSpread = rateSpread;
        this.purchaserType = purchaserType;
        this.lienStatus = lienStatus;
    }

    // Getter for lienStatus
    public int getLienStatus() {
        return lienStatus;
    }

    // Other existing getters (if needed)
    public int getApplicationId() { return applicationId; }
    public String getRespondentId() { return respondentId; }
    public int getLoanType() { return loanType; }
    public int getLoanAmount() { return loanAmount; }
    public String getActionTakenName() { return actionTakenName; }
    public int getActionTaken() { return actionTaken; }
    public String getMsamdName() { return msamdName; }
    public int getMsamd() { return msamd; }
    public String getCountyName() { return countyName; }
    public int getApplicantIncome() { return applicantIncome; }
    public double getRateSpread() { return rateSpread; }
    public int getPurchaserType() { return purchaserType; }
}
