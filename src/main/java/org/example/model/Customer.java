package org.example.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Customer {
    private String customerID;
    private String address;
    private String username;
    private String password;
    private final List<Account> accounts = new ArrayList<>();

    public Customer(String customerID, String address, String username, String password) {
        this.customerID = customerID;
        this.address = address;
        this.username = username;
        this.password = password;
    }

    public abstract String getDisplayName();

    public void addAccount(Account a) { accounts.add(a); }
    public List<Account> getAccounts() { return accounts; }

    public String getCustomerID() { return customerID; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getUsername() { return username; }
    public boolean checkPassword(String p) { return password.equals(p); }
}