package org.example.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Account {
    private final String accountNumber;
    protected double balance;
    private String branch;
    private final Customer owner;
    private final List<Transaction> transactions = new ArrayList<>();

    public Account(String accountNumber, String branch, Customer owner) {
        this.accountNumber = accountNumber;
        this.branch = branch;
        this.owner = owner;
        owner.addAccount(this);   // an account cannot exist without a customer
    }

    public abstract String getAccountType();

    // Overloading: two versions of deposit
    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Deposit must be greater than 0");
        balance += amount;
        transactions.add(new Transaction("DEPOSIT", amount));
    }

    public void deposit(double amount, String note) {
        if (amount <= 0) throw new IllegalArgumentException("Deposit must be greater than 0");
        balance += amount;
        transactions.add(new Transaction("DEPOSIT (" + note + ")", amount));
    }

    // Subclasses may override this (overriding)
    public boolean withdraw(double amount) {
        if (amount <= 0 || amount > balance) return false;
        balance -= amount;
        transactions.add(new Transaction("WITHDRAWAL", amount));
        return true;
    }

    // Default: no interest. Interest-bearing accounts override it (polymorphism)
    public void applyInterest() { }

    protected void recordTransaction(String type, double amount) {
        transactions.add(new Transaction(type, amount));
    }

    public double getBalance() { return balance; }
    public String getAccountNumber() { return accountNumber; }
    public String getBranch() { return branch; }
    public Customer getOwner() { return owner; }
    public List<Transaction> getTransactions() { return transactions; }

    // used only when loading an account from the database
    public void restoreBalance(double balance) { this.balance = balance; }
    public void restoreTransaction(Transaction t) { transactions.add(t); }
}