package org.example.model;

public class InvestmentAccount extends Account implements InterestBearing {
    private static final double MIN_OPENING_DEPOSIT = 500.00;
    private final double interestRate = 0.05;   // 5% monthly

    public InvestmentAccount(String accountNumber, String branch, Customer owner, double openingDeposit) {
        super(accountNumber, branch, checkOpening(owner, openingDeposit));
        deposit(openingDeposit);
    }

    private static Customer checkOpening(Customer owner, double openingDeposit) {
        if (openingDeposit < MIN_OPENING_DEPOSIT) {
            throw new IllegalArgumentException("Investment account needs an opening deposit of at least BWP500.00");
        }
        return owner;
    }

    @Override
    public String getAccountType() { return "Investment"; }

    @Override
    public double calculateInterest() { return balance * interestRate; }

    @Override
    public void payInterest() {
        double interest = calculateInterest();
        if (interest > 0) {
            balance += interest;
            recordTransaction("INTEREST", interest);
        }
    }

    public void depositMonthlyInterest() { payInterest(); }

    @Override
    public void applyInterest() { payInterest(); }
}