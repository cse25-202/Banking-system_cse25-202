package org.example.model;

import java.time.LocalDateTime;

public class Transaction {
    private static int counter = 1;
    private final int transactionID;
    private final String transactionType;
    private final double amount;
    private final LocalDateTime timestamp;

    public Transaction(String transactionType, double amount) {
        this.transactionID = counter++;
        this.transactionType = transactionType;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }
    public Transaction(int transactionID, String transactionType, double amount, LocalDateTime timestamp) {
        this.transactionID = transactionID;
        this.transactionType = transactionType;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public int getTransactionID() { return transactionID; }
    public String getTransactionType() { return transactionType; }
    public double getAmount() { return amount; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return transactionID + " | " + transactionType + " | BWP" + amount + " | " + timestamp;
    }
}