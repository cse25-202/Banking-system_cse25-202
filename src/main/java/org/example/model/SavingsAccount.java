package org.example.model;

public class SavingsAccount extends Account implements InterestBearing {
    private final double interestRate = 0.0005;   // 0.05% monthly

    public SavingsAccount(String accountNumber, String branch, Customer owner, double initialDeposit) {
        super(accountNumber, branch, owner);
        if (initialDeposit > 0) deposit(initialDeposit);
    }

    @Override
    public String getAccountType() { return "Savings"; }

    @Override
    public boolean withdraw(double amount) {
        return false;   // savings accounts do not allow withdrawals
    }

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