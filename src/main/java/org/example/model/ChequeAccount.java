package org.example.model;

public class ChequeAccount extends Account {
    private final String companyName;
    private final String companyAddress;

    public ChequeAccount(String accountNumber, String branch, Customer owner,
                         String companyName, String companyAddress) {
        super(accountNumber, branch, checkEmployer(owner, companyName, companyAddress));
        this.companyName = companyName;
        this.companyAddress = companyAddress;
    }

    private static Customer checkEmployer(Customer owner, String name, String address) {
        if (name == null || name.isBlank() || address == null || address.isBlank()) {
            throw new IllegalArgumentException("Cheque account needs employer name and address");
        }
        return owner;
    }

    @Override
    public String getAccountType() { return "Cheque"; }

    @Override
    public boolean withdraw(double amount) {
        return super.withdraw(amount);   // any amount up to the balance
    }

    public String getCompanyName() { return companyName; }
    public String getCompanyAddress() { return companyAddress; }
}