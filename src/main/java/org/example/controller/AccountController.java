package org.example.controller;

import org.example.model.*;

import java.util.ArrayList;
import java.util.List;

public class AccountController {
    private final Customer customer;
    private int nextNumber = 100;

    public AccountController(Customer customer) {
        this.customer = customer;
    }

    public String getCustomerName() {
        return customer.getDisplayName();
    }

    // account labels for the drop-down, e.g. "S001 - Savings"
    public List<String> getAccountLabels() {
        List<String> labels = new ArrayList<>();
        for (Account a : customer.getAccounts()) {
            labels.add(a.getAccountNumber() + " - " + a.getAccountType());
        }
        return labels;
    }

    public String getBalanceText(String label) {
        Account a = find(label);
        return a == null ? "" : String.format("BWP %.2f", a.getBalance());
    }

    public List<String> getTransactionLines(String label) {
        List<String> lines = new ArrayList<>();
        Account a = find(label);
        if (a != null) {
            for (Transaction t : a.getTransactions()) {
                lines.add(t.getTransactionType() + "  |  BWP " + String.format("%.2f", t.getAmount())
                        + "  |  " + t.getTimestamp().toString().replace('T', ' ').substring(0, 19));
            }
        }
        return lines;
    }

    public String deposit(String label, String amountText) {
        Account a = find(label);
        if (a == null) return "Select an account first";
        try {
            double amount = Double.parseDouble(amountText.trim());
            a.deposit(amount);
            return "Deposit successful";
        } catch (NumberFormatException e) {
            return "Enter a valid amount";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    public String withdraw(String label, String amountText) {
        Account a = find(label);
        if (a == null) return "Select an account first";
        try {
            double amount = Double.parseDouble(amountText.trim());
            if (a.withdraw(amount)) return "Withdrawal successful";
            if (a instanceof SavingsAccount) return "Savings accounts do not allow withdrawals";
            return "Withdrawal failed: check the amount and balance";
        } catch (NumberFormatException e) {
            return "Enter a valid amount";
        }
    }

    // polymorphism: every account decides for itself whether it pays interest
    public String payMonthlyInterest() {
        for (Account a : customer.getAccounts()) {
            a.applyInterest();
        }
        return "Monthly interest paid to interest-bearing accounts";
    }

    public String openAccount(String type, String depositText, String employer, String employerAddress) {
        try {
            double deposit = depositText == null || depositText.isBlank() ? 0 : Double.parseDouble(depositText.trim());
            String number = type.substring(0, 1) + (++nextNumber);
            switch (type) {
                case "Savings" -> new SavingsAccount(number, "Rail Park Mall", customer, deposit);
                case "Investment" -> new InvestmentAccount(number, "Rail Park Mall", customer, deposit);
                case "Cheque" -> new ChequeAccount(number, "Rail Park Mall", customer, employer, employerAddress);
                default -> { return "Choose an account type"; }
            }
            return "Account " + number + " opened";
        } catch (NumberFormatException e) {
            return "Enter a valid amount";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    private Account find(String label) {
        if (label == null) return null;
        String number = label.split(" - ")[0];
        for (Account a : customer.getAccounts()) {
            if (a.getAccountNumber().equals(number)) return a;
        }
        return null;
    }
}